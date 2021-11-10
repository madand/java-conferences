
-- Clean-up the database
DROP TABLE IF EXISTS talk_speaker_request;
DROP TABLE IF EXISTS talk_speaker_proposal;
DROP TABLE IF EXISTS new_talk_proposal_lang;
DROP TABLE IF EXISTS new_talk_proposal;
DROP TABLE IF EXISTS talk_lang;
DROP TABLE IF EXISTS talk;
DROP TABLE IF EXISTS conference_attendee;
DROP TABLE IF EXISTS conference_lang;
DROP TABLE IF EXISTS conference;
DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS language;
DROP TYPE IF EXISTS user_role;

------------------------------------------------------------------------

CREATE TYPE user_role AS ENUM ('moderator', 'speaker', 'attendee');

CREATE TABLE "user" (
  id serial NOT NULL PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  email character varying(255) NOT NULL UNIQUE,
  real_name character varying(255) NOT NULL,
  password_hash character varying(255) NOT NULL,
  role user_role NOT NULL
);

CREATE TABLE language (
  id serial NOT NULL PRIMARY KEY,
  code character varying(6) NOT NULL,
  name character varying(20) NOT NULL,
  is_default boolean NOT NULL DEFAULT false
);

CREATE TABLE conference (
  id serial NOT NULL PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  event_date date NOT NULL,
  actually_attended_count integer
);

CREATE TABLE conference_lang (
  conference_id integer NOT NULL REFERENCES conference(id) ON UPDATE CASCADE ON DELETE CASCADE,
  language_id integer NOT NULL REFERENCES language(id) ON UPDATE CASCADE ON DELETE CASCADE,
  name character varying(255) NOT NULL,
  description text NOT NULL,
  location text NOT NULL,
  PRIMARY KEY (conference_id, language_id)
);

CREATE TABLE conference_attendee (
  id serial NOT NULL PRIMARY KEY,
  conference_id integer NOT NULL REFERENCES conference(id) ON UPDATE CASCADE ON DELETE CASCADE,
  user_id integer NOT NULL REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE CASCADE,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE conference_attendee
  IS 'Fact that the user will attend the conference.';

CREATE TABLE talk (
  id serial NOT NULL PRIMARY KEY,
  crated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  speaker_id integer REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE SET NULL,
  start_time time without time zone NOT NULL,
  end_time time without time zone NOT NULL,
  CHECK (start_time < end_time)
);
COMMENT ON TABLE conference_attendee
  IS 'A talk given at the conference.';

CREATE TABLE talk_lang (
    talk_id integer NOT NULL REFERENCES talk(id) ON UPDATE CASCADE ON DELETE CASCADE,
    language_id integer NOT NULL REFERENCES language(id) ON UPDATE CASCADE ON DELETE CASCADE,
    name character varying(255) NOT NULL,
    description text NOT NULL,
    PRIMARY KEY (talk_id, language_id)
);

CREATE TABLE talk_speaker_request (
  id serial NOT NULL PRIMARY KEY,
  crated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  talk_id integer NOT NULL REFERENCES talk(id) ON UPDATE CASCADE ON DELETE CASCADE,
  speaker_id integer NOT NULL REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE CASCADE,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE talk_speaker_request
  IS 'Speaker''s request to hold the talk. Should be approved by the moderator.';

CREATE TABLE talk_speaker_proposal (
  id serial NOT NULL PRIMARY KEY,
  crated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  talk_id integer NOT NULL REFERENCES talk(id) ON UPDATE CASCADE ON DELETE CASCADE,
  speaker_id integer NOT NULL REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE CASCADE,
  moderator_id integer NOT NULL REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE CASCADE,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE talk_speaker_proposal
  IS 'Moderator''s proposal to the speaker to hold the talk. Should be accepted by the speaker.';

CREATE TABLE new_talk_proposal (
  id serial NOT NULL PRIMARY KEY,
  crated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  conference_id integer NOT NULL REFERENCES conference(id) ON UPDATE CASCADE ON DELETE CASCADE,
  speaker_id integer NOT NULL REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE SET NULL,
  duration integer NOT NULL
);
COMMENT ON TABLE new_talk_proposal
  IS 'New talk proposed by the speaker. Should be accepted by a moderator.';

CREATE TABLE new_talk_proposal_lang (
  talk_proposal_id integer NOT NULL REFERENCES new_talk_proposal(id) ON UPDATE CASCADE ON DELETE CASCADE,
  language_id integer NOT NULL REFERENCES language(id) ON UPDATE CASCADE ON DELETE CASCADE,
  name character varying(255) NOT NULL,
  description text NOT NULL,
  PRIMARY KEY (talk_proposal_id, language_id)
);
