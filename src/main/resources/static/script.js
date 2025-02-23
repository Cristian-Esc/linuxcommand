function crudApp() {
    const apiUrl = `${window.location.origin}/api/commands`;

    return {
        commands: [],
        showModal: false,
        showQRModal: false,
        isEditing: false,
        showDeleteModal: false,
        commandToDelete: null, // Comando seleccionado para eliminar
        form: {
            id: null,
            title: '',
            description: '',
            command: '',
            url: '',
            active: false,
        },
        qrCodeSrc: '',
		errorMessage: '',
		
		
		setError(message) {
		     this.errorMessage = message;
		     setTimeout(() => {
		         this.errorMessage = ''; // Limpia el mensaje después de 9 segundos
		     }, 9000);
		 },


		 async loadCommands() {
		     try {
		         const response = await fetch(apiUrl);
		         if (response.ok) {
		             this.commands = await response.json();
		         } else {
		             this.setError(`Error al cargar los datos: ${response.statusText}`);
		         }
		     } catch (error) {
		         this.setError(`Error en fetch: ${error.message}`);
		     }
		 },

        openCreateModal() {
            this.resetForm();
            this.isEditing = false;
			const modal = new bootstrap.Modal(document.getElementById('fromModal'));
			modal.show();
        },

        openEditModal(command) {
            this.form = { ...command };
            this.isEditing = true;
			const modal = new bootstrap.Modal(document.getElementById('fromModal'));
			modal.show();
        },

        closeModal() {
			console.log('holsss')
            this.showModal = false;
        },

        resetForm() {
            this.form = {
                id: null,
                title: '',
                description: '',
                command: '',
                url: '',
                active: false,
            };
        },


		async createCommand() {
		    try {
		        const response = await fetch(apiUrl, {
		            method: 'POST',
		            headers: { 'Content-Type': 'application/json' },
		            body: JSON.stringify(this.form),
		        });
				const data = await response.json(); // Parsear la respuesta
				
		        if (response.ok) {
		            this.closeModal();
		            this.loadCommands();
		        } else {
		            this.setError(data.message);
		        }
		    } catch (error) {
		        this.setError(`Error en fetch: ${error.message}`);
		    }
		},

		async updateCommand() {
		    try {
		        const response = await fetch(`${apiUrl}/${this.form.id}`, {
		            method: 'PUT',
		            headers: { 'Content-Type': 'application/json' },
		            body: JSON.stringify(this.form),
		        });
				const data = await response.json(); // Parsear la respuesta
				
		        if (response.ok) {
		            this.closeModal();
		            this.loadCommands();
		        } else {
		            this.setError(data.message);

		        }
		    } catch (error) {
		        this.setError(`Error en fetch: ${data.message}`);
		    }
		},

		async deleteCommand() {
		    try {
		        const response = await fetch(`${apiUrl}/${this.commandToDelete.id}`, {
		            method: 'DELETE',
		        });
		        if (response.ok) {
		            this.closeDeleteModal();
		            this.loadCommands();
		        } else {
		            this.setError(`Error al eliminar comando: ${response.statusText}`);
		        }
		    } catch (error) {
		        this.setError(`Error en fetch: ${error.message}`);
		    }
		},
		
		
        async showQR(id) {
            this.qrCodeSrc = `${apiUrl}/${id}/qr`;
			const modal = new bootstrap.Modal(document.getElementById('qrModal'));
			modal.show();
        },

        // Abrir el modal de eliminación
        openDeleteModal(command) {
            this.commandToDelete = command; // Guardamos el comando a eliminar
			const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
			modal.show();
        },

        // Cerrar el modal de eliminación
        closeDeleteModal() {
            this.showDeleteModal = false;
			console.log("EEE")
        },
		
		
		generateUrl() {
		           // Convierte el título en una URL amigable
		           this.form.url = this.form.title
		               .toLowerCase() // Convierte a minúsculas
		               .trim() // Elimina espacios al inicio y final
		               .replace(/\s+/g, '-') // Reemplaza espacios por guiones
		               .replace(/[^\w\-]+/g, ''); // Elimina caracteres no válidos
		       },


    };
}
