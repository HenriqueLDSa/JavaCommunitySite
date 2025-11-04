-- // revert_is_hidden_to_is_deleted_on_post
ALTER TABLE public.post
RENAME COLUMN is_hidden TO is_deleted;

-- //@UNDO

ALTER TABLE public.post
RENAME COLUMN is_deleted TO is_hidden;