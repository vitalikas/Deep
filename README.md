# Deep

Kotlin Multiplatform (KMP) application with Clean Architecture and MVI pattern. Supports Android and iOS platforms.

## Architecture

### Modular Structure

```
├── composeApp/              # Shared Compose UI entry point
├── core/                    # Core modules
│   ├── presentation/        # MVI base classes, utilities
│   ├── domain/              # Domain errors, utilities
│   ├── data/                # Network, logging
│   ├── database/            # Room database
│   └── designsystem/        # UI components, theme
├── feature/                 # Feature modules
│   ├── auth/                # Authentication (login/logout)
│   │   ├── presentation/
│   │   ├── domain/
│   │   └── data/
│   └── scan/                # Scan list, bathymetry
│       ├── presentation/
│       ├── domain/
│       └── data/
└── build-logic/             # Convention plugins
```

Each feature follows **Clean Architecture** with three layers:
- **Presentation** - UI, ViewModels, MVI pattern
- **Domain** - Use cases, repositories interfaces, models
- **Data** - Repository implementations, API, local database

### MVI Pattern

Custom MVI implementation with:
- `BaseStore<I, S, E>` - Core store logic (thread-safe, lifecycle-aware)
- `MviViewModel` - Android ViewModel wrapper
- `Reducer<S, I>` - Pure state transformations
- `Middleware<I, S, E>` - Side effects (API calls, navigation)

Key features:
- Type-safe DSL for reducers
- Optional `initialIntent` for auto-initialization
- State observation via `StateFlow`
- Effects via `SharedFlow` (one-time events)

## Authentication Flow

```
AppViewModel                      AuthMiddleware
     |                               |
     |-- CheckAuth (initialIntent) -->
     |                               |
     |<-- AuthChecked(isAuth=true) --| (from isAuthenticatedUseCase flow)
     |                               |
AuthNav (login)              MainNav (scans)
     |                               |
     |                               |
LoginStore -- Authenticated ------> AppViewModel
     |                               |
     |                          ScanListScreen
     |                               |
     |<-- Logout (on logout click) --|
```

- Auth state is **single source of truth** in `AppViewModel`
- No callbacks between screens - all via state observation
- Automatic navigation on auth state changes
- Secure logout clears all cached data + token

## CI/CD

GitHub Actions workflow (`.github/workflows/ci.yml`):
- Runs on push to `main`, `develop` and PRs
- Executes `ScanListReducerTest` unit tests
- Requires `API_KEY` secret in GitHub repository settings

## Configuration

### API Key

API key is injected via **BuildKonfig** at build time:

1. Create `local.properties` in project root:
```properties
sdk.dir=/path/to/android/sdk
API_KEY=your_api_key_here
```

2. For CI, add `API_KEY` to GitHub Secrets:
   - Repository → Settings → Secrets and variables → Actions → New repository secret

## Testing

### Unit Tests

Located in `src/commonTest/kotlin`:
- `ScanListReducerTest` - Reducer state transformations

Run locally:
```bash
./gradlew :feature:scan:presentation:testDebugUnitTest
```

### UI Tests

Located in `src/androidInstrumentedTest/kotlin`:
- `ScanListScreenTest` - Compose UI tests

Requires Android emulator or device.

## Build & Run

### Android

```bash
./gradlew :composeApp:assembleDebug
```

Or use run configuration in Android Studio.

### iOS

Open `/iosApp` in Xcode and run, or use run configuration in Android Studio.

## Security

- Tokens stored in encrypted database (SQLCipher via Room)
- Automatic cache invalidation on logout
- API keys excluded from version control via BuildKonfig
- No sensitive data in composable layer - all auth via ViewModel state

## Tech Stack

- **Kotlin**: 2.2.0
- **UI**: Compose Multiplatform 1.10.1, Material3
- **DI**: Koin 4.1.1
- **Database**: Room KMP 2.8.4
- **Network**: Ktor 3.3.1
- **Serialization**: kotlinx.serialization 1.9.0
- **Time**: kotlin.time (Kotlin 2.2.0+)
- **Navigation**: JetBrains Navigation 2.9.2
- **Build**: Gradle Convention Plugins
