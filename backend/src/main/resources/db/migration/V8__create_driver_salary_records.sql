CREATE TABLE driver_salary_records (
    id              UUID            NOT NULL DEFAULT gen_random_uuid(),
    company_id      UUID            NOT NULL,
    driver_id       UUID            NOT NULL,
    period_month    SMALLINT        NOT NULL,
    period_year     SMALLINT        NOT NULL,
    amount_paid     NUMERIC(10,2)   NOT NULL,
    payment_date    DATE            NOT NULL,
    notes           TEXT,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
    created_by      VARCHAR(255),

    CONSTRAINT pk_salary_records PRIMARY KEY (id),
    CONSTRAINT fk_salary_records_driver FOREIGN KEY (driver_id) REFERENCES drivers(id),
    CONSTRAINT fk_salary_records_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT uq_salary_period UNIQUE (driver_id, period_month, period_year)
);

CREATE INDEX idx_salary_records_driver_id ON driver_salary_records(driver_id);