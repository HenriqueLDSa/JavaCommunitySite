ALTER TABLE public.vote
ADD COLUMN IF NOT EXISTS user_aturi text;

ALTER TABLE public.vote
ADD CONSTRAINT vote_user_fkey
FOREIGN KEY (user_aturi)
REFERENCES public."user"(aturi)
ON DELETE RESTRICT
ON UPDATE RESTRICT;

-- Add unique constraint to prevent duplicate votes per user per post
ALTER TABLE public.vote
ADD CONSTRAINT vote_user_post_unique
UNIQUE (user_aturi, root);

-- //@UNDO
ALTER TABLE public.vote
DROP CONSTRAINT IF EXISTS vote_user_post_unique;

ALTER TABLE public.vote
DROP CONSTRAINT IF EXISTS vote_user_fkey;

ALTER TABLE public.vote
DROP COLUMN IF EXISTS user_aturi;
