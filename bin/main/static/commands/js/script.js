// Define esta función globalmente al inicio del archivo
function getCSRFToken() {
    return document.querySelector('meta[name="_csrf"]').getAttribute('content');
}

function getCSRFHeaderName() {
    return document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
}

function crudApp() {
    const apiUrl = `${window.location.origin}/api/commands`;

    return {
        commands: [],
        showModal: false,
        showQRModal: false,
        showDonationModal: false,
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
            this.showModal = true;
        },

        openEditModal(command) {
            this.form = { ...command };
            this.isEditing = true;
            this.showModal = true;
        },

        closeModal() {
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
                const csrfToken = getCSRFToken();
                const csrfHeader = getCSRFHeaderName();

                const response = await fetch(apiUrl, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        [csrfHeader]: csrfToken
                    },
                    body: JSON.stringify(this.form),
                });
                const data = await response.json();

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
                const csrfToken = getCSRFToken();
                const csrfHeader = getCSRFHeaderName();

                const response = await fetch(`${apiUrl}/${this.form.id}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        [csrfHeader]: csrfToken
                    },
                    body: JSON.stringify(this.form),
                });
                const data = await response.json();

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

        async deleteCommand() {
            try {
                const csrfToken = getCSRFToken();
                const csrfHeader = getCSRFHeaderName();
                const response = await fetch(`${apiUrl}/${this.commandToDelete.id}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        [csrfHeader]: csrfToken
                    },
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
            this.showQRModal = true;
        },

        // Abrir el modal de eliminación
        openDeleteModal(command) {
            this.commandToDelete = command;
            this.showDeleteModal = true;
        },

        // Cerrar el modal de eliminación
        closeDeleteModal() {
            this.showDeleteModal = false;
        },

        generateUrl() {
            // Convierte el título en una URL amigable
            this.form.url = this.form.title
                .toLowerCase()
                .trim()
                .replace(/\s+/g, '-')
                .replace(/[^\w\-]+/g, '');
        },
    };
}

// Configuración de Alpine.js
document.addEventListener('alpine:init', () => {
    // Store para CSRF
    Alpine.store('csrf', {
        token: document.querySelector('meta[name="_csrf"]')?.getAttribute('content'),
        header: document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content'),
    });

    // Data para funciones de logout
    Alpine.data('logoutFunctions', () => ({
        async logout() {
            const csrfToken = getCSRFToken();
            const csrfHeader = getCSRFHeaderName();
            const response = await fetch('/perform_logout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken,
                },
            });
            if (response.ok) {
                window.location.reload();
            } else {
                console.error('Error al cerrar sesión');
            }
        },
    }));
});