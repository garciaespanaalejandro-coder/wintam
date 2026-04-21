# Wintam
Proyecto fin de DAM, consiste en el desarrollo de una aplicación móvil diseñada  para solucionar la dificultad de encontrar catas de vino cercanas. La aplicación  permite a los usuarios actuar como anfitriones, publicando con detalles específicos como  nivel de experiencia requerido y la ubicación física de la cata.  
# Guía de Despliegue y Ejecución

Esta guía detalla los pasos necesarios para levantar el entorno de desarrollo de Wintam, compuesto por una API (Backend) y una aplicación móvil (Frontend- pendiente de desarrollo).

## 1. Requisitos Previos
* **Entornos de desarrollo:** IntelliJ IDEA (Backend) y Android Studio (Frontend).
* **Base de datos:** MySQL en ejecución.
* **Hardware recomendado:** PC con al menos 16GB de RAM y un dispositivo físico Android para pruebas reales.

## 2. Instalación Local

### Clonar el proyecto
Abre tu terminal y ejecuta:
```bash
https://github.com/garciaespanaalejandro-coder/wintam.git
```
Arrancar el Backend:
  1.  Abrir IntelliJ, selecciona File>Open... y elige la carpeta backend.
  2.  Espera a que se descarguen las dependencias del proyecto.
  3.  Ejecuta la clse principal para levantar el servidor localmente.

Dependencias necesarias:
  1.  JWT
  2.  Spring Security
  3.  Hibernate + JPA
  4.  Driver MySQL
  5.  SpringBoot Web
  6.  Loombok
  7.  Validation
  8.  Spring Mail

## 3. Pruebas y despliegue:
* **Testing**: Se recomienda usar Postman para probar los endpoints localmente. (Pendientes de desarrollo)
