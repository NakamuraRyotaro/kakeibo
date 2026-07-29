CREATE TABLE users (
    id          BIGSERIAL    PRIMARY KEY,
    cognito_sub CHAR(36)     NOT NULL UNIQUE,
    nickname    VARCHAR(50)  NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

COMMENT ON TABLE users IS 'アプリケーション利用者。Cognito の sub と 1:1 で対応する';
COMMENT ON COLUMN users.cognito_sub IS 'Cognito ユーザープールの sub(UUID)';