# FoodleBump

A modernized fork of libGDX's classic **Super Jumper** demo (a simple Doodle Jump clone), brought up to a current Gradle / Android / libGDX toolchain so it builds and runs on **JDK 21**.

The original demo was last set up for the old `gdx-setup` era and wouldn't build on a modern JDK — cloning it on JDK 21 fails immediately because its Gradle 6.7.1 wrapper can't run on anything newer than Java 15. This fork fixes that and removes a pile of accumulated bit-rot along the way.

---

## What was modernized

| Component | Original | This fork |
|---|---|---|
| Gradle | 6.7.1 | **8.11.1** |
| Android Gradle Plugin | ~4.x (fetched over HTTP at build time) | **8.10.1** |
| libGDX | fetched dynamically from a now-defunct endpoint | **1.14.2** (pinned) |
| Desktop backend | LWJGL (v2) | **LWJGL3** |
| Dependency configurations | `compile` (removed in Gradle 7) | `api` / `implementation` |
| JDK to run the build | 8–15 only | **17 or 21** |
| Android `compileSdk` | (dynamic) | **36** (targetSdk 34, minSdk 21) |
| Modules | core, desktop, android, html, ios | **core, desktop, android** |

### The specific changes

- **Killed the dynamic version fetch.** The original root `build.gradle` pulled every dependency version at configure time from `http://libgdx.com/service/versions.json` via an `ant.get(...)` call. That endpoint is unreliable today, so a flaky network meant a failed build. All versions are now pinned, with `gdxVersion` living in `gradle.properties` as the single source of truth.
- **Upgraded the Gradle wrapper to 8.11.1** so the build runs on modern JDKs (this fork is developed on JDK 21).
- **Switched the desktop backend from LWJGL to LWJGL3.** `DesktopLauncher` was rewritten to use `Lwjgl3Application` / `Lwjgl3ApplicationConfiguration`, including the macOS `-XstartOnFirstThread` flag that LWJGL3 requires.
- **Migrated the Android module to AGP 8.** Added the now-mandatory `namespace`, removed the forbidden `package` attribute from the manifest, added `android:exported` to the launcher activity, dropped the removed RenderScript source sets, and replaced the deprecated `whenTaskAdded` hook with `configureEach`.
- **Replaced `compile` with `api`/`implementation`** throughout, since `compile` was removed in Gradle 7.
- **Dropped the `html` (GWT) and `ios` (RoboVM) modules.** These are the hardest to keep current (GWT is being superseded by TeaVM; RoboVM needs macOS + signing). Removing them dramatically simplifies the build. They can be re-added later via [gdx-liftoff](https://github.com/libgdx/gdx-liftoff) if needed.

The game code itself (the `core` module) is unchanged — this was a build/toolchain modernization, not a gameplay rewrite.

> **A note on Java versions:** the *build* runs on JDK 21, but the shared `core` and `android` code compiles to Java 8 bytecode so Android devices stay happy without extra desugaring config. The standalone `desktop` module compiles to Java 17. Raise these in the respective `build.gradle` files if you want newer language features.

---

## Project layout

```
core/      Shared game logic (platform-independent)
desktop/   LWJGL3 launcher for Windows / macOS / Linux
android/   Android app module (assets live in android/assets)
```

---

## Building & running

### Prerequisites

- **JDK 17 or 21.** Confirm with `java -version`.
- **For the Android module only:** the Android SDK, with **SDK Platform 36** installed (via Android Studio's SDK Manager). Create a `local.properties` file in the repo root pointing at your SDK — this file is machine-specific and git-ignored:
  ```properties
  sdk.dir=C:/Users/<you>/AppData/Local/Android/Sdk
  ```
  If you only want the desktop game, you can skip the Android SDK entirely.

### Commands

```bash
# Run the desktop game (no Android SDK required)
./gradlew :desktop:run

# Build a runnable fat jar  ->  desktop/build/lib/*.jar
./gradlew :desktop:dist

# Build an Android debug APK  ->  android/build/outputs/apk/debug/
./gradlew :android:assembleDebug
```

In IntelliJ IDEA / Android Studio: open the root `build.gradle` as a project and set the **Gradle JDK to 17 or 21** (Settings → Build, Execution, Deployment → Build Tools → Gradle).

---

## Credits

This is a fork of the [libGDX Super Jumper demo](https://github.com/libgdx/libgdx-demo-superjumper), originally created by the libGDX team. libGDX and the original demo are licensed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0). All modernization work here is in the same spirit — full credit for the original game to the libGDX authors.

- libGDX framework: <https://libgdx.com>
- Original demo: <https://github.com/libgdx/libgdx-demo-superjumper>
