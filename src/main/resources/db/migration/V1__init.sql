CREATE TABLE IF NOT EXISTS match (
    id SERIAL PRIMARY KEY,
    altId varchar NOT NULL UNIQUE,
    team1Player1 VARCHAR(50) NOT NULL,
    team1Player2 VARCHAR(50),
    team2Player1 VARCHAR(50) NOT NULL,
    team2Player2 VARCHAR(50),
    team1Score INT NOT NULL DEFAULT 0 CHECK (team1Score >= 0),
    team2Score INT NOT NULL DEFAULT 0 CHECK (team2Score >= 0),
    createdAt TIMESTAMP WITH TIME ZONE NOT NULL,
    endedAt TIMESTAMP WITH TIME ZONE
);

