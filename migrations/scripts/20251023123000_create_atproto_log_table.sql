-- //UP
CREATE EXTENSION IF NOT EXISTS pgcrypto;


CREATE TABLE public.atproto_log (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    aturi TEXT,
    collection_type TEXT,
    record_key TEXT,
    operation TEXT NOT NULL,
    record JSONB,
    attempts INTEGER NOT NULL DEFAULT 0,
    max_attempts INTEGER NOT NULL DEFAULT 10,
    last_error TEXT,
    processed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_atproto_log_pending ON public.atproto_log(created_at) WHERE processed = false;
CREATE INDEX IF NOT EXISTS idx_atproto_log_aturi ON public.atproto_log(aturi);

-- //@UNDO
DROP INDEX IF EXISTS idx_atproto_log_aturi;
DROP INDEX IF EXISTS idx_atproto_log_pending;
DROP TABLE IF EXISTS public.atproto_log;
