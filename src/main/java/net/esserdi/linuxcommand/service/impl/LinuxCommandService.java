package net.esserdi.linuxcommand.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import net.esserdi.linuxcommand.model.LinuxCommand;
import net.esserdi.linuxcommand.repository.LinuxCommandRepository;
import net.esserdi.linuxcommand.service.ILinuxCommandService;
import net.esserdi.linuxcommand.service.SystemCommandService;

@Service
public class LinuxCommandService implements ILinuxCommandService {

	private final LinuxCommandRepository repository;
	private final SystemCommandService systemCommandService;

	public LinuxCommandService(LinuxCommandRepository repository, SystemCommandService systemCommandService) {
		this.repository = repository;
		this.systemCommandService = systemCommandService;
	}

	@Override
	public LinuxCommand saveCommand(LinuxCommand request) {
		// Verificar si ya existe un comando con el mismo título
		repository.findByUrl(request.getUrl()).ifPresent(existingCommand -> {
			throw new IllegalArgumentException("La URL ya existe en otro comando.");
		});

		LinuxCommand newCommand = new LinuxCommand(request.getTitle(), request.getDescription(), request.getCommand(),
				request.isActive(), request.getUrl());
		return repository.save(newCommand);
	}

	@Override
	public LinuxCommand editCommand(Long id, LinuxCommand request) {
		if (isInvalid(request)) {
			throw new IllegalArgumentException("Todos los campos son obligatorios.");
		}

		// Buscar el comando existente por ID
		LinuxCommand existingCommand = repository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Comando no encontrado."));

		// Verificar si la URL ya está en uso por otro comando
		repository.findByUrl(request.getUrl()).ifPresent(commandWithSameUrl -> {
			if (!commandWithSameUrl.getId().equals(id)) {
				throw new IllegalArgumentException("La URL ya existe en otro comando.");
			}
		});

		// Actualizar los valores del comando existente
		existingCommand.setTitle(request.getTitle());
		existingCommand.setDescription(request.getDescription());
		existingCommand.setCommand(request.getCommand());
		existingCommand.setActive(request.isActive());
		existingCommand.setUrl(request.getUrl());

		return repository.save(existingCommand);
	}

	private boolean isInvalid(LinuxCommand command) {
		return command.getTitle().isBlank() || command.getDescription().isBlank() || command.getCommand().isBlank()
				|| command.getUrl().isBlank();
	}

	@Override
	public List<LinuxCommand> getAllCommands() {
		return repository.findAll();
	}

	@Override
	public CompletableFuture<String> executeCommand(String url) throws Exception {
	    LinuxCommand command = repository.findByUrl(url)
	            .orElseThrow(() -> new IllegalArgumentException("Comando no encontrado o inactivo"));

	    if (!command.isActive()) {
	        throw new IllegalStateException("El comando no está activo.");
	    }
	    return systemCommandService.execute(command.getCommand());
	}


	@Override
	public byte[] generateQRCode(String text, int width, int height) throws Exception {
		BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
		return outputStream.toByteArray();
	}

	@Override
	public Optional<LinuxCommand> findById(Long id) {
		return repository.findById(id); // Se asume que existe un repositorio de JPA
	}

	@Override
	public void deleteCommand(Long id) {
		// Verificamos si el comando existe antes de eliminarlo
		Optional<LinuxCommand> commandOpt = repository.findById(id);
		if (commandOpt.isPresent()) {
			repository.deleteById(id);
		} else {
			throw new IllegalArgumentException("Comando con ID " + id + " no encontrado");
		}
	}

}
