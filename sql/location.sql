CREATE TABLE location (
                          code BIGINT PRIMARY KEY,
                          name TEXT NOT NULL,
                          created_at DATE,
                          deleted_at DATE,
                          hierarchy INT NOT NULL,
                          parent_code BIGINT,
                          FOREIGN KEY (parent_code) REFERENCES location(code)
);


COPY location(code, name, created_at, deleted_at, hierarchy, parent_code)
    FROM 'location.csv 경로'
    DELIMITER ','
    CSV HEADER
    ENCODING 'UTF8';