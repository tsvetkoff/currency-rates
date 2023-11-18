CREATE TABLE IF NOT EXISTS public.users (
    username VARCHAR NOT NULL  PRIMARY KEY,
    password VARCHAR NOT NULL,
    enabled  BOOLEAN NOT NULL
);
