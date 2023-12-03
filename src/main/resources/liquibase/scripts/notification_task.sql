-- liquibase formatted sql

-- changeset alina:1
CREATE TABLE notification_task (
    id BIGSERIAL,
    chat_id BIGINT,
    message_text TEXT NOT NULL,
    notification_send_time TIME
)