# Wintam

Aplicación Android para la organización y gestión de catas de vino entre usuarios. Permite crear eventos, inscribirse, confirmar asistencia y moderar la comunidad mediante un sistema de reportes y karma. 

---

## Descripción

Wintam es una red social temática orientada a los amantes del vino. Los usuarios pueden organizar catas y gestionar todo el ciclo del evento desde la app: desde la creación hasta la finalización, pasando por la confirmación de asistencia mediante un código único generado en el momento de inicio.

La aplicación incluye un sistema de reputación basado en karma que premia la participación y penaliza las cancelaciones tardías, así como un panel de administración para la gestión de reportes entre usuarios.

---

## Tecnologías utilizadas

**Backend**
- Java 21
- Spring Boot
- Spring Security con autenticación JWT
- Spring Data JPA + Hibernate
- MySQL (desplegado en Railway)

**Frontend**
- Kotlin
- Jetpack Compose
- Retrofit para las llamadas HTTP
- DataStore para persistencia local del token
- Artquitectura MVVM (Model, View, Viewmodel)

**Infraestructura**
- Railway (backend + base de datos)
- GitHub (control de versiones)

---

## Arquitectura

El proyecto sigue una arquitectura en capas tanto en backend como en el frontend.

**Backend**

```
controller/    Endpoints REST
service/       Lógica de negocio
repository/    Acceso a datos (Spring Data JPA)
model/         Entidades JPA
dto/           Objetos de transferencia de datos
exception/     Manejo centralizado de errores
security/      Filtro JWT y configuración de seguridad
```

**Frontend**

```
ui/screens/        Pantallas principales
ui/dialogs/        Diálogos reutilizables
ui/components/     Componentes de UI compartidos
viewmodel/         ViewModels por funcionalidad
data/repository/   Repositorios de datos
data/remote/dto/   DTOs de request y response
data/              TokenManager (gestión del JWT)
utils/             safeApiCall (manejo de errores HTTP)
```

---

## Funcionalidades

**Autenticación**
- Registro con verificación de email por código
- Login con JWT persistido en DataStore
- Recuperación y restablecimiento de contraseña por código

**Catas**
- Crear catas con título, tipo de vino, nivel de experiencia, ubicación, fecha, hora y aforo máximo
- Feed de catas disponibles (estados OPEN y ACTIVE)
- Búsqueda y filtrado por título, tipo de vino, nivel, ubicación y estado
- Detalle completo de cada cata
- Cancelar cata (solo el anfitrión — penalización de karma si faltan menos de 24 horas)
- Iniciar cata — genera un código de asistencia de 5 dígitos y cambia el estado a ACTIVE

**Inscripciones**
- Inscribirse en catas en estado OPEN
- Cancelar inscripción (penalización de karma si faltan menos de 24 horas)
- Confirmar asistencia introduciendo el código generado por el anfitrión (recompensa karma)
- Ver lista de asistentes con username y karma

**Sistema de karma**
- Se gana karma al confirmar asistencia
- Se pierde karma al cancelar tarde como asistente o como anfitrión
- El karma de cada usuario es visible en su perfil y en la lista de asistentes

**Reportes y moderación**
- Cualquier usuario puede reportar a otro desde el detalle de la cata
- Panel de administración exclusivo para el rol ADMIN
- El admin puede resolver reportes aplicando: aviso, penalización de karma o baneo
- El admin puede descartar reportes sin sanción
- Los usuarios baneados no pueden autenticarse en la aplicación

**Perfil**
- Ver y editar nombre, apellido y descripción
- Visualización de karma y username

---

## Modelos de datos

| Entidad | Campos principales |
|---|---|
| User | id, name, surname, username, email, passwordHash, birthdate, description, isVerified, role, karma, createdAt |
| Cata | id, title, wineType, experienceLevel, location, scheduleDate, scheduledTime, maxAttendees, status, attendanceCode, host, createdAt |
| Inscripcion | id, cata, player, status, createdAt |
| Report | id, reporter, reported, reason, sanctionType, status, createdAt |

