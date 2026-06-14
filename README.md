# NovaLearn 🎓

**NovaLearn** es una plataforma educativa móvil para Android diseñada para facilitar el aprendizaje continuo. La aplicación permite a los usuarios descubrir un catálogo de cursos, inscribirse en ellos y realizar un seguimiento de su progreso a través de lecciones estructuradas, todo bajo un entorno seguro y moderno.

## ✨ Características Principales

*   **Autenticación Profesional:** Implementación de **Auth0** para un inicio de sesión seguro y gestión de identidad (JWT).
*   **Exploración de Cursos:** Catálogo dinámico con imágenes (vía Glide) y descripciones detalladas.
*   **Gestión de Inscripciones:** Los usuarios pueden inscribirse en cursos y visualizarlos en su sección personalizada "Mis Cursos".
*   **Base de Datos Local:** Uso de **SQLite** para persistir la información de cursos, lecciones y el estado de inscripción del usuario.
*   **Navegación Intuitiva:** Flujo de usuario fluido mediante `BottomNavigationView` y `Jetpack Navigation Component`.
*   **Diseño Moderno:** Interfaz construida con **Material Design 3**, asegurando una experiencia visual limpia y responsiva.

## 🛠️ Stack Tecnológico

*   **Lenguaje:** Java / Kotlin.
*   **Arquitectura:** Modular orientada a UI con separación de datos.
*   **Librerías Clave:**
    *   `com.auth0.android:auth0`: Autenticación delegada.
    *   `androidx.navigation`: Gestión centralizada del flujo de la app.
    *   `com.github.bumptech.glide`: Carga y caché de imágenes.
    *   `de.hdodenhof:circleimageview`: Estética para fotos de perfil.
    *   `com.auth0.android:jwtdecode`: Manejo de tokens de sesión.

## 📁 Estructura del Proyecto (`com.proyecto.novalearn`)

*   **`data/`**: Modelos de negocio (`Curso`, `Leccion`, `Inscripcion`) y lógica de persistencia (`DBHelper`).
*   **`ui/auth/`**: Gestión de acceso e integración con Auth0 (`LoginActivity`).
*   **`ui/home/`**: Pantalla principal y adaptadores para el catálogo global.
*   **`ui/mycourses/`**: Lógica para gestionar los cursos en los que el usuario está inscrito.
*   **`ui/detail/`**: Detalle extendido de cursos y visualización de lecciones (`CourseDetailActivity`).
*   **`utils/`**: Utilidades de sistema como `SessionManager` para la persistencia de la sesión local.

## 🚀 Configuración y Requisitos

1.  **Requisitos:** Android Studio y API Mínima 24 (Android 7.0).
2.  **Configuración de Auth0:**
    *   Define las variables `auth0Domain` y `auth0Scheme` en tu `build.gradle`.
    *   El callback de redirección está configurado en el `AndroidManifest.xml` como:  
        `demo://${auth0Domain}/android/com.proyecto.novalearn/callback`
3.  **Base de Datos:** La aplicación inicializa automáticamente la base de datos local mediante `DBHelper` al primer arranque.

---
*Desarrollado como parte del proyecto NovaLearn.*
