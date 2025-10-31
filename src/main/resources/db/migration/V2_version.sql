-- public.changelog definition

-- Drop table

-- DROP TABLE public.changelog;

CREATE TABLE public.changelog (
	id numeric(20) NOT NULL,
	applied_at varchar(25) NOT NULL,
	description varchar(255) NOT NULL,
	CONSTRAINT pk_changelog PRIMARY KEY (id)
);


-- public.community definition

-- Drop table

-- DROP TABLE public.community;

CREATE TABLE public.community (
	"name" text NOT NULL,
	description text DEFAULT ''::text NOT NULL,
	created_at timestamptz DEFAULT now() NULL,
	updated_at timestamptz DEFAULT now() NULL,
	aturi text DEFAULT 'at://community/temp'::text NOT NULL,
	CONSTRAINT community_name_key UNIQUE (name),
	CONSTRAINT community_pkey PRIMARY KEY (aturi)
);


-- public."group" definition

-- Drop table

-- DROP TABLE public."group";

CREATE TABLE public."group" (
	"name" text NOT NULL,
	description text DEFAULT ''::text NULL,
	aturi text DEFAULT 'at://group/temp'::text NOT NULL,
	CONSTRAINT group_pkey PRIMARY KEY (aturi)
);


-- public."user" definition

-- Drop table

-- DROP TABLE public."user";

CREATE TABLE public."user" (
	handle text NOT NULL,
	first_name text NOT NULL,
	last_name text NOT NULL,
	email text NOT NULL,
	password_hash text NOT NULL,
	created_at timestamptz DEFAULT now() NULL,
	updated_at timestamptz DEFAULT now() NULL,
	aturi text DEFAULT 'at://user/temp'::text NOT NULL,
	avatar text NULL,
	description text NULL,
	display_name text NULL,
	CONSTRAINT user_pkey PRIMARY KEY (aturi),
	CONSTRAINT users_email_key UNIQUE (email),
	CONSTRAINT users_handle_key UNIQUE (handle)
);


-- public.category definition

-- Drop table

-- DROP TABLE public.category;

CREATE TABLE public.category (
	"name" text NOT NULL,
	aturi text DEFAULT 'at://category/temp'::text NOT NULL,
	"group" text NULL,
	category_type text NULL,
	description text NULL,
	CONSTRAINT category_pkey PRIMARY KEY (aturi),
	CONSTRAINT category_group_fkey FOREIGN KEY ("group") REFERENCES public."group"(aturi) ON DELETE RESTRICT ON UPDATE RESTRICT
);


-- public.post definition

-- Drop table

-- DROP TABLE public.post;

CREATE TABLE public.post (
	title text NOT NULL,
	"content" text NOT NULL,
	created_at timestamptz DEFAULT now() NULL,
	updated_at timestamptz DEFAULT now() NULL,
	tags jsonb DEFAULT '[]'::jsonb NOT NULL,
	category_aturi text NULL,
	aturi text DEFAULT 'at://post/temp'::text NOT NULL,
	forum text DEFAULT ''::text NOT NULL,
	solution text NULL,
	is_deleted bool DEFAULT false NULL,
	CONSTRAINT post_pkey PRIMARY KEY (aturi),
	CONSTRAINT post_category_fkey FOREIGN KEY (category_aturi) REFERENCES public.category(aturi) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX idx_post_tags ON public.post USING gin (tags);


-- public.reply definition

-- Drop table

-- DROP TABLE public.reply;

CREATE TABLE public.reply (
	root text NULL,
	"content" text NOT NULL,
	created_at timestamptz DEFAULT now() NULL,
	updated_at timestamptz DEFAULT now() NULL,
	aturi text DEFAULT 'at://reply/temp'::text NOT NULL,
	CONSTRAINT reply_pkey PRIMARY KEY (aturi),
	CONSTRAINT reply_root_fkey FOREIGN KEY (root) REFERENCES public.post(aturi) ON DELETE RESTRICT ON UPDATE RESTRICT
);


-- public.vote definition

-- Drop table

-- DROP TABLE public.vote;

CREATE TABLE public.vote (
	root text NULL,
	value int4 NULL,
	aturi text DEFAULT 'at://vote/temp'::text NOT NULL,
	created_at timestamptz DEFAULT now() NULL,
	user_aturi text NULL,
	CONSTRAINT vote_pkey PRIMARY KEY (aturi),
	CONSTRAINT vote_user_post_unique UNIQUE (user_aturi, root),
	CONSTRAINT vote_value_check CHECK ((value = ANY (ARRAY['-1'::integer, 1]))),
	CONSTRAINT vote_root_fkey FOREIGN KEY (root) REFERENCES public.post(aturi) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT vote_user_fkey FOREIGN KEY (user_aturi) REFERENCES public."user"(aturi) ON DELETE RESTRICT ON UPDATE RESTRICT
);


-- public.notification definition

-- Drop table

-- DROP TABLE public.notification;

CREATE TABLE public.notification (
	recipient_user_aturi text NOT NULL,
	triggering_user_aturi text NOT NULL,
	post_aturi text NOT NULL,
	reply_aturi text NULL,
	"type" public."notification_type" NOT NULL,
	created_at timestamptz DEFAULT now() NULL,
	read_at timestamptz NULL,
	aturi text DEFAULT 'at://notification/temp'::text NOT NULL,
	CONSTRAINT notification_pkey PRIMARY KEY (aturi),
	CONSTRAINT notification_post_fkey FOREIGN KEY (post_aturi) REFERENCES public.post(aturi) ON DELETE CASCADE,
	CONSTRAINT notification_recipient_user_fkey FOREIGN KEY (recipient_user_aturi) REFERENCES public."user"(aturi) ON DELETE CASCADE,
	CONSTRAINT notification_reply_fkey FOREIGN KEY (reply_aturi) REFERENCES public.reply(aturi) ON DELETE CASCADE,
	CONSTRAINT notification_triggering_user_fkey FOREIGN KEY (triggering_user_aturi) REFERENCES public."user"(aturi) ON DELETE CASCADE
);
CREATE INDEX idx_notification_recipient_user_aturi ON public.notification USING btree (recipient_user_aturi);