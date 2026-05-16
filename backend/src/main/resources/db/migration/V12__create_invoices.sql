CREATE TABLE invoices (
    id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
    company_id          UUID            NOT NULL,
    client_id           UUID            NOT NULL,
    transport_job_id    UUID,
    invoice_number      VARCHAR(50)     NOT NULL,
    issue_date          DATE            NOT NULL,
    due_date            DATE            NOT NULL,
    amount              NUMERIC(10,2)   NOT NULL,
    status              VARCHAR(30)     NOT NULL DEFAULT 'PENDING',
    payment_date        DATE,
    amount_paid         NUMERIC(10,2),
    reminder_sent_at    TIMESTAMPTZ,
    notes               TEXT,
    created_at          TIMESTAMPTZ     NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ     NOT NULL DEFAULT now(),
    created_by          VARCHAR(255),

    CONSTRAINT pk_invoices PRIMARY KEY (id),
    CONSTRAINT fk_invoices_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT uq_invoice_number UNIQUE (company_id, invoice_number)
);

CREATE INDEX idx_invoices_company_id ON invoices(company_id);
CREATE INDEX idx_invoices_client_id ON invoices(client_id);
CREATE INDEX idx_invoices_status ON invoices(status);