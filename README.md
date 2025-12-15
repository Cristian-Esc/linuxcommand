<<<<<<< HEAD
# Linux Commands API Application

---
=======
<<<<<<< HEAD
# Linux Commands CRUD Application
=======
# Linux Commands API Application

---
>>>>>>> 1138c29 (init repository)
>>>>>>> d4e1e4c (Guardar cambios antes de sincronizar)

Este proyecto es una aplicación web basada en Spring Boot que permite gestionar comandos de Linux de manera fácil y rápida. Puedes agregar, editar, eliminar y ejecutar comandos Linux desde una interfaz web, además de obtener un código QR para cada comando. Además, ofrece una API REST para interactuar con los comandos programáticamente.
## Características

- Gestión de comandos Linux (Crear, Leer, Actualizar, Eliminar)
- Interfaz web interactiva utilizando **Thymeleaf** y **Bootstrap**.
- Generación de códigos QR para cada comando.
- Permite ejecutar los comandos a través de enlaces generados dinámicamente.
- Opciones para activar o desactivar comandos.
- **API REST** para gestionar y ejecutar comandos.

## Tecnologías Utilizadas

- **Backend**:
    
    - Java 21
        
    - Spring Boot
        
    - Spring MVC
        
    - Spring Data JPA
        
- **Frontend**:
    
    - HTML5, CSS3
        
    - Bootstrap 5.3.3
        
    - Alpine.js
        
- **Base de Datos**:
    
    - H2 (Por defecto, se puede configurar con otras bases de datos como MySQL o PostgreSQL)
        
- **Utilidades**:
    
    - Librería para generar códigos QR.

## Instalación

### Requisitos Previos

Antes de comenzar, asegúrate de tener instalados los siguientes programas en tu sistema:

