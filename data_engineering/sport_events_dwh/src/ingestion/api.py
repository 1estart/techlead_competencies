"""Mock API для спортивных событий."""

from fastapi import FastAPI

from src.ingestion.event_generator import generate_batch, generate_match_events

app = FastAPI(title="Sports Events Mock API")


@app.get("/health")
def health():
    return {"status": "ok"}


@app.get("/events/batch")
def events_batch(size: int = 50):
    """Батч случайных событий (для batch-загрузки)."""
    return {"events": generate_batch(size)}


@app.get("/events/match")
def match_events(count: int = 10):
    """События одного матча."""
    return {"events": generate_match_events(count)}