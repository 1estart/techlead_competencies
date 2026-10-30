"""Генератор фейковых спортивных событий."""

import random
import uuid
from datetime import datetime, timezone

TEAMS = [
    "Spartak", "Zenit", "CSKA", "Dinamo",
    "Lokomotiv", "Krasnodar", "Rostov", "Rubin",
]

EVENT_TYPES = ["goal", "yellow_card", "red_card", "substitution", "penalty"]


def generate_event(match_id: str | None = None) -> dict:
    """Генерирует одно случайное спортивное событие."""
    return {
        "event_id": str(uuid.uuid4()),
        "match_id": match_id or str(uuid.uuid4()),
        "event_type": random.choice(EVENT_TYPES),
        "team": random.choice(TEAMS),
        "player": f"player_{random.randint(1, 99)}",
        "minute": random.randint(1, 90),
        "timestamp": datetime.now(timezone.utc).isoformat(),
    }


def generate_match_events(count: int = 10) -> list[dict]:
    """Генерирует пачку событий для одного матча."""
    match_id = str(uuid.uuid4())
    return [generate_event(match_id) for _ in range(count)]


def generate_batch(batch_size: int = 50) -> list[dict]:
    """Генерирует батч событий для разных матчей."""
    return [generate_event() for _ in range(batch_size)]