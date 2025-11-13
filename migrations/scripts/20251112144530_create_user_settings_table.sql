-- // create_user_settings_table

CREATE TABLE IF NOT EXISTS public.user_settings (
    user_did TEXT PRIMARY KEY
        REFERENCES public."user"(did)
        ON DELETE CASCADE,
    theme_preference TEXT DEFAULT 'light'
        CHECK (theme_preference IN ('light', 'dark')),
    created_at timestamptz DEFAULT now() NOT NULL,
    updated_at timestamptz DEFAULT now() NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_user_settings_user_did
    ON public.user_settings (user_did);

-- //@UNDO
DROP INDEX IF EXISTS idx_user_settings_user_did;
DROP TABLE IF EXISTS public.user_settings;
