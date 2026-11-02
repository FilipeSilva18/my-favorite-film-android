# build-logic/ — convention plugins

Complementa o [AGENTS.md da raiz](../AGENTS.md).

Os módulos não repetem configuração: cada `build.gradle.kts` aplica plugins `movies.*` e declara só as dependências próprias. Versões ficam em `gradle/libs.versions.toml`, compartilhado com este build incluído.

| Plugin | Classe | Usado em |
|---|---|---|
| `movies.android.application` | `AndroidApplicationConventionPlugin` | `:app` (SDKs, Orchestrator, Kover, task `unitTest`) |
| `movies.android.library` | `AndroidLibraryConventionPlugin` | Todo módulo Android |
| `movies.android.compose` | `AndroidComposeConventionPlugin` | Módulos com Compose |
| `movies.jvm.library` | `JvmLibraryConventionPlugin` | Módulos JVM (`domain`, `api`, `common`, `analytics`, `ai`, `testing`) |
| `movies.dagger` | `DaggerConventionPlugin` | Módulos com `@Inject`, `@Module` ou `@Component` |
| `movies.android.room` | `AndroidRoomConventionPlugin` | `core:database` |
| `movies.android.feature.data` | `AndroidFeatureDataConventionPlugin` | `feature:*:data` |
| `movies.android.feature.presentation` | `AndroidFeaturePresentationConventionPlugin` | `feature:*:presentation` |
| `movies.android.robolectric` | `AndroidRobolectricConventionPlugin` | Módulos com Robolectric, Compose UI Test e Roborazzi |

## Regras

- Configuração repetida em dois ou mais módulos vira convention plugin; não copie blocos entre `build.gradle.kts`.
- Plugin novo: classe em `convention/src/main/kotlin`, registro em `convention/build.gradle.kts`, alias em `[plugins]` do catálogo.
- AGP 9 usa o Kotlin embutido: não aplique `kotlin-android`. O `jvmTarget` é configurado nas tasks `KotlinJvmCompile` (`KotlinConfig.kt`).
- Os testes Robolectric precisam dos `--add-opens` e `--enable-native-access` do `AndroidRobolectricConventionPlugin` no JDK 24+. Não remova.
- Todo plugin base registra a task agregadora `unitTest`; o CI depende dela.
- Mudança aqui afeta todos os módulos: rode `./scripts/verify.sh` completo.
