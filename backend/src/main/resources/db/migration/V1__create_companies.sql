CREATE TABLE companies (
                           id          UUID        NOT NULL DEFAULT gen_random_uuid(),
                           name        VARCHAR(255) NOT NULL,
                           contact_email VARCHAR(255),
                           phone       VARCHAR(50),
                           subscription_tier VARCHAR(50) NOT NULL DEFAULT 'free',
                           created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
                           updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
                           created_by  VARCHAR(255),

                           CONSTRAINT pk_companies PRIMARY KEY (id)
);