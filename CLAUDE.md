# Reglas permanentes – Walking with Ghosts
1. Nombres de clases, funciones y variables en inglés: PascalCase para tipos, camelCase para funciones y propiedades, UPPER_SNAKE_CASE para constantes; `val` por defecto y sin `!!`.
2. Un tipo público principal por archivo, con el mismo nombre; paquetes por capa: ui.<pantalla>, data.*, domain.model, permissions, crash.
3. MVVM: cada pantalla tiene un ViewModel que expone un único StateFlow<UiState> inmutable; la UI solo renderiza y envía eventos; el ViewModel no guarda Activity, Context ni View.
4. Los datos del juego (ubicación, colección y generación de fantasmas) solo se acceden desde un Repository detrás de una interfaz, recibido por constructor (DI manual en AppContainer); un componente genérico puede guardar su propio estado interno.
5. Todo permiso peligroso se solicita solo a través del componente genérico de permissions/, que recibe el permiso como parámetro y sigue el flujo oficial: devuelve Granted, ShowRationale, Denied o PermanentlyDenied.
6. Solo FirebaseCrashReporter importa Firebase Crashlytics; el resto depende de la interfaz CrashReporter. Nunca enviar coordenadas ni datos personales.
7. Nada de catch vacíos: los errores de datos se capturan en el Repository, se registran con CrashReporter y se devuelven como Result para mostrar un estado seguro.
8. Pedir los cambios por pasos y verificar cada uno compilando (`gradlew assembleDebug`) y con pruebas antes de continuar; no aceptar versiones de dependencias sin fuente oficial.
