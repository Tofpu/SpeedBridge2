CREATE TABLE islands
(
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    slot           INT          NOT NULL,
    schematic_name VARCHAR(255) NOT NULL,
    x              INT          NOT NULL,
    y              INT          NOT NULL,
    z              INT          NOT NULL,
    yaw            FLOAT        NOT NULL,
    pitch          FLOAT        NOT NULL,
    PRIMARY KEY (id)
);