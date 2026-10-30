from ingestion.event_generator import generate_batch, generate_event


def test_generate_event_structure():
    event = generate_event()
    assert "event_id" in event
    assert "match_id" in event
    assert "event_type" in event
    assert "timestamp" in event


def test_generate_batch_size():
    events = generate_batch(20)
    assert len(events) == 20