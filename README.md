# ClaseSmart

Aplicación educativa Android para niños de 8 a 12 años sobre organización,
convivencia, planificación y colaboración escolar. La pantalla principal
**es un aula interactiva**, no un menú ni un panel de control: se navega
tocando la pizarra, el reloj, el estante, los pupitres, la biblioteca, la
mochila o la puerta.

- **Package:** `com.educalab.clasesmart`
- **Versión:** 1.0.0
- **Stack:** Kotlin · Jetpack Compose · Material 3 (solo como base técnica) · Room · Coroutines/Flow · Navigation Compose · MVVM
- **minSdk:** 24 · **compileSdk/targetSdk:** 34 · **JDK:** 17 · **Kotlin:** 1.9.24 · **AGP:** 8.5.2

## Antes de compilar: genera el Gradle Wrapper

Este proyecto **no incluye el binario `gradle/wrapper/gradle-wrapper.jar`**
a propósito. Descargar un `.jar` compilado desde un repositorio de GitHub
arbitrario es un vector de ataque real (supply-chain), así que se omitió
deliberadamente.

- Si vas a compilar **localmente**: genéralo tú mismo con un Gradle de
  confianza (`gradle wrapper --gradle-version 8.7`), o simplemente abre
  la carpeta en **Android Studio** (Hedgehog o superior), que lo ofrece
  generar automáticamente al sincronizar.
- Si vas a compilar con **GitHub Actions** (ver "Opción B" más abajo):
  no tienes que hacer nada, el workflow lo genera solo en cada
  ejecución.

## Compilar

### Opción A: en tu máquina

```bash
./gradlew clean
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew assembleDebug
```

El APK resultante queda en `app/build/outputs/apk/debug/app-debug.apk`.

### Opción B: dejar que GitHub Actions lo compile por ti

Este repositorio incluye [`.github/workflows/android-build.yml`](.github/workflows/android-build.yml).
Al hacer `git push` a `main`/`master` (o al lanzarlo a mano desde la
pestaña **Actions → Android CI — Build APK → Run workflow**), el
workflow:

1. Instala JDK 17 y provisiona Gradle 8.7 de forma oficial (vía
   `gradle/actions/setup-gradle`, que descarga Gradle directamente de
   `services.gradle.org` — nunca un `.jar` de terceros).
2. Genera `gradle-wrapper.jar` con `gradle wrapper --gradle-version 8.7`
   (el motivo de que no venga incluido en el repo se explica más arriba,
   en "Antes de compilar").
3. Ejecuta `clean`, `testDebugUnitTest`, `lintDebug` y `assembleDebug`.
4. Sube el APK de depuración, el informe de tests y el informe de lint
   como artefactos descargables de la ejecución (pestaña **Actions** →
   la ejecución correspondiente → **Artifacts**), y escribe el SHA-256
   del APK en el resumen de la ejecución.

Los runners `ubuntu-latest` de GitHub ya traen el Android SDK
preinstalado, así que no hace falta ningún paso adicional de
`setup-android` para `compileSdk 34`.

> **Nota de transparencia:** este workflow se ha escrito y se ha
> validado su sintaxis YAML, pero no se ha podido ejecutar de verdad en
> este entorno de construcción (no hay forma de hacer `git push` a un
> repositorio real desde aquí). La primera vez que lo uses, revisa el
> log de la ejecución en la pestaña Actions por si tu cuenta necesita
> aceptar alguna licencia del SDK que el runner no tuviera ya aceptada.


> **Nota sobre este entregable v1.0.0:** el código se escribió y se revisó
> en un entorno sin SDK de Android ni acceso a `dl.google.com` /
> `maven.google.com` / `services.gradle.org`, así que esos cuatro comandos
> **no se pudieron ejecutar de extremo a extremo** en ese entorno. Para
> eso está la Opción B: `.github/workflows/android-build.yml` ejecuta
> esos mismos comandos en un runner de GitHub con acceso real a esos
> repositorios. Lo que sí se compiló y ejecutó de verdad en este entorno,
> con evidencia guardada, fue toda la capa de dominio (`domain/model` +
> `domain/logic`, cero dependencias de Android) y el esquema SQL
> completo. Detalles exactos en [`docs/BUILD_REPORT.md`](docs/BUILD_REPORT.md).

## Estructura del repositorio

```
.github/workflows/       Workflow de GitHub Actions (compila el APK en cada push)
app/                    Código fuente Android (Kotlin/Compose)
  src/main/java/com/educalab/clasesmart/
    data/local/          Entidades Room, DAOs, converters, AppDatabase
    data/repository/     Repositorios (Entity <-> modelo de dominio)
    data/seed/           Contenido semilla (personajes, situaciones, insignias...)
    domain/model/        Modelos de dominio puros (sin Android)
    domain/logic/         8 motores de lógica de negocio (testeados)
    di/                  Contenedor de dependencias manual
    ui/scene/            Escena del aula: ilustraciones y objetos interactivos
    ui/screens/          Pantallas de cada módulo
    ui/viewmodel/        ViewModels (MVVM)
    ui/navigation/       NavGraph y transiciones propias
    ui/theme/            Tema visual propio de ClaseSmart
  src/test/java/...      48 tests JUnit de la capa de dominio
database/                schema.sql y sample_data.sql (reflejan la app real)
docs/                    Documentación completa (ver abajo)
deliverables/            Entregables finales (ZIP fuente + PDFs; ver nota de APK)
```


## Privacidad y offline

ClaseSmart no usa Internet, no tiene backend, no usa Firebase/analytics/ads,
no pide login ni datos personales reales. No declara el permiso
`INTERNET` en el manifest. Todo el progreso vive en el dispositivo, en una
base de datos Room local.