**Enumerados**
- `CataStatus`: OPEN, FULL, ACTIVE, COMPLETED, CANCELLED
- `ExperienceLevel`: BEGINNER, INTERMEDIATE, EXPERT
- `InscripcionStatus`: CONFIRMED, CANCELLED, ATTENDED
- `ReportStatus`: PENDING, RESOLVED, DISMISSED
- `SanctionType`: WARNING, KARMA_PENALTY, BAN
- `Role`: USER, ADMIN, BANNED

---

## API REST

```
POST   /api/auth/register
POST   /api/auth/signIn
POST   /api/auth/verify
POST   /api/auth/recover-password
POST   /api/auth/reset-password

POST   /api/cata/createCata
GET    /api/cata/getAllCatas
GET    /api/cata/searchCata
PATCH  /api/cata/cancel/{id}
PATCH  /api/cata/startCata/{id}

POST   /api/inscripcion/joinCata/{id}
PATCH  /api/inscripcion/cancelCata/{id}
PATCH  /api/inscripcion/confirmAttendance
GET    /api/inscripcion/getAttendees/{cataId}

GET    /api/user/getProfile
PATCH  /api/user/updateProfile

POST   /api/report/reportUser
GET    /api/report/getReport
PATCH  /api/report/resolveReport
PATCH  /api/report/dismissReport/{id}
```

Los endpoints de autenticación son públicos. El resto requieren token JWT en la cabecera. Los endpoints de reporte (GET y PATCH) requieren rol ADMIN.

---

## Guía de instalación 

### Requisitos previos

- Java 21 o superior
- Android Studio
- IntelliJ IDEA
- MySQL 8+ (local) o cuenta en Railway
- Git

---

### Backend 

**1. Clonar el repositorio**

```bash
https://github.com/garciaespanaalejandro-coder/wintam.git
```
Acceder a la carpeta backend.

**2. Configurar las variables de entorno**

El backend utiliza un archivo .env para mantener fuera del repositorio las credenciales sensibles. Renombra el archivo .env.pruebas a .env e introduce tus credenciales.

```env
URL_DATABASE=
DB_USERNAME=
DB_PASSWORD=
JWT_SECRET=
MAIL_USERNAME=
MAIL_PASSWORD=
```

**3. Crear la base de datos**

```sql
CREATE DATABASE wintam;
```

Spring Boot con Hibernate creará las tablas automáticamente al arrancar gracias a "ddl-auto: update" en la configuración.

**4. Inicia Docker**

```docker
docker compose up -d  
```

**5. Inicia el proyecto**

Desde el IDE inicia el proyecto.

### Frontend (App Android)

**1. Clonar el repositorio**

```bash
https://github.com/garciaespanaalejandro-coder/wintam.git
```
Acceder a la carpeta frontend.


**2. Configurar la URL del backend**

En el archivo RetrofitClient.kt, cambia la URL por la de tu backend:


```kotlin
// Para desarrollo local (usar IP local)
private const val BASE_URL = "http://192.168.x.x:8080/"

// Para producción en Railway
private const val BASE_URL = "https://wintam-production.up.railway.app/"
```

**3. Abrir en Android Studio**

Abre el proyecto en Android Studio, espera a que sincronice el Gradle y ejecuta la app en un emulador o dispositivo físico con Android 8.0 (API 26) o superior.

---

### Despliegue en producción (Railway)

El proyecto está pensado para desplegarse en Railway, **ATENCIÓN: PROCESO NO GRATUITO** .

1. Crea una cuenta en [railway.app](https://railway.app)
2. Crea un nuevo proyecto y clona el repositorio
3. Añade un segundo servicio MySQL conectado al repositorio del backend
4. En las variables del servicio backend, añade todas las variables del .env descritas arriba
5. Iniciar despliegue

---

## Autor

Alejandro Domingo García España - DAM (Desarrollo de Aplicaciones Multiplataforma)
Proyecto de Fin de Ciclo - 2026
