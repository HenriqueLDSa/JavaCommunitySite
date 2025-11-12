-- // Description: Set default UUID for notification IDs
ALTER TABLE public.notification
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

-- // @UNDO

ALTER TABLE public.notification
    ALTER COLUMN id DROP DEFAULT;