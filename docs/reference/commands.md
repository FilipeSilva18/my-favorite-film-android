# Commands

All of them run from the repository root, with JDK 21.

## Scripts

| Script | What it does |
|---|---|
| `./scripts/verify.sh` | Module graph, Konsist, JVM tests, screenshots and lint, without stopping at the first failure. It is what the CI `checks` job runs |
| `./scripts/verify.sh --quick` | Only module graph and Konsist (seconds) |
| `./scripts/verify-module.sh <module>` | JVM tests of one module + Konsist + graph. E.g. `:feature:movies:presentation` |
| `./scripts/record-screenshots.sh [module]` | Re-records the Roborazzi references (all or one module) and lists the changed PNGs |
| `./scripts/install-git-hooks.sh` | Installs the `pre-commit` hook that blocks secrets |
| `./scripts/check-docs.py` | Validates relative links in `.md` files and the skills' frontmatter |

## Gradle

| Command | What it does |
|---|---|
| `./gradlew assertModuleGraph` | Module graph rules (`moduleGraphAssert` in the root build) |
| `./gradlew :architecture-test:unitTest` | Konsist rules |
| `./gradlew unitTest` | Every JVM test (unit, integration, Compose with Robolectric, DI, Konsist) |
| `./gradlew :<module>:unitTest` | JVM tests of one module |
| `./gradlew verifyRoborazziDebug` | Fails if a screenshot changes |
| `./gradlew recordRoborazziDebug` | Re-records screenshots |
| `./gradlew compareRoborazziDebug` | Generates diff images for review |
| `./gradlew lintDebug` | Android Lint |
| `./gradlew koverHtmlReport` | Aggregated coverage in `build/reports/kover/html` |
| `./gradlew connectedDebugAndroidTest` | Instrumented tests (emulator or connected device) |
| `./gradlew :app:assembleDebug` | Debug APK |

## Local configuration

| File | Contents | Committed |
|---|---|---|
| `local.properties` | `sdk.dir`, `TMDB_ACCESS_TOKEN=<TMDB read access token>` | No |
| `app/google-services.json` | Firebase configuration (optional; without it analytics and AI are disabled) | No |

The App Check debug token shows up in Logcat (filter `DebugAppCheckProvider`) and is registered in the Firebase console. It changes when the app is reinstalled or its data is cleared. It is never committed.

## CI (`.github/workflows/ci.yml`, "Android CI")

| Job | Steps |
|---|---|
| `checks` | Docs check → `assertModuleGraph` → Konsist → `unitTest` → `verifyRoborazziDebug` → `lintDebug` → Kover |
| `instrumented` | API 35 emulator → `connectedDebugAndroidTest` |

Secrets: `TMDB_ACCESS_TOKEN`, `GOOGLE_SERVICES_JSON`.
