CREATE TABLE transport_jobs (
    id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
    company_id          UUID            NOT NULL,
    client_id           UUID            NOT NULL,
    truck_id            UUID,
    driver_id           UUID,
    job_date            DATE            NOT NULL,
    origin              VARCHAR(255),
    destination         VARCHAR(255),
    cargo_description   TEXT,
    distance_km         INTEGER,
    notes               TEXT,
    created_at          TIMESTAMPTZ     NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ     NOT NULL DEFAULT now(),
    created_by          VARCHAR(255),

    CONSTRAINT pk_transport_jobs PRIMARY KEY (id),
    CONSTRAINT fk_transport_jobs_client FOREIGN KEY (client_id) REFERENCES clients(id),
    CONSTRAINT fk_transport_jobs_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE INDEX idx_transport_jobs_client_id ON transport_jobs(client_id);
CREATE INDEX idx_transport_jobs_company_id ON transport_jobs(company_id);