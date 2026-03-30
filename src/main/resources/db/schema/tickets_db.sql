

-- Schema used when you create a MySQL database manually.
-- It matches the current JPA entity mappings (see src/main/java/.../entity).

CREATE TABLE IF NOT EXISTS users (
	id BIGINT NOT NULL AUTO_INCREMENT,
	username VARCHAR(100) NOT NULL,
	password VARCHAR(255) NOT NULL,
	role VARCHAR(20) NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT uk_users_username UNIQUE (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS tickets (
	id BIGINT NOT NULL AUTO_INCREMENT,
	title VARCHAR(150) NOT NULL,
	description VARCHAR(2000) NOT NULL,
	status VARCHAR(20) NOT NULL,
	created_by VARCHAR(100) NOT NULL,
	assigned_to_id BIGINT NULL,
	resolution_note VARCHAR(2000) NULL,
	created_at TIMESTAMP(6) NOT NULL,
	updated_at TIMESTAMP(6) NOT NULL,
	PRIMARY KEY (id),
	INDEX idx_tickets_status (status),
	INDEX idx_tickets_created_by (created_by),
	INDEX idx_tickets_assigned_to_id (assigned_to_id),
	CONSTRAINT fk_tickets_assigned_to_id FOREIGN KEY (assigned_to_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS ticket_status_transitions (
	id BIGINT NOT NULL AUTO_INCREMENT,
	ticket_id BIGINT NOT NULL,
	from_status VARCHAR(20) NULL,
	to_status VARCHAR(20) NOT NULL,
	changed_by VARCHAR(100) NOT NULL,
	note VARCHAR(2000) NULL,
	changed_at TIMESTAMP(6) NOT NULL,
	PRIMARY KEY (id),
	INDEX idx_transitions_ticket_id_changed_at (ticket_id, changed_at),
	CONSTRAINT fk_transitions_ticket_id FOREIGN KEY (ticket_id) REFERENCES tickets (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