- **Java 21** o superior (si no lo tienes, [descárgalo desde aquí](https://adoptopenjdk.net/))
- **Gradle** (si no lo tienes, [descárgalo desde aquí](https://gradle.org/install/))

### Clonación del Repositorio

1. Clona el repositorio a tu máquina local:
    
    ```bash
    git clone https://github.com/Cristian-Esc/linuxcommand.git
<<<<<<< HEAD
    cd linux-commands-crud
=======
<<<<<<< HEAD
    cd linuxcommand
=======
    cd linux-commands-crud
>>>>>>> 1138c29 (init repository)
>>>>>>> d4e1e4c (Guardar cambios antes de sincronizar)
    ```
    

### Configuración y Dependencias

2. Si ya tienes **Gradle** instalado, puedes ejecutar el siguiente comando para compilar e instalar las dependencias del proyecto:
    
    ```bash
    gradle build
    ```
    

### Ejecución de la Aplicación

1. Ejecuta la aplicación Spring Boot con Gradle:
    
    ```bash
    gradle bootRun
    ```
    
2. Accede a la aplicación en tu navegador desde la siguiente URL:
    
    ```
    http://localhost:10010
    ```
    


La aplicación estará disponible en **[http://localhost:10010](http://localhost:10010)**.

### Configuración de Inicio Automático (Opcional)

Si deseas que la aplicación se inicie automáticamente al iniciar sesión en un entorno gráfico, puedes seguir los siguientes pasos:

1. Crea el directorio necesario si no existe:
    
    ```
    mkdir -p ~/.config/autostart
    ```
    
2. Edita o crea el archivo de configuración del inicio automático:
    
    ```
    nano ~/.config/autostart/linuxCommands.desktop
    ```
    
3. Añade el siguiente contenido al archivo:
    
    ```
    [Desktop Entry]
    Type=Application
    Exec=/ruta/a/tu/script/de/inicio.sh
    X-GNOME-Autostart-enabled=true
    Name=Linux Commands CRUD
    Comment=Aplicación CRUD de comandos de Linux
    ```
    
4. Guarda y cierra el archivo. Ahora la aplicación se iniciará automáticamente al iniciar sesión.
    

#### Ventajas del Método de Configuración Automática

- **Compatibilidad:** No necesitas preocuparte por variables como `DISPLAY` o `XAUTHORITY`, ya que el entorno gráfico las configura automáticamente.
    
- **Facilidad de Uso:** Funciona en la mayoría de entornos gráficos como GNOME, KDE, XFCE, etc.
    

Con esto, tu aplicación se iniciará automáticamente cada vez que inicies sesión en tu entorno gráfico.
---

## API REST

Este proyecto también ofrece una **API REST** para interactuar con los comandos Linux. Aquí están los **endpoints** disponibles:

### CRUD Comandos

|Método|Endpoint|Descripción|
|---|---|---|
|GET|`/api/commands`|Obtener todos los comandos.|
|POST|`/api/commands`|Crear un nuevo comando.|
|PUT|`/api/commands/{id}`|Editar un comando existente.|
|DELETE|`/api/commands/{id}`|Eliminar un comando.|

### Otras Funcionalidades

|        |                               |                                     |
| ------ | ----------------------------- | ----------------------------------- |
| Método | Endpoint                      | Descripción                         |
| GET    | `/api/commands/execute/{url}` | Ejecutar un comando por su URL.     |
| GET    | `/api/commands/{id}/qr`       | Obtener el código QR de un comando. |

### 1. **Obtener todos los comandos**

**GET** `/api/commands`

Obtiene una lista de todos los comandos Linux.

**Respuesta:**

```json
[
    {
        "id": 1,
        "title": "Titulo del comando 1",
        "description": "Descripción del comando 1",
        "command": "firefox",
        "url": "comando1",
        "active": true
    },
    ...
]
```

### 2. **Crear un nuevo comando**

**POST** `/api/commands`

Crea un nuevo comando Linux. Debes proporcionar el cuerpo de la solicitud con los datos del comando.

**Cuerpo de la solicitud:**

```json
{
  "title": "Comando nuevo",
  "description": "Descripción del comando nuevo",
  "command": "firefox",
  "url": "comando-nuevo",
  "active": true
}
```

**Respuesta:**

```json
{
  "id": 1002,
  "title": "Comando nuevo",
  "description": "Descripción del comando nuevo",
  "command": "firefox",
  "active": true,
  "url": "comando-nuevo"
}
```

### 3. **Editar un comando existente**

**PUT** `/api/commands/{id}`

Edita un comando existente. Debes proporcionar el ID del comando y el nuevo cuerpo de la solicitud.

**Cuerpo de la solicitud:**

```json
{
	"id": 1003,
	"title": "Comando nuevo",
	"description": "Descripción editada",
	"command": "kill 1",
    "active": false,
    "url": "comando-editado",
}
```

**Respuesta:**

```json
{
    "message": "Command updated successfully.",
    "data": {
        "id": 1003,
        "title": "Comando nuevo",
		"description": "Descripción editada",
		"command": "kill 1",
	    "active": false,
	    "url": "comando-editado",
    }
}
```

### 4. **Eliminar un comando**

**DELETE** `/api/commands/{id}`

Elimina un comando Linux existente por su ID.

**Respuesta:**

- `200 OK` si la eliminación fue exitosa.
- `404 Not Found` si el comando no existe.

### 5. **Ejecutar un comando**

**GET** `/api/commands/execute/{url}`

Ejecuta un comando Linux a través de su URL. Devuelve la salida del comando ejecutado.

**Respuesta:**

```text
✅ Comando ejecutado exitosamente.
```

**Error:**

```text
❌ Error al ejecutar el comando: [mensaje de error]
```

### 6. **Obtener el código QR de un comando**

**GET** `/api/commands/{id}/qr`

Obtiene un código QR para ejecutar un comando. El código QR contendrá una URL para ejecutar el comando.

**Respuesta:**

- Imagen en formato PNG con el código QR.

---

## Cómo Usar

1. Al iniciar la aplicación, verás una tabla con los comandos de Linux.
2. Puedes crear un nuevo comando, editar uno existente o eliminarlo.
3. Para ejecutar un comando, haz clic en el enlace correspondiente en la columna de **Atajo (URL)**.
4. Si deseas ver el código QR del comando, haz clic en el botón "QR".
5. Activa o desactiva un comando marcando la casilla correspondiente.

---



## Cómo Donar

Si deseas apoyar este proyecto y su mantenimiento, puedes hacerlo mediante donaciones en criptomonedas. A continuación te dejo las direcciones para donar:

<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
>>>>>>> d4e1e4c (Guardar cambios antes de sincronizar)
### Bitcoin (BTC)

```
bc1qkpswywdqjht6zpfm694t2m6ewww3ukw25vlemu
````

<<<<<<< HEAD
=======
>>>>>>> 1138c29 (init repository)
>>>>>>> d4e1e4c (Guardar cambios antes de sincronizar)
### Osmosis (OSMO)

```
osmo1gcqmcc8xa4lgxflwfm4fcg2nh722dgmel9l54m
```

### Cosmos (ATOM)

```
cosmos1gcqmcc8xa4lgxflwfm4fcg2nh722dgmeh7vyrf
```

Gracias por tu apoyo, ¡todas las donaciones son bienvenidas!

---

## Autor

Este proyecto fue creado por **Cristian Escobedo**. Puedes ver más sobre mi trabajo en [GitHub](https://github.com/Cristian-Esc).

---


### MIT License

- ✅ Uso personal y comercial permitido
- ✅ Modificación y adaptación permitidas
- ✅ Distribución permitida
- ❌ Sin garantía de ningún tipo
- ❌ El autor no se hace responsable por daños derivados del uso

Esto permite utilizar **Linux Commands API** como:

- Herramienta interna
- Base para proyectos comerciales
- Producto personalizado para clientes

Siempre que se conserve el aviso de copyright y la licencia MIT original.



<<<<<<< HEAD
¡Gracias por usar **Linux Commands API**!
=======
<<<<<<< HEAD
¡Gracias por usar **Linux Commands CRUD**!
=======
¡Gracias por usar **Linux Commands API**!
>>>>>>> 1138c29 (init repository)
>>>>>>> d4e1e4c (Guardar cambios antes de sincronizar)
