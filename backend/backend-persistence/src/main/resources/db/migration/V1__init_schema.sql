CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE users (
    id UUID PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL CHECK (role IN ('USER', 'ADMIN')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE routes (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    status VARCHAR(20) NOT NULL CHECK (status IN ('SUBMITTED', 'APPROVED', 'REJECTED')),
    auto_check_result BOOLEAN NOT NULL
);

CREATE INDEX idx_routes_user_id ON routes(user_id);

CREATE TABLE route_points (
    id BIGSERIAL PRIMARY KEY,
    route_id UUID NOT NULL REFERENCES routes(id) ON DELETE CASCADE,
    order_index INT NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL
);

CREATE INDEX idx_route_points_route_id ON route_points(route_id);

CREATE TABLE verification_zones (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    geometry GEOMETRY(Polygon, 4326) NOT NULL,
    created_by UUID NOT NULL REFERENCES users(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_verification_zones_created_by ON verification_zones(created_by);
CREATE INDEX idx_verification_zones_geometry ON verification_zones USING GIST(geometry);
