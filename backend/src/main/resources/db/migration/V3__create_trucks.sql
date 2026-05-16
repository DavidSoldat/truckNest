CREATE TABLE trucks (
    id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
    company_id          UUID            NOT NULL,
    plate_number        VARCHAR(30)     NOT NULL,
    make                VARCHAR(100)    NOT NULL,
    model               VARCHAR(100)    NOT NULL,
    year                SMALLINT        NOT NULL,
    vin                 VARCHAR(50),
    next_service_date   DATE,
    euro_standard       VARCHAR(30)     NOT NULL DEFAULT 'EURO_6',
    status              VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    notes               TEXT,
    created_at          TIMESTAMPTZ     NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ     NOT NULL DEFAULT now(),
    created_by          VARCHAR(255),

    CONSTRAINT pk_trucks PRIMARY KEY (id),
    CONSTRAINT fk_trucks_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE INDEX idx_trucks_company_id ON trucks(company_id);