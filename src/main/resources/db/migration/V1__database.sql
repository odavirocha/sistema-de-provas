CREATE TABLE user_table(
    id UUID PRIMARY KEY,
    email VARCHAR,
    password VARCHAR,
    role VARCHAR(15)
);

CREATE TABLE refresh_token_table(
    refresh_token UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    issued_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_refresh_user_id 
        FOREIGN KEY (user_id)
        REFERENCES user_table(id)
);

CREATE TABLE test_table(
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    name VARCHAR,
    CONSTRAINT fk_test_user_id
        FOREIGN KEY (user_id)
        REFERENCES user_table(id)
);

CREATE TABLE question_table(
    id UUID PRIMARY KEY,
    test_id UUID NOT NULL,
    question VARCHAR,
    CONSTRAINT fk_question_test_id
        FOREIGN KEY (test_id)
        REFERENCES test_table(id)
);

CREATE TABLE option_table(
    id UUID PRIMARY KEY,
    value VARCHAR,
    is_correct BOOLEAN,
    question_id UUID NOT NULL,
    CONSTRAINT fk_option_question_id
        FOREIGN KEY (question_id)
        REFERENCES question_table(id)
);