# FreeOrion Android Companion + API

This directory adds a small Android companion application and an optional REST service for status/metadata integration with FreeOrion. It does **not** replace the existing native/Godot Android game export already maintained in `.github/workflows/_build-android.yml`.

## Components
- `app/`: Android app (Kotlin, Jetpack Compose) with home, API status, and galaxy/game metadata screens.
- `server/`: FastAPI service with health, game metadata, and status endpoints.
- `openapi.yaml`: OpenAPI 3.1 contract.
- `docs/API.md`: endpoint examples and configuration.
- `../.github/workflows/android-companion.yml`: CI build for debug APK and service tests.

## Build the APK
Use Android Studio with Android SDK 35 and JDK 17, open `android-api/app`, then run:
```sh
./gradlew assembleDebug
```
The APK output is `app/build/outputs/apk/debug/app-debug.apk`.

Or use the repository Actions workflow: Actions → **FreeOrion Android Companion** → Run workflow; download the `freeorion-android-companion-debug-apk` artifact.

## Run the REST API locally
Python 3.11+ recommended:
```sh
cd android-api/server
python -m venv .venv
# macOS/Linux
source .venv/bin/activate
# Windows PowerShell: .venv\Scripts\Activate.ps1
pip install -r requirements.txt
uvicorn main:app --host 0.0.0.0 --port 8080
```
API docs: `http://127.0.0.1:8080/docs`.

Configure the app's API base URL in `app/src/main/java/org/arkansasio/freeorion/api/ApiConfig.kt`. Android emulator access to a host machine uses `http://10.0.2.2:8080/`. For a real device, use your computer's LAN IP and ensure firewall/network access. HTTPS is required for public deployments.

## Integration boundary
The API currently exposes service health and static FreeOrion project metadata. It does not read or mutate live game saves, command the game engine, or provide multiplayer services. Those require a deliberate server-side adapter and a stable game-state contract. Never expose an unauthenticated write endpoint to a public network.

## License
FreeOrion's upstream source is GPL-2.0-or-later as stated by its repository; this companion source is distributed under GPL-2.0-or-later for compatibility. Check upstream `default/COPYING` and third-party notices before redistribution.
