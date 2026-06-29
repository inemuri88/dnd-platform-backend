-- =====================================================================
-- Flyway migration V1 - creazione tabella utenti.
--
-- Convenzione di naming Flyway:  V<versione>__<descrizione>.sql
--   - "V"  => migration versionata (eseguita una sola volta, in ordine di versione)
--   - il doppio underscore "__" separa versione e descrizione
-- All'avvio Flyway registra l'avvenuta esecuzione nella tabella "flyway_schema_history".
--
-- Lo schema qui sotto deve combaciare con l'entita' User (JPA gira in ddl-auto=validate).
-- =====================================================================

CREATE TABLE users (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    username   VARCHAR(50)  NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(20)  NOT NULL,
    enabled    BIT          NOT NULL,
    created_at DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email    UNIQUE (email)
) ENGINE = InnoDB;
