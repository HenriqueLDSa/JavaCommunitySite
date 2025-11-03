-- // create_full_admin_system
-- Creates tables for role-based access (superadmin, admin, user),
-- moderation-related records, and an automatic user-role assignment trigger.

-- ======================================
-- ========== MIGRATE UP ================
-- ======================================

-- ======================================
-- 1. Roles
-- ======================================

CREATE TABLE IF NOT EXISTS public.role (
    id SERIAL PRIMARY KEY,
    name TEXT UNIQUE NOT NULL,  -- 'superadmin', 'admin', 'user'
    description TEXT,
    CONSTRAINT role_name_check CHECK (name IN ('superadmin', 'admin', 'user'))
);

-- Maps users to roles (many-to-many relationship)
CREATE TABLE IF NOT EXISTS public.user_role (
    user_did TEXT NOT NULL REFERENCES public."user"(did) ON DELETE CASCADE,
    role_id INT NOT NULL REFERENCES public.role(id) ON DELETE CASCADE,
    PRIMARY KEY (user_did, role_id)
);

-- Insert base roles
INSERT INTO public.role (name, description) VALUES
    ('superadmin', 'Main system administrator with full privileges'),
    ('admin', 'Administrator with moderation and tag management rights'),
    ('user', 'Standard user role')
ON CONFLICT (name) DO NOTHING;

-- ======================================
-- 2. Hidden Content Tables
-- ======================================

-- Hidden users
CREATE TABLE IF NOT EXISTS public.hidden_user (
    aturi TEXT PRIMARY KEY,
    target_did TEXT UNIQUE NOT NULL REFERENCES public."user"(did) ON DELETE CASCADE,
    hidden_by TEXT NOT NULL REFERENCES public."user"(did) ON DELETE CASCADE,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL
);

-- Hidden posts
CREATE TABLE IF NOT EXISTS public.hidden_post (
    aturi TEXT PRIMARY KEY,
    post_aturi TEXT UNIQUE NOT NULL REFERENCES public.post(aturi) ON DELETE CASCADE,
    target_owner_did TEXT NOT NULL REFERENCES public."user"(did) ON DELETE CASCADE,
    hidden_by TEXT NOT NULL REFERENCES public."user"(did) ON DELETE CASCADE,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL
);

-- Hidden replies
CREATE TABLE IF NOT EXISTS public.hidden_reply (
    aturi TEXT PRIMARY KEY,
    reply_aturi TEXT UNIQUE NOT NULL REFERENCES public.reply(aturi) ON DELETE CASCADE,
    target_owner_did TEXT NOT NULL REFERENCES public."user"(did) ON DELETE CASCADE,
    hidden_by TEXT NOT NULL REFERENCES public."user"(did) ON DELETE CASCADE,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL
);

-- ======================================
-- 3. Tags Table
-- ======================================

CREATE TABLE IF NOT EXISTS public.tags (
    aturi TEXT PRIMARY KEY,
    tag_name TEXT UNIQUE NOT NULL,
    created_by TEXT NOT NULL REFERENCES public."user"(did) ON DELETE CASCADE,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL
);

-- Add index for fast tag lookups
CREATE INDEX IF NOT EXISTS idx_tags_tag_name ON public.tags (tag_name);

-- ======================================
-- 4. Superadmin Seeding
-- ======================================

-- Assign the hardcoded system superadmin DID to the superadmin role
INSERT INTO public.user_role (user_did, role_id)
SELECT 'did:plc:bwh2fxasbh3ieuxjyym7bmeh', id FROM public.role WHERE name = 'superadmin'
ON CONFLICT DO NOTHING;

-- ======================================
-- 5. Auto-Assign Default Role Trigger
-- ======================================

-- Create trigger function
CREATE OR REPLACE FUNCTION public.assign_default_user_role()
RETURNS TRIGGER AS $$
BEGIN
    -- Assign 'user' role to every new registered user
    INSERT INTO public.user_role (user_did, role_id)
    SELECT NEW.did, id FROM public.role WHERE name = 'user'
    ON CONFLICT DO NOTHING;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Attach trigger to "user" table
DROP TRIGGER IF EXISTS trg_assign_default_user_role ON public."user";
CREATE TRIGGER trg_assign_default_user_role
AFTER INSERT ON public."user"
FOR EACH ROW
EXECUTE FUNCTION public.assign_default_user_role();


-- //@UNDO
-- ======================================
-- ========== MIGRATE DOWN ==============
-- ======================================

-- Drop trigger and function first
DROP TRIGGER IF EXISTS trg_assign_default_user_role ON public."user" CASCADE;
DROP FUNCTION IF EXISTS public.assign_default_user_role() CASCADE;

-- Drop all tables in reverse order
DROP TABLE IF EXISTS public.tags CASCADE;
DROP TABLE IF EXISTS public.hidden_reply CASCADE;
DROP TABLE IF EXISTS public.hidden_post CASCADE;
DROP TABLE IF EXISTS public.hidden_user CASCADE;
DROP TABLE IF EXISTS public.user_role CASCADE;
DROP TABLE IF EXISTS public.role CASCADE;