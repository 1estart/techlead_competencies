-- ============================================================
-- Демонстрация: Настройка уровня изоляции транзакций
-- ============================================================

-- Создаём таблицу для демонстрации
CREATE TABLE IF NOT EXISTS accounts (
    id          SERIAL PRIMARY KEY,
    owner       VARCHAR(100) NOT NULL,
    balance     NUMERIC(10, 2) NOT NULL DEFAULT 0,
    created_at  TIMESTAMP DEFAULT NOW()
);

-- Вставляем тестовые данные
INSERT INTO accounts (owner, balance) VALUES
    ('Alice', 1000.00),
    ('Bob', 500.00),
    ('Charlie', 750.00);

-- Создаём индекс для быстрого поиска по владельцу
CREATE INDEX idx_accounts_owner ON accounts(owner);

-- ============================================================
-- Демонстрация уровня изоляции READ COMMITTED (по умолчанию)
-- ============================================================

-- Начинаем транзакцию
BEGIN;

-- Устанавливаем уровень изоляции (для демонстрации)
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;

-- Выполняем перевод денег от Alice к Bob
UPDATE accounts SET balance = balance - 100 WHERE owner = 'Alice';
UPDATE accounts SET balance = balance + 100 WHERE owner = 'Bob';

-- Фиксируем транзакцию
COMMIT;

-- Проверяем результат
SELECT owner, balance FROM accounts ORDER BY id;