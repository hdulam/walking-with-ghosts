# Walking with Ghosts (Ghost GO: Paranormal Hunt)

Demo académica de **permisos en tiempo de ejecución** y **Firebase Crashlytics** con arquitectura **MVVM**.
El jugador usa su ubicación aproximada para detectar fantasmas ficticios, los captura y los guarda en su GhostDex.

Integrantes: Juan Carlos Espinal · Hector Eduardo Duarte · Jonathan Jorge Cofiño

## Tecnologías (versiones reales del proyecto)
| Pieza | Versión | Fuente |
|---|---|---|
| Android Gradle Plugin | 9.3.3 | `gradle/libs.versions.toml` (plantilla de Android Studio) |
| Gradle | 9.5.0 | `gradle/wrapper/gradle-wrapper.properties` |
| Kotlin (plugin Compose) | 2.2.10 | plantilla |
| Compose BOM | 2026.02.01 | plantilla |
| lifecycle (viewmodel/runtime-compose) | 2.11.0 | Google Maven (última estable) |
| navigation-compose | 2.10.2 | Google Maven |
| datastore-preferences | 1.3.0 | Google Maven |
| play-services-location | 21.4.0 | Google Maven |
| Firebase BoM / firebase-crashlytics | 35.1.0 | firebase.google.com/docs/crashlytics/android/get-started |
| google-services / crashlytics Gradle plugins | 4.5.0 / 3.0.8 | misma guía |

`compileSdk 37.1`, `targetSdk 36`, `minSdk 26`.

## Arquitectura
```
domain/model      Ghost, Rarity, GhostCatalog
data/location     LocationDataSource + FusedLocationDataSource (API real de ubicación)
data/local        GhostDexStore (DataStore)
data/repository   GhostRepository + DefaultGhostRepository, GhostSpawner, GhostSearchException
permissions       rememberPermissionRequester (genérico), PermissionResult, PermissionDenialClassifier
crash             CrashReporter (interfaz), FirebaseCrashReporter, LogcatCrashReporter
ui/*              Screens (Compose) + ViewModels (StateFlow) + UiState
AppContainer      DI manual
```
UI → ViewModel (StateFlow<UiState>) → Repository → fuentes de datos. Los ViewModels no guardan `Context`.

**Persistencia:** DataStore Preferences con un `Set<String>` de ids capturados. Es suficiente para 6 fantasmas; Room sería excesivo.

## Permiso
`ACCESS_COARSE_LOCATION` (permiso de tiempo de ejecución). Se solicita solo cuando el usuario toca **Buscar fantasmas**.
El componente sigue el flujo oficial: comprobar → explicación (si Android la recomienda) → diálogo del sistema → resultado.
El rechazo permanente se infiere combinando `shouldShowRequestPermissionRationale` antes y después de la solicitud
con un historial persistente de rechazos (Android no ofrece una API directa). Limitación: si se borran los datos de la app,
el historial se pierde y el primer rechazo se clasifica como rechazo simple.

## Configurar Firebase
Estado actual: la app está registrada en el proyecto **`walking-with-ghosts-2026`** (creado con Firebase CLI,
App ID `1:356561986080:android:2d264b3e6d712dfe29d245`), y `app/google-services.json` se descargó con `firebase apps:sdkconfig`.
⚠️ La hoja pide registrarla como app adicional en el proyecto **del equipo**; este proyecto es nuevo porque esa cuenta no tenía acceso al del equipo.

Para registrarla en otro proyecto:
1. En Firebase Console, abrir el proyecto del equipo → *Agregar app* → Android.
2. Nombre del paquete: `com.hdulam.walkingwithghosts`.
3. Descargar `google-services.json` y copiarlo en `app/google-services.json`.
4. Compilar: el aviso "google-services.json not found" debe desaparecer.
5. Ejecutar la app, cerrarla y volver a abrirla. El panel de Crashlytics debe salir de la pantalla de "esperando datos".

Sin ese archivo, la app compila y usa `LogcatCrashReporter`, que **no envía nada a Firebase**.

## Compilar y probar
```bash
./gradlew assembleDebug testDebugUnitTest
```

## Limitaciones conocidas
- La compatibilidad de los plugins de Firebase con AGP 9.3.3 no está documentada; se comprobará al agregar `google-services.json`.
- En el emulador hay que fijar una ubicación (Extended controls → Location) para que haya una ubicación disponible.
- El botón "Fantasma destacado (demo)" solo aparece en compilaciones debug y existe para la actividad de Crashlytics.
