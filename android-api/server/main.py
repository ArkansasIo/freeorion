"""Read-only companion API for FreeOrion project metadata."""
from datetime import datetime, timezone
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI(
    title="FreeOrion Companion API",
    version="1.0.0",
    description="Read-only metadata and health API. No live game state or save mutation is exposed.",
)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Restrict to trusted origins in deployed environments.
    allow_credentials=False,
    allow_methods=["GET"],
    allow_headers=["*"],
)

@app.get("/health", tags=["system"])
def health():
    return {"status": "ok", "service": "freeorion-companion-api", "utc": datetime.now(timezone.utc).isoformat()}

@app.get("/api/v1/game", tags=["game"])
def game_info():
    return {
        "id": "freeorion",
        "name": "FreeOrion",
        "genre": "4X turn-based strategy",
        "platform": "Companion API for Android",
        "source": "https://github.com/ArkansasIo/freeorion",
        "integration": "Companion metadata API",
        "capabilities": ["health-check", "game-metadata"],
        "limitations": [
            "does-not-control-running-game",
            "does-not-read-save-files",
            "does-not-provide-multiplayer-session"
        ],
    }

@app.get("/api/v1/status", tags=["system"])
def service_status():
    return {"status": "operational", "api_version": "1.0.0", "game_bridge": "not-configured"}
