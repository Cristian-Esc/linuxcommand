package net.esserdi.linuxcommand.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
<<<<<<< HEAD
import net.esserdi.linuxcommand.dto.CommandDTO;
=======

>>>>>>> d4e1e4c (Guardar cambios antes de sincronizar)
import net.esserdi.linuxcommand.model.LinuxCommand;

public interface LinuxCommandService {

	/**
	 * Guarda un nuevo comando o actualiza un comando existente.
	 *
	 * @param title       El título del comando (debe ser único).
	 * @param description La descripción del comando.
	 * @param command     El comando en formato de texto.
	 * @param active      Estado del comando (activo o inactivo).
	 * @param url         La URL asociada al comando.
	 * @return El comando guardado o actualizado.
	 * @throws IllegalArgumentException Si ya existe un comando con el mismo título.
	 */
	LinuxCommand saveCommand(LinuxCommand request);

	/**
	 * Edita un comando existente.
	 *
	 * @param id          El ID del comando a editar.
	 * @param title       El nuevo título del comando.
	 * @param description La nueva descripción del comando.
	 * @param command     El nuevo comando en formato de texto.
	 * @param active      El nuevo estado del comando (activo o inactivo).
	 * @param url         La nueva URL asociada al comando.
	 * @return El comando actualizado.
	 */
	LinuxCommand editCommand(Long id, LinuxCommand request);

	/**
	 * Obtiene la lista de todos los comandos registrados en el sistema.
	 *
	 * @return Una lista de todos los comandos.
	 */
<<<<<<< HEAD
	List<CommandDTO> getAllCommands() throws Exception;
=======
	List<LinuxCommand> getAllCommands();
>>>>>>> d4e1e4c (Guardar cambios antes de sincronizar)

	/**
	 * Ejecuta un comando según su título.
	 *
	 * @param title El título del comando a ejecutar.
	 * @return El resultado de la ejecución del comando.
	 * @throws IllegalArgumentException Si no se encuentra el comando o está
	 *                                  inactivo.
	 * @throws IllegalStateException    Si el comando está inactivo.
	 * @throws Exception                Si ocurre un error durante la ejecución del
	 *                                  comando.
	 */
	CompletableFuture<String> executeCommand(String title) throws Exception;

	/**
	 * Genera un código QR para un texto dado.
	 *
	 * @param text   El texto a codificar en el código QR.
	 * @param width  El ancho del código QR.
	 * @param height La altura del código QR.
	 * @return Un arreglo de bytes que representa la imagen del código QR en formato
	 *         PNG.
	 * @throws Exception Si ocurre un error durante la generación del código QR.
	 */
	byte[] generateQRCode(String text, int width, int height) throws Exception;

	/**
	 * Busca un comando por su ID.
	 *
	 * @param id El ID del comando a buscar.
	 * @return Un Optional que contiene el comando si existe, o vacío si no se
	 *         encuentra.
	 */
	Optional<LinuxCommand> findById(Long id);

	/**
	 * Elimina un comando por su ID.
	 *
	 * @param id El ID del comando a eliminar.
	 * @throws IllegalArgumentException Si no se encuentra un comando con el ID
	 *                                  proporcionado.
	 */
	void deleteCommand(Long id);

<<<<<<< HEAD


=======
>>>>>>> d4e1e4c (Guardar cambios antes de sincronizar)
}
