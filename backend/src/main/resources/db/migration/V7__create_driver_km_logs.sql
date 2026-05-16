CREATE TABLE driver_km_logs (
                                id          UUID        NOT NULL DEFAULT gen_random_uuid(),
                                company_id  UUID        NOT NULL,
                                driver_id   UUID        NOT NULL,
                                truck_id    UUID,
                                log_date    DATE        NOT NULL,
                                km_driven   INTEGER     NOT NULL,
                                route_notes TEXT,
                                created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
                                updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
                                created_by  VARCHAR(255),

                                CONSTRAINT pk_driver_km_logs PRIMARY KEY (id),
                                CONSTRAINT fk_km_logs_driver FOREIGN KEY (driver_id) REFERENCES drivers(id),
                                CONSTRAINT fk_km_logs_truck FOREIGN KEY (truck_id) REFERENCES trucks(id),
                                CONSTRAINT fk_km_logs_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE INDEX idx_km_logs_driver_id ON driver_km_logs(driver_id);
CREATE INDEX idx_km_logs_company_id ON driver_km_logs(company_id);