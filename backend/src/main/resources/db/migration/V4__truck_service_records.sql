CREATE TABLE truck_service_records (
    id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
    company_id          UUID            NOT NULL,
    truck_id            UUID            NOT NULL,
    service_date        DATE            NOT NULL,
    service_type        VARCHAR(100)    NOT NULL,
    cost                NUMERIC(10,2),
    odometer_km         INTEGER,
    next_service_date   DATE,
    notes               TEXT,
    created_at          TIMESTAMPTZ     NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ     NOT NULL DEFAULT now(),
    created_by          VARCHAR(255),

    CONSTRAINT pk_truck_service_records PRIMARY KEY (id),
    CONSTRAINT fk_service_records_truck FOREIGN KEY (truck_id) REFERENCES trucks(id),
    CONSTRAINT fk_service_records_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE INDEX idx_service_records_truck_id ON truck_service_records(truck_id);
CREATE INDEX idx_service_records_company_id ON truck_service_records(company_id);