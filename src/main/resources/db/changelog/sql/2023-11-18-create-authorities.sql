CREATE TABLE IF NOT EXISTS public.authorities (
    username  VARCHAR NOT NULL  REFERENCES public.users(username),
    authority VARCHAR NOT NULL,
    PRIMARY KEY (username, authority)
);
