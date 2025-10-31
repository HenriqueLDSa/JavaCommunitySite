ALTER TABLE public."user" ADD COLUMN IF NOT EXISTS avatar TEXT;
ALTER TABLE public."user" ADD COLUMN IF NOT EXISTS description TEXT;

-- //@UNDO
ALTER TABLE public."user" DROP COLUMN IF EXISTS description;
ALTER TABLE public."user" DROP COLUMN IF EXISTS avatar;