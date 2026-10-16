from fastapi.testclient import TestClient
from main import app

client = TestClient(app)

def test_health():
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json()["status"] == "ok"

def test_game_info():
    response = client.get("/api/v1/game")
    assert response.status_code == 200
    assert response.json()["id"] == "freeorion"

def test_status_does_not_claim_live_bridge():
    response = client.get("/api/v1/status")
    assert response.status_code == 200
    assert response.json()["game_bridge"] == "not-configured"
