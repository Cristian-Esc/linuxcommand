package net.esserdi.linuxcommand.controller;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
=======

<<<<<<< HEAD
=======
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> 1138c29 (init repository)
>>>>>>> d4e1e4c (Guardar cambios antes de sincronizar)
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import net.esserdi.linuxcommand.config.ApiEndpoints;
import net.esserdi.linuxcommand.dto.CommandDTO;
import net.esserdi.linuxcommand.model.LinuxCommand;
<<<<<<< HEAD
import net.esserdi.linuxcommand.service.LinuxCommandService;
=======
<<<<<<< HEAD
import net.esserdi.linuxcommand.service.impl.LinuxCommandService;
=======
import net.esserdi.linuxcommand.service.LinuxCommandService;
>>>>>>> 1138c29 (init repository)
>>>>>>> d4e1e4c (Guardar cambios antes de sincronizar)

@RestController
@RequestMapping(ApiEndpoints.Commands.BASE)
public class LinuxCommandController {

<<<<<<< HEAD
	@Autowired
	private LinuxCommandService service;

	 @Autowired
	private ServletWebServerApplicationContext webServerAppContext;
=======
<<<<<<< HEAD
	private final LinuxCommandService service;

	public LinuxCommandController(LinuxCommandService service) {
		this.service = service;
	}
=======
	@Autowired
	private LinuxCommandService service;

>>>>>>> 1138c29 (init repository)
>>>>>>> d4e1e4c (Guardar cambios antes de sincronizar)

	@GetMapping
	public ResponseEntity<List<CommandDTO>> getAllCommands() throws Exception {
		return ResponseEntity.ok(service.getAllCommands());
	}

	@PostMapping
	public ResponseEntity<?> createCommandJson(@RequestBody LinuxCommand request) {
<<<<<<< HEAD
=======
<<<<<<< HEAD
		System.out.println("createCommand");
=======
		System.out.println("oooo");
		System.out.println(request);
>>>>>>> 1138c29 (init repository)
>>>>>>> d4e1e4c (Guardar cambios antes de sincronizar)
		try {
			LinuxCommand commandCreated = service.saveCommand(request);
			return ResponseEntity.ok(commandCreated);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", "Validation Error", "message", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(Map.of("error", "Internal Error", "message", "An unexpected error occurred."));
		}
	}

	@PutMapping(ApiEndpoints.Commands.BY_ID)
	public ResponseEntity<?> editCommand(@PathVariable Long id, @RequestBody LinuxCommand request) {
		try {
			LinuxCommand updatedCommand = service.editCommand(id, request);
			return ResponseEntity.ok(Map.of("message", "Command updated successfully.", "data", updatedCommand));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", "Validation Error", "message", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(Map.of("error", "Internal Error", "message", "An unexpected error occurred."));
		}
	}

	@DeleteMapping(ApiEndpoints.Commands.BY_ID)
	public ResponseEntity<Void> deleteCommand(@PathVariable Long id) {
		try {
			service.deleteCommand(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(ApiEndpoints.Commands.EXECUTE)
	public CompletableFuture<ResponseEntity<String>> executeCommand(@PathVariable String url) {
		try {
			CompletableFuture<String> output = service.executeCommand(url);
			return output.thenApply(res -> ResponseEntity.ok("✅ " + res));
		} catch (Exception e) {
			return CompletableFuture.completedFuture(ResponseEntity.badRequest().body("❌ " + e.getMessage()));
		}
	}

	@GetMapping(value = ApiEndpoints.Commands.QR_CODE, produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public ResponseEntity<byte[]> getQRCode(@PathVariable Long id, HttpServletRequest request) throws Exception {

		LinuxCommand command = service.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Command not found"));
<<<<<<< HEAD
		String urlCommand = command.getUrl();

		// Retorna la imagen en formato PNG
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(service.generateQRCode(urlCommand, 400, 400));
=======

		// Construir la URL con hostname y puerto
<<<<<<< HEAD
		System.err.println(id);
		System.err.println(command);
=======
>>>>>>> 1138c29 (init repository)
		String hostname = request.getServerName();
		int port = request.getServerPort();
		String title = command.getUrl();
		String qrContent = String.format("http://%s:%d/api/commands/execute/%s", hostname, port, title);

		// Retorna la imagen en formato PNG
<<<<<<< HEAD
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(service.generateQRCode(qrContent, 200, 200));
=======
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(service.generateQRCode(qrContent, 400, 400));
>>>>>>> 1138c29 (init repository)
>>>>>>> d4e1e4c (Guardar cambios antes de sincronizar)
	}


    @GetMapping("/server-address")
    public String getServerAddress() {
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            int port = webServerAppContext.getWebServer().getPort();
            System.out.println(hostname + ":" + port);

            return hostname + ":" + port;
        } catch (Exception e) {
            return "localhost:" + webServerAppContext.getWebServer().getPort();
        }
    }

}
