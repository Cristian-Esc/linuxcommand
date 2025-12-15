#!/bin/bash

# Variables predefinidas (serán reemplazadas por Gradle)
APP_NAME="{{APP_NAME}}"
INSTALL_DIR="{{INSTALL_DIR}}"

# Confirmar inicio de la desinstalación
zenity --question --title="Desinstalación de $APP_NAME" \
    --text="Se desinstalará $APP_NAME del sistema.\n¿Desea continuar?" \
    --width=300 || exit 0

# Detener y deshabilitar el servicio systemd
echo "# Deteniendo el servicio..."; sleep 1
sudo systemctl stop "$APP_NAME" || {
    zenity --error --title="Error de desinstalación" --text="No se pudo detener el servicio. Verifique el log del sistema."
    exit 1
}

echo "# Deshabilitando el servicio..."; sleep 1
sudo systemctl disable "$APP_NAME" || {
    zenity --error --title="Error de desinstalación" --text="No se pudo deshabilitar el servicio."
    exit 1
}

# Eliminar el archivo de servicio systemd
echo "# Eliminando archivo de servicio systemd..."; sleep 1
sudo rm -f "/etc/systemd/system/$APP_NAME.service" || {
    zenity --error --title="Error de desinstalación" --text="No se pudo eliminar el archivo de servicio."
    exit 1
}

# Eliminar los archivos de la aplicación
echo "# Eliminando archivos de la aplicación..."; sleep 1
sudo rm -rf "$INSTALL_DIR/$APP_NAME" || {
    zenity --error --title="Error de desinstalación" --text="No se pudieron eliminar los archivos de la aplicación."
    exit 1
}

# Eliminar el acceso directo en el menú
echo "# Eliminando acceso directo en el menú..."; sleep 1
DESKTOP_FILE="/usr/share/applications/$APP_NAME.desktop"
if [ -f "$DESKTOP_FILE" ]; then
    rm -f "$DESKTOP_FILE" || {
        zenity --error --title="Error de desinstalación" --text="No se pudo eliminar el archivo .desktop."
        exit 1
    }
else
    echo "# No se encontró el archivo .desktop. Saltando eliminación..."
fi

# Reiniciar systemd
echo "# Recargando configuración de systemd..."; sleep 1
sudo systemctl daemon-reload || {
    zenity --error --title="Error de desinstalación" --text="No se pudo recargar systemd."
    exit 1
}

# Confirmar desinstalación exitosa
zenity --info --title="Desinstalación completa" \
    --text="$APP_NAME se ha desinstalado correctamente." \
    --width=300
