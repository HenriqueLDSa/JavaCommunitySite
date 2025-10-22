-- //UP
-- Change vote_type (or value) column to integer
ALTER TABLE public.vote
ALTER COLUMN value TYPE integer USING value::integer;

-- //@UNDO
-- Revert column back to smallint if needed
ALTER TABLE public.vote
ALTER COLUMN value TYPE smallint USING value::smallint;
