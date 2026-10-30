# запуск тестов
uv run pytest tests/ -v

# запуск API
uv run uvicorn src.ingestion.api:app --reload --port 8000

# проверка в другом терминале
curl http://localhost:8000/health
curl http://localhost:8000/events/batch?size=5