CREATE TABLE IF NOT EXISTS islands
(
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    slot           INT          NOT NULL,
    schematic_name VARCHAR(255) NOT NULL,
    x              DOUBLE          NOT NULL,
    y              DOUBLE          NOT NULL,
    z              DOUBLE          NOT NULL,
    yaw            FLOAT        NOT NULL,
    pitch          FLOAT        NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS scores (
    player_id UUID NOT NULL,
    slot INT NOT NULL,
    time DOUBLE NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS blocks (
    player_id UUID NOT NULL,
    block_id VARCHAR(255) NOT NULL
)