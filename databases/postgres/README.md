# PostgreSQL Demo

Минимальный пример настройки PostgreSQL с демонстрацией ключевых возможностей.

## Что демонстрирует

1. **Кастомная конфигурация** через `custom.conf`
   - Включено логирование всех SQL-запросов (`log_statement = 'all'`)
   - Настроен порог для медленных запросов (`log_min_duration_statement = 100`)

2. **Уровни изоляции транзакций**
   - Показан `READ COMMITTED` (по умолчанию в PostgreSQL)

3. **Проверка настроек**
   - Скрипт `verify/check.sql` проверяет, что конфигурация применилась

## Быстрый старт

### Запуск
```bash
docker compose up -d

### Проверка
```bash
docker exec -i pg-demo psql -U demo -d demo_db < verify/check.sql
docker logs pg-demo 2>&1 | grep "statement:"