-- public.category definition

-- Drop table

-- DROP TABLE public.category;

CREATE TABLE public.category (
	id uuid DEFAULT uuid_generate_v4() NOT NULL,
	"name" text NOT NULL,
	CONSTRAINT category_name_key UNIQUE (name),
	CONSTRAINT category_pkey PRIMARY KEY (id)
);


-- public.community definition

-- Drop table

-- DROP TABLE public.community;

CREATE TABLE public.community (
	id uuid DEFAULT uuid_generate_v4() NOT NULL,
	"name" text NOT NULL,
	description text DEFAULT ''::text NOT NULL,
	created_at timestamptz DEFAULT now() NULL,
	updated_at timestamptz DEFAULT now() NULL,
	CONSTRAINT community_name_key UNIQUE (name),
	CONSTRAINT community_pkey PRIMARY KEY (id)
);


-- public."user" definition

-- Drop table

-- DROP TABLE public."user";

CREATE TABLE public."user" (
	id uuid DEFAULT uuid_generate_v4() NOT NULL,
	username text NOT NULL,
	first_name text NOT NULL,
	last_name text NOT NULL,
	email text NOT NULL,
	password_hash text NOT NULL,
	created_at timestamptz DEFAULT now() NULL,
	updated_at timestamptz DEFAULT now() NULL,
	CONSTRAINT users_email_key UNIQUE (email),
	CONSTRAINT users_pkey PRIMARY KEY (id),
	CONSTRAINT users_username_key UNIQUE (username)
);


-- public.post definition

-- Drop table

-- DROP TABLE public.post;

CREATE TABLE public.post (
	id uuid DEFAULT uuid_generate_v4() NOT NULL,
	user_id uuid NULL,
	community_id uuid NULL,
	title text NOT NULL,
	"content" text NOT NULL,
	created_at timestamptz DEFAULT now() NULL,
	updated_at timestamptz DEFAULT now() NULL,
	tags jsonb DEFAULT '[]'::jsonb NOT NULL,
	is_deleted BOOLEAN DEFAULT FALSE,
	category_id uuid NULL,
	CONSTRAINT posts_pkey PRIMARY KEY (id),
	CONSTRAINT post_category_id_fkey FOREIGN KEY (category_id) REFERENCES public.category(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT post_user_id_fkey FOREIGN KEY (user_id) REFERENCES public."user"(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT posts_community_id_fkey FOREIGN KEY (community_id) REFERENCES public.community(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX idx_post_tags ON public.post USING gin (tags);


-- public.vote definition

-- Drop table

-- DROP TABLE public.vote;

CREATE TABLE public.vote (
	id uuid DEFAULT uuid_generate_v4() NOT NULL,
	user_id uuid NULL,
	post_id uuid NULL,
	vote_type int2 NULL,
	CONSTRAINT vote_pkey PRIMARY KEY (id),
	CONSTRAINT vote_user_id_post_id_key UNIQUE (user_id, post_id),
	CONSTRAINT vote_vote_type_check CHECK ((vote_type = ANY (ARRAY['-1'::integer, 1]))),
	CONSTRAINT vote_post_id_fkey FOREIGN KEY (post_id) REFERENCES public.post(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT vote_user_id_fkey FOREIGN KEY (user_id) REFERENCES public."user"(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);


-- public."comment" definition

-- Drop table

-- DROP TABLE public."comment";

CREATE TABLE public."comment" (
	id uuid DEFAULT uuid_generate_v4() NOT NULL,
	user_id uuid NULL,
	post_id uuid NULL,
	"content" text NOT NULL,
	created_at timestamptz DEFAULT now() NULL,
	updated_at timestamptz DEFAULT now() NULL,
	CONSTRAINT comments_pkey PRIMARY KEY (id),
	CONSTRAINT comment_post_id_fkey FOREIGN KEY (post_id) REFERENCES public.post(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT comment_user_id_fkey FOREIGN KEY (user_id) REFERENCES public."user"(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);


-- public.notification_history definition

-- Drop table

-- DROP TABLE public.notification_history;

CREATE TABLE public.notification_history (
	id uuid DEFAULT uuid_generate_v4() NOT NULL,
	recipient_user_id uuid NOT NULL,
	triggering_user_id uuid NOT NULL,
	post_id uuid NOT NULL,
	comment_id uuid NULL,
	"type" public."notification_type" NOT NULL,
	created_at timestamptz DEFAULT now() NULL,
	read_at timestamptz NULL,
	CONSTRAINT notification_history_pkey PRIMARY KEY (id),
	CONSTRAINT notification_history_recipient_user_id_triggering_user_id_p_key UNIQUE (recipient_user_id, triggering_user_id, post_id, comment_id),
	CONSTRAINT notification_history_comment_id_fkey FOREIGN KEY (comment_id) REFERENCES public."comment"(id) ON DELETE CASCADE,
	CONSTRAINT notification_history_post_id_fkey FOREIGN KEY (post_id) REFERENCES public.post(id) ON DELETE CASCADE,
	CONSTRAINT notification_history_recipient_user_id_fkey FOREIGN KEY (recipient_user_id) REFERENCES public."user"(id) ON DELETE CASCADE,
	CONSTRAINT notification_history_triggering_user_id_fkey FOREIGN KEY (triggering_user_id) REFERENCES public."user"(id) ON DELETE CASCADE
);
CREATE INDEX idx_notification_history_recipient_user_id ON public.notification_history USING btree (recipient_user_id);