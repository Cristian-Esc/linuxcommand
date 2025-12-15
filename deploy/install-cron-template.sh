#!/bin/bash

# Variables predefinidas (serán reemplazadas por Gradle)
APP_NAME="{{APP_NAME}}"
ZIP_PATH="{{ZIP_PATH}}"
INSTALL_DIR="{{INSTALL_DIR}}"

# Confirmar inicio de la instalación
zenity --question --title="Instalación de $APP_NAME" \
    --text="Se instalará $APP_NAME en el sistema.\n¿Desea continuar?" \
    --width=300 || exit 0

# Seleccionar directorio de instalación
INSTALL_DIR=$(zenity --file-selection --directory \
    --title="Seleccione el directorio de instalación" \
    --filename="$INSTALL_DIR/" \
    --width=500) || {
    zenity --error --title="Error de instalación" --text="Debe seleccionar un directorio para continuar."
    exit 1
}

# Mostrar barra de progreso durante la instalación
(
    echo "10"; sleep 1
    echo "# Creando directorio de instalación..."; sleep 1
    sudo mkdir -p "$INSTALL_DIR/$APP_NAME" || {
        zenity --error --title="Error de instalación" --text="No se pudo crear el directorio de instalación."
        exit 1
    }
    sudo chmod -R 755 "$INSTALL_DIR/$APP_NAME"

    echo "40"; sleep 1
    echo "# Descomprimiendo el paquete ZIP..."; sleep 1
    sudo unzip -o "$ZIP_PATH" -d "$INSTALL_DIR/$APP_NAME" || {
        zenity --error --title="Error de instalación" --text="No se pudo descomprimir el paquete ZIP."
        exit 1
    }

    # Detectar el nombre del directorio descomprimido automáticamente
    BASE_DIR=$(find "$INSTALL_DIR/$APP_NAME" -mindepth 1 -maxdepth 1 -type d | head -n 1)
    if [ -z "$BASE_DIR" ]; then
        zenity --error --title="Error de instalación" --text="No se encontró un directorio válido dentro del archivo ZIP."
        exit 1
    fi

    echo "60"; sleep 1
    echo "# Moviendo archivos a la carpeta de destino..."; sleep 1
    sudo mv "$BASE_DIR"/* "$INSTALL_DIR/$APP_NAME" || {
        zenity --error --title="Error de instalación" --text="No se pudo mover los archivos al directorio de destino."
        exit 1
    }
    sudo rm -rf "$BASE_DIR" || {
        zenity --error --title="Error de instalación" --text="No se pudo eliminar el directorio temporal."
        exit 1
    }

    echo "80"; sleep 1
    echo "# Configurando la tarea en cron..."; sleep 1
    CRON_FILE="/etc/cron.d/$APP_NAME"
    echo "# Tarea para ejecutar el script al inicio y cada hora
@reboot root bash $INSTALL_DIR/$APP_NAME/scripts/startup-task.sh
0 * * * * root bash $INSTALL_DIR/$APP_NAME/scripts/startup-task.sh
" | sudo tee "$CRON_FILE" > /dev/null || {
        zenity --error --title="Error de instalación" --text="No se pudo crear la tarea en cron."
        exit 1
    }

    sudo chmod 644 "$CRON_FILE"
    sudo service cron reload || {
        zenity --error --title="Error de instalación" --text="No se pudo recargar el servicio cron."
        exit 1
    }

    echo "100"; sleep 1
    echo "# Finalizando instalación..."; sleep 1
) | zenity --progress --title="Instalación de $APP_NAME" --text="Instalando $APP_NAME..." \
    --percentage=0 --auto-close || {
    zenity --error --title="Instalación cancelada" --text="La instalación fue cancelada por el usuario."
    exit 1
}

# Confirmar instalación exitosa
zenity --info --title="Instalación completa" \
    --text="$APP_NAME se ha instalado correctamente.\nLas tareas automáticas han sido configuradas." \
    --width=300
