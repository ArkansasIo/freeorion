# FreeOrion Companion API

Base URL examples:
- Emulator from Android app: `http://10.0.2.2:8080`
- Local computer: `http://127.0.0.1:8080`
- Deployed server: use your configured HTTPS base URL.

## GET /health
Returns process health and a UTC timestamp.

```json
{"status":"ok","service":"freeorion-companion-api","utc":"2026-01-01T00:00:00+00:00"}
```

## GET /api/v1/game
Returns static project metadata, integration capabilities, and current limitations.

## GET /api/v1/status
Returns API status and explicitly reports that a live game bridge is not configured.

## Security and versioning
All endpoints are GET-only and read-only. Keep CORS origins restricted in production, deploy behind HTTPS, and add authentication/rate limiting before adding player-specific or write operations. Never expose filesystem paths, save data, admin operations, or game-server controls without authorization. The `/api/v1` prefix allows compatible future versions.
