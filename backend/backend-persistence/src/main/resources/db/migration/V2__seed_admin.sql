-- Admin: admin@bas.ru / admin
-- BCrypt hash for "admin"
INSERT INTO users (id, full_name, email, password_hash, role, created_at)
VALUES (gen_random_uuid(), 'Admin', 'admin@bas.ru',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', NOW());
