CREATE TABLE financial_summary (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    date_init TEXT NOT NULL,
    date_end TEXT NOT NULL,
    total_credit_in_cents INTEGER NOT NULL,
    total_debit_in_cents INTEGER NOT NULL,
    result_in_cents INTEGER NOT NULL,
    
    FOREIGN KEY (user_id) REFERENCES user(id)
)