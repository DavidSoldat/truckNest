CREATE TABLE truck_tires (
                             id                          UUID        NOT NULL DEFAULT gen_random_uuid(),
                             company_id                  UUID        NOT NULL,
                             truck_id                    UUID        NOT NULL,
                             position                    VARCHAR(30) NOT NULL,
                             brand                       VARCHAR(100),
                             fit_date                    DATE,
                             expected_replacement_date   DATE,
                             notes                       TEXT,
                             created_at                  TIMESTAMPTZ NOT NULL DEFAULT now(),
                             updated_at                  TIMESTAMPTZ NOT NULL DEFAULT now(),
                             created_by                  VARCHAR(255),

                             CONSTRAINT pk_truck_tires PRIMARY KEY (id),
                             CONSTRAINT fk_truck_tires_truck FOREIGN KEY (truck_id) REFERENCES trucks(id),
                             CONSTRAINT fk_truck_tires_company FOREIGN KEY (company_id) REFERENCES companies(id),
                             CONSTRAINT uq_truck_tire_position UNIQUE (truck_id, position)
);

CREATE INDEX idx_truck_tires_truck_id ON truck_tires(truck_id);