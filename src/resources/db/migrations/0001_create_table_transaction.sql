CREATE TABLE financial_transaction (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    user_id INTEGER NOT NULL,
    type TEXT CHECK(type IN ('CREDIT', 'DEBIT')) NOT NULL,
    amount_in_cents INTEGER NOT NULL,
    date TEXT NOT NULL,
    category TEXT,
    description TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);