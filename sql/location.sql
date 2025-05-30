CREATE TABLE location
(
    code       BIGINT PRIMARY KEY,
    sido       TEXT,
    sigungu    TEXT,
    eupmyeon   TEXT,
    dongri     TEXT,
    created_at DATE,
    deleted_at DATE,
    hierarchy  INT NOT NULL
);

COPY location
    FROM {location.csv 파일 절대 경로}
    DELIMITER ','
    CSV HEADER
    ENCODING 'UTF8';