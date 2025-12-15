#!/bin/bash

# Variables predefinidas (serán reemplazadas por Gradle)
APP_NAME="{{APP_NAME}}"
ZIP_PATH="{{ZIP_PATH}}"
INSTALL_DIR="{{INSTALL_DIR}}"

# Confirmar inicio de la instalación
zenity --question --title="Instalación de $APP_NAME" \
    --text="Se instalará $APP_NAME en el sistema.\n¿Desea continuar?" \
    --width=300 || exit 0

# Seleccionar directorio de instalación (opcional, predefinido como /opt)
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
    sudo mkdir -p "$INSTALL_DIR/$APP_NAME" || exit 1
    sudo chmod -R 755 "$INSTALL_DIR/$APP_NAME"

    echo "40"; sleep 1
    echo "# Descomprimiendo el paquete ZIP..."; sleep 1
    sudo unzip -o "$ZIP_PATH" -d "$INSTALL_DIR/$APP_NAME" || exit 1

    # Detectar el nombre del directorio descomprimido automáticamente
    BASE_DIR=$(find "$INSTALL_DIR/$APP_NAME" -mindepth 1 -maxdepth 1 -type d | head -n 1)
    if [ -z "$BASE_DIR" ]; then
        zenity --error --title="Error al encontrar el directorio descomprimido" --text="No se encontró un directorio dentro del archivo ZIP."
        exit 1
    fi

    echo "50"; sleep 1
    echo "# Moviendo archivos a la carpeta de destino..."; sleep 1
    sudo mv "$BASE_DIR"/* "$INSTALL_DIR/$APP_NAME" || exit 1
    sudo rm -rf "$BASE_DIR" || exit 1

    echo "70"; sleep 1
    echo "# Configurando el servicio systemd..."; sleep 1
    SERVICE_FILE="/etc/systemd/system/$APP_NAME.service"
    echo "[Unit]
Description=$APP_NAME Service
After=network.target

[Service]
Type=simple
ExecStart=$INSTALL_DIR/$APP_NAME/bin/$APP_NAME
Restart=always
User=$(whoami)

[Install]
WantedBy=multi-user.target
" | sudo tee "$SERVICE_FILE" >/dev/null || exit 1

    echo "90"; sleep 1
    echo "# Creando el acceso directo en el menú..."; sleep 1
    DESKTOP_FILE="/usr/share/applications/$APP_NAME.desktop"
    echo "[Desktop Entry]
Type=Application
Name=$APP_NAME
Exec=xdg-open http://localhost:8080
Terminal=false
" > "$DESKTOP_FILE" || {
        zenity --error --title="Error de instalación" --text="No se pudo crear el archivo .desktop."
        exit 1
    }

    echo "100"; sleep 1
    echo "# Finalizando instalación..."; sleep 1
) | zenity --progress --title="Instalación de $APP_NAME" --text="Instalando $APP_NAME..." \
    --percentage=0 --auto-close || {
    zenity --error --title="Instalación cancelada" --text="La instalación fue cancelada por el usuario."
    exit 1
}

# Reiniciar systemd y habilitar el servicio
sudo systemctl daemon-reload
sudo systemctl enable "$APP_NAME"
sudo systemctl start "$APP_NAME" || {
    zenity --error --title="Error de instalación" --text="No se pudo iniciar el servicio. Verifique el log del sistema."
    exit 1
}

# Confirmar instalación exitosa
zenity --info --title="Instalación completa" \
    --text="$APP_NAME se ha instalado correctamente.\nAcceda desde su navegador: http://localhost:8080" \
    --width=300
