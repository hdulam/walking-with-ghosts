# Matriz de requisitos y pruebas

Estados: IMPLEMENTADO · COMPILADO · PROBADO · VERIFICADO MANUALMENTE · PENDIENTE · BLOQUEADO

## Requisitos de la hoja
| Requisito | Implementación | Archivo / clase | Prueba | Estado |
|---|---|---|---|---|
| App nueva registrada en Firebase | Proyecto nuevo `walking-with-ghosts-2026` vía CLI (no es el del equipo) | `app/google-services.json` | P13 | COMPILADO; comunicación PROBADA el 27/10: el SDK 21.0.1 inicializa y el servidor responde `Loaded settings` con `status: activated` |
| ≤ 8 reglas + lectura | 8 reglas | `CLAUDE.md`, `docs/DOCUMENTACION_ACADEMICA.md` | revisión | IMPLEMENTADO (citas por confirmar textualmente) |
| Consulta usada | Resumen derivado de la real | `docs/DOCUMENTACION_ACADEMICA.md` | — | IMPLEMENTADO |
| MVVM | Screen + ViewModel + StateFlow + Repository | `ui/*`, `data/repository/*` | auditoría regla 3 | COMPILADO |
| Componente genérico de permisos | `rememberPermissionRequester(permission, onResult)` | `permissions/PermissionRequester.kt` | P02–P05 | COMPILADO; lógica PROBADA (unitaria) |
| Interfaz que aísla Crashlytics | `CrashReporter` / `FirebaseCrashReporter` | `crash/*` | grep regla 6 | COMPILADO |
| Permiso de tiempo de ejecución | `ACCESS_COARSE_LOCATION` | `AndroidManifest.xml`, `RadarScreen` | P02 | COMPILADO |
| Diagrama con 3 casos y ❌ | Mermaid | `docs/DOCUMENTACION_ACADEMICA.md` §2.1 | revisión con el código | IMPLEMENTADO |
| Auditoría y regla reescrita | Reglas 2, 4 y 5 | §2.2–2.3 | — | IMPLEMENTADO |
| Error fatal a propósito | Acción de demostración (solo debug) | — (secreto) | P12–P13 | PROBADO (cierre reproducido); reporte en la consola PENDIENTE |
| Diagnóstico desde Crashlytics | Plantilla | §3 | P13 | PENDIENTE |
| Corrección + no fatal + claves + logs | try/catch en el Repository → `CrashReporter.recordException` → `Result.failure` → estado seguro | `DefaultGhostRepository.getFeaturedGhost`, `GhostDexViewModel` | P14–P17 | COMPILADO; verificación en el dispositivo y la consola PENDIENTE |
| Diagrama de la corrección en otro color | — | §3 | — | PENDIENTE |
| Flujo del equipo + regressed | 6 pasos con responsables | §3 | — | IMPLEMENTADO (asignaciones propuestas) |
| Demo ≤ 5 min | Guion | §4 | ensayo | PENDIENTE (ensayo) |

## Matriz de pruebas
| ID | Objetivo | Pasos clave | Resultado esperado | Obtenido | Estado |
|---|---|---|---|---|---|
| P01 | Inicio | Abrir la app | Radar sin pedir permiso | 27/10: `am start` Status ok (COLD), proceso vivo, sin entradas en el buffer de crash; GhostDex muestra «Capturados: 0 / 6» | PROBADO (emulador API 37) |
| P02 | Concedido | Buscar → Permitir | Lista de fantasmas | — | PENDIENTE |
| P03 | Rechazado | Buscar → Rechazar | Explicación + reintento | — | PENDIENTE |
| P04 | Rechazo permanente | Rechazar 2 veces → Buscar | Texto + botón Abrir Ajustes | — | PENDIENTE |
| P05 | Regreso desde Ajustes | Conceder en Ajustes → volver | Estado vuelve a Idle | — | PENDIENTE |
| P06 | Ubicación desactivada | Apagar ubicación → Buscar | Mensaje "desactivada" | — | PENDIENTE |
| P07 | Ubicación no disponible | Emulador sin ubicación | Mensaje tras ≤ 15 s, sin bucle | — | PENDIENTE |
| P08 | Búsqueda | Misma zona dos veces en 10 min | Mismos fantasmas | Determinismo probado (`GhostSpawnerTest`) | PROBADO (unitaria) |
| P09 | Captura exitosa | Capturar hasta lograrlo | Mensaje + aparece en GhostDex | — | PENDIENTE |
| P10 | Captura fallida | Capturar uno legendario | "escapó" + reintento | — | PENDIENTE |
| P11 | Persistencia | Capturar → cerrar → abrir | Sigue en GhostDex | — | PENDIENTE |
| P12 | Error fatal | Acción de demostración | La app se cierra | 27/10: la app se cerró con FATAL EXCEPTION; Crashlytics registró «Handling uncaught exception» | PROBADO |
| P13 | Reporte fatal | Reabrir → consola | Issue en Crashlytics | 27/10: «report successfully enqueued to DataTransport»; al reabrir, upload con `Status Code: 200`. Falta verlo en la consola | PENDIENTE (verificar en la consola) |
| P14 | Corrección | Repetir P12 | No se cierra | 29/10: con GhostDex vacía, mismo PID antes y después, sin entradas en el buffer de crash; la UI muestra «Aún no tienes fantasmas capturados para destacar» | PROBADO |
| P15 | No fatal | Consola | Evento no fatal | 29/10: log «log non-fatal event to persistence»; envío posterior con Status 200. Falta verlo en la consola | PENDIENTE (consola) |
| P16 | Claves | Detalle del issue | screen_name, operation, ghost_count | — | PENDIENTE |
| P17 | Logs | Detalle del issue | Logs de la operación | — | PENDIENTE |
| P18 | Recreación | Rotar durante la búsqueda / el diálogo | Se conserva el estado | — | PENDIENTE |
| P19 | Sin conexión | Modo avión → Buscar | Error comprensible; el reporte se envía después | — | PENDIENTE |
| P20 | Compilación limpia | `gradlew assembleDebug testDebugUnitTest` | BUILD SUCCESSFUL, 0 advertencias de Kotlin | BUILD SUCCESSFUL, 8/8 pruebas | PROBADO |
