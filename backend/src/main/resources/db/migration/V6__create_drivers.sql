CREATE TABLE drivers (
                         id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
                         company_id          UUID            NOT NULL,
                         first_name          VARCHAR(100)    NOT NULL,
                         last_name           VARCHAR(100)    NOT NULL,
                         date_of_birth       DATE,
                         phone               VARCHAR(50),
                         email               VARCHAR(255),
                         license_number      VARCHAR(50),
                         license_expiry      DATE,
                         visa_expiry         DATE,
                         status              VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
                         monthly_salary      NUMERIC(10,2),
                         notes               TEXT,
                         created_at          TIMESTAMPTZ     NOT NULL DEFAULT now(),
                         updated_at          TIMESTAMPTZ     NOT NULL DEFAULT now(),
                         created_by          VARCHAR(255),

                         CONSTRAINT pk_drivers PRIMARY KEY (id),
                         CONSTRAINT fk_drivers_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE INDEX idx_drivers_company_id ON drivers(company_id);