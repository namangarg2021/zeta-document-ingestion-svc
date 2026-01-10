CREATE SCHEMA IF NOT EXISTS document_qa_processor;

CREATE TABLE IF NOT EXISTS document_qa_processor.document_chat_sessions (
    session_id                uuid NOT NULL,
    advisor_id                uuid,
    client_id                 uuid,
    doc_id                    uuid,
    chat_session              jsonb,
    chat_type                 text,
    session_name              text,
    created_by                text,
    created_at                timestamp without time zone NOT NULL,
    updated_at                timestamp without time zone,
    CONSTRAINT document_chat_sessions_pkey PRIMARY KEY (session_id)
);

CREATE TABLE IF NOT EXISTS document_qa_processor.document_registry (
    id             uuid NOT NULL DEFAULT gen_random_uuid(),
    session_id     uuid NOT NULL,
    client_id      uuid,
    advisor_id     uuid,
    doc_id         uuid NOT NULL,
    filename       text NOT NULL,
    s3_file_path   text NOT NULL,
    created_by     text NOT NULL,
    created_at     timestamp without time zone NOT NULL,
    updated_at     timestamp without time zone NOT NULL,
    CONSTRAINT document_registry_pkey PRIMARY KEY (id)
);
