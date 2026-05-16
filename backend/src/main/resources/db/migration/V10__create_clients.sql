CREATE TABLE clients (
    id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
    company_id          UUID            NOT NULL,
    name                VARCHAR(255)    NOT NULL,
    contact_person      VARCHAR(255),
    contact_email       VARCHAR(255),
    phone               VARCHAR(50),
    payment_terms_days  SMALLINT        NOT NULL DEFAULT 30,
    notes               TEXT,
    created_at          TIMESTAMPTZ     NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ     NOT NULL DEFAULT now(),
    created_by          VARCHAR(255),

    CONSTRAINT pk_clients PRIMARY KEY (id),
    CONSTRAINT fk_clients_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT chk_payment_terms CHECK (payment_terms_days IN (30, 60, 90))
);

CREATE INDEX idx_clients_company_id ON clients(company_id);