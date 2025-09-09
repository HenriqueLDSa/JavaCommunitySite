CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE "user" (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username TEXT UNIQUE NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE community (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE category (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT UNIQUE NOT NULL
);

CREATE TABLE "post" (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES "user"(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    community_id UUID REFERENCES community(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    tags JSONB NOT NULL DEFAULT '[]'::jsonb,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "comment" (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES "user"(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    post_id UUID REFERENCES "post"(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE post_category (
    post_id UUID REFERENCES "post"(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    category_id UUID REFERENCES category(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    PRIMARY KEY (post_id, category_id)
);

CREATE TABLE vote (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES "user"(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    post_id UUID REFERENCES "post"(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    vote_type SMALLINT CHECK (vote_type IN (-1, 1)),
    UNIQUE (user_id, post_id)
);

-- Create a new ENUM type to manage different kinds of notifications
CREATE TYPE notification_type AS ENUM ('NEW_COMMENT', 'NEW_VOTE', 'USER_MENTION');

-- Create the notification_history table
CREATE TABLE notification_history (
    -- A unique ID for each notification event
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    recipient_user_id UUID NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    triggering_user_id UUID NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    post_id UUID NOT NULL REFERENCES "post"(id) ON DELETE CASCADE,
    comment_id UUID REFERENCES "comment"(id) ON DELETE CASCADE,
    type notification_type NOT NULL,
    read_at TIMESTAMPTZ,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (recipient_user_id, triggering_user_id, post_id, comment_id)
);

-- Create an index for quickly fetching all notifications for a specific user
CREATE INDEX idx_notification_history_recipient_user_id ON notification_history(recipient_user_id);

CREATE INDEX idx_post_tags ON "post" USING GIN (tags);
