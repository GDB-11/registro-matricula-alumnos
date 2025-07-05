# 📦 Estructura del Proyecto

Proyecto de gestión de matrícula y retiro de alumnos en Java.

---

## 🧱 Anatomía de las carpetas

| Carpeta (`src/main/java/...`)            | Rol en la arquitectura                                                                      | Clases clave que ya existen                                      |
|------------------------------------------|----------------------------------------------------------------------------------------------|-------------------------------------------------------------------|
| **application/core/interfaces**          | _Puertos de entrada_ → contratos que exponen los casos de uso.                              | `IAlumno`, `ICurso`, `IMatriculaService`, `IRetiroService`, etc. |
| **application/core/services**            | _Casos de uso_ → lógica de negocio delegada a los repositorios.                             | `AlumnoService`, `CursoService`, `MatriculaService`, etc.        |
| **infrastructure/core/models**           | _Dominio_ → entidades y value objects.                                                      | `Alumno`, `Curso`, `Matricula`, `Retiro`                         |
| **infrastructure/core/interfaces**       | _Puertos de salida_ → contratos de acceso a datos.                                          | `IAlumnoRepository`, `ICursoRepository`, etc.                    |
| **infrastructure/core/services**         | _Adaptadores de salida_ → implementación en memoria o persistencia real.                   | `AlumnoRepository`, `CursoRepository`, etc.                      |
| **presentation**                         | UI Swing modular. Cada subcarpeta representa un flujo o módulo funcional.                   | `MainWindow`, ventanas y modales                                 |
| **presentation/helper**                  | Utilidades de UI (factories, helpers visuales, validaciones).                              | `GridBagHelper`, `ValidationHelper`, etc.                        |
| **main**                                 | _Bootstrap y DI artesanal_ → inicializa servicios, base de datos y arranque de la app.      | `Program`, `ServiceContainer`, `WindowFactory`                   |
| **global**                               | Objetos utilitarios compartidos.                                                            | `Result`, `ConstantsHelper`, `DateHelper`                        |
| **test**                                 | (Vacío por ahora) — espacio reservado para pruebas unitarias.                               |                                                                 |

---

## 🔁 Carpetas que intervienen en cada flujo

### 🟢 Matrícula

- **Interfaces:**  
  `application/core/interfaces` → `IMatriculaService`

- **Servicios:**  
  `application/core/services` → `MatriculaService`

- **Repositorios:**  
  `infrastructure/core/interfaces` → `IMatriculaRepository`  
  `infrastructure/core/services` → `MatriculaRepository`

- **Presentación:**  
  `presentation/registro/matricula`  
  - Ventanas: `MatriculaWindow.java`  
  - Modales: `AddMatriculaModal.java`, `EditMatriculaModal.java`

- **Entidades:**  
  `Matricula`, `Alumno`, `Curso`


### 🔵 Retiro

- **Interfaces:**  
  `application/core/interfaces` → `IRetiroService`

- **Servicios:**  
  `application/core/services` → `RetiroService`

- **Repositorios:**  
  `infrastructure/core/interfaces` → `IRetiroRepository`  
  `infrastructure/core/services` → `RetiroRepository`

- **Presentación:**  
  `presentation/registro/retiro`  
  - Ventanas: `RetiroWindow.java`  
  - Modales: `AddRetiroModal.java`, `EditRetiroModal.java`

- **Entidades:**  
  `Retiro`, `Matricula`, `Alumno`


### 🟣 Consulta

- **Interfaces y servicios:**  
  `application/core/interfaces` → `IConsultaService`  
  `application/core/services` → `ConsultaService`  
  *Reutiliza los repos ya definidos de alumno, curso, matrícula y retiro*

- **Presentación:**  
  `presentation/consultas`  
  - Ventanas: `AlumnosCursosWindow.java`, `MatriculasRetirosWindow.java`  
  - Modales: `AlumnoInfoModal.java`, `CursoInfoModal.java`, `MatriculaInfoModal.java`, `RetirosInfoModal.java`

- **Entidades:**  
  `Alumno`, `Curso`, `Matricula`, `Retiro`


### 🟠 Mantenimiento

- **Presentación:**  
  `presentation/mantenimiento`  
  - Ventanas: `AlumnoWindow.java`, `CursoWindow.java`  
  - Modales: `AddAlumnoModal.java`, `EditAlumnoModal.java`, `AddCursoModal.java`, `EditCursoModal.java`

- **Entidades:**  
  `Alumno`, `Curso`


### 🟤 Reportes

- **Presentación:**  
  `presentation/reporte`  
  - Ventana: `ReportesWindow.java`

- **Estado actual:**  
  En proceso aún.


---

## ⚙️ Carpeta `main/`

Contiene la lógica de arranque y configuración central de la app.

- **`Program.java`**  
  Orquestador  de la aplicación. Inicia el contenedor, conecta la base de datos y lanza la interfaz principal.

- **`ServiceContainer.java`**  
  Contenedor propio de _Dependency Injection_.  
  Registra e instancia servicios (singleton o por instancia) usando reflexión.

- **`WindowFactory.java`**  
  Fábrica de ventanas Swing, con inyección manual de dependencias desde el contenedor.

- **`DatabaseManager.java`**  
  Se encarga de la conexión a la base de datos SQLite.  
  Métodos: `getConnection()`, `closeConnection()`, `initializeDatabase()`, `databaseExists()`

- **`DatabaseInitializer.java`**  
  Ejecuta la creación de tablas si la base aún no existe.  
  Se ejecuta al inicio de la app desde `Program`.

---

## 🌐 Global

Contiene utilitarios compartidos en todo el proyecto.

- `Result.java`: wrapper estándar para respuestas (éxito, error, datos).
- `ConstantsHelper.java`: constantes generales.
- `DateHelper.java`: utilidades para manipulación de fechas.

---

## 🧪 Test

Actualmente vacía. Se reserva para futuras pruebas unitarias.


