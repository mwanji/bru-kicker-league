CREATE TABLE IF NOT EXISTS award (
    id SERIAL PRIMARY KEY,
    playerName varchar NOT NULL,
    awardType VARCHAR(50) NOT NULL,
    startedAt date,
    endedAt date
);

