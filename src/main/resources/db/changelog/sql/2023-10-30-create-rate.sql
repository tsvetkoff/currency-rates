CREATE TABLE IF NOT EXISTS public.rate (
  id        UUID                      PRIMARY KEY DEFAULT public.uuid_generate_v4(),
  bank      VARCHAR                   REFERENCES public.bank(id),
  currency  VARCHAR                   REFERENCES public.currency(id),
  date      TIMESTAMP WITH TIME ZONE,
  purchase  NUMERIC(20, 4),
  sale      NUMERIC(20, 4),
  UNIQUE (bank, currency, date)
);
