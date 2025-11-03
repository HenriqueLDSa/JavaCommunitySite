-- // add_reason_to_hidden_tables

-- ======================================
-- ========== MIGRATE UP ================
-- ======================================

ALTER TABLE public.hidden_user
ADD COLUMN reason TEXT NULL;

ALTER TABLE public.hidden_post
ADD COLUMN reason TEXT NULL;

ALTER TABLE public.hidden_reply
ADD COLUMN reason TEXT NULL;

-- //@UNDO
-- ======================================
-- ========== MIGRATE DOWN ==============
-- ======================================

ALTER TABLE public.hidden_reply
DROP COLUMN IF EXISTS reason;

ALTER TABLE public.hidden_post
DROP COLUMN IF EXISTS reason;

ALTER TABLE public.hidden_user
DROP COLUMN IF EXISTS reason;