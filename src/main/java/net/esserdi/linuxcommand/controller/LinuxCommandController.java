package net.esserdi.linuxcommand.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
import net.esserdi.linuxcommand.model.LinuxCommand;
import net.esserdi.linuxcommand.service.impl.LinuxCommandService;

@RestController
@RequestMapping("/api/commands")
public class LinuxCommandController {

	private final LinuxCommandService service;

	public LinuxCommandController(LinuxCommandService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<LinuxCommand>> getAllCommands() {
		return ResponseEntity.ok(service.getAllCommands());
	}

	@PostMapping
	public ResponseEntity<?> createCommandJson(@RequestBody LinuxCommand request) {
		System.out.println("createCommand");
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

	@PutMapping("/{id}")
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

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCommand(@PathVariable Long id) {
		try {
			service.deleteCommand(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/execute/{url}")
	public CompletableFuture<ResponseEntity<String>> executeCommand(@PathVariable String url) {
	    try {
	        CompletableFuture<String> output = service.executeCommand(url);
	        return output.thenApply(res -> ResponseEntity.ok("✅ " + res));
	    } catch (Exception e) {
	        return CompletableFuture.completedFuture(ResponseEntity.badRequest().body("❌ " + e.getMessage()));
	    }
	}



	@GetMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public ResponseEntity<byte[]> getQRCode(@PathVariable Long id, HttpServletRequest request) throws Exception {

		LinuxCommand command = service.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Command not found"));

		// Construir la URL con hostname y puerto
		System.err.println(id);
		System.err.println(command);
		String hostname = request.getServerName();
		int port = request.getServerPort();
		String title = command.getUrl();
		String qrContent = String.format("http://%s:%d/api/commands/execute/%s", hostname, port, title);

		// Retorna la imagen en formato PNG
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(service.generateQRCode(qrContent, 200, 200));
	}

}
