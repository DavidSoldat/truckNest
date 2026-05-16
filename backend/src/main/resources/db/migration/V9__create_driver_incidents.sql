CREATE TABLE driver_incidents (
    id              UUID            NOT NULL DEFAULT gen_random_uuid(),
    company_id      UUID            NOT NULL,
    driver_id       UUID            NOT NULL,
    incident_date   DATE            NOT NULL,
    incident_type   VARCHAR(100)    NOT NULL,
    description     TEXT,
    cost            NUMERIC(10,2),
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
    created_by      VARCHAR(255),

    CONSTRAINT pk_driver_incidents PRIMARY KEY (id),
    CONSTRAINT fk_incidents_driver FOREIGN KEY (driver_id) REFERENCES drivers(id),
    CONSTRAINT fk_incidents_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE INDEX idx_incidents_driver_id ON driver_incidents(driver_id);
CREATE INDEX idx_incidents_company_id ON driver_incidents(company_id);