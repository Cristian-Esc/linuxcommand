package net.esserdi.linuxcommand.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
<<<<<<< HEAD
@SequenceGenerator(name = "Custom_Sequence", sequenceName = "custom_sequence", initialValue = 1000, allocationSize = 1)
=======
<<<<<<< HEAD
@SequenceGenerator(name = "Custom_Sequence", sequenceName = "custom_sequence", initialValue = 1000, // Same as the
																									// sequence's
																									// starting value
		allocationSize = 1 // Adjust allocation size as needed
)
=======
@SequenceGenerator(name = "Custom_Sequence", sequenceName = "custom_sequence", initialValue = 1000, allocationSize = 1)
>>>>>>> 1138c29 (init repository)
>>>>>>> d4e1e4c (Guardar cambios antes de sincronizar)
public class LinuxCommand {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Custom_Sequence")
	private Long id;

	private String title;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(columnDefinition = "TEXT")
	private String command;

	private boolean active;
	private String url;

	public LinuxCommand(String title, String description, String command, boolean active, String url) {
		this.title = title;
		this.description = description;
		this.command = command;
		this.active = active;
		this.url = url;
	}

}
