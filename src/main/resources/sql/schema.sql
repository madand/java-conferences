
-- Clean-up the database
DROP TABLE IF EXISTS talk_speaker_request;
DROP TABLE IF EXISTS talk_speaker_proposal;
DROP TABLE IF EXISTS new_talk_proposal_l10n;
DROP TABLE IF EXISTS new_talk_proposal;
DROP TABLE IF EXISTS talk_l10n;
DROP TABLE IF EXISTS talk;
DROP TABLE IF EXISTS conference_attendee;
DROP TABLE IF EXISTS conference_l10n;
DROP TABLE IF EXISTS conference;
DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS language;
DROP TYPE IF EXISTS role_type;

------------------------------------------------------------------------

CREATE TYPE role_type AS ENUM ('MODERATOR', 'SPEAKER', 'ATTENDEE');

CREATE TABLE "user" (
  id SERIAL NOT NULL PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  email CHARACTER VARYING(255) NOT NULL UNIQUE,
  real_name CHARACTER VARYING(255) NOT NULL,
  password_hash CHARACTER VARYING(255) NOT NULL,
  role role_type NOT NULL
);
COMMENT ON TABLE "user"
  IS 'The application user.';

CREATE TABLE language (
  id SERIAL NOT NULL PRIMARY KEY,
  code CHARACTER VARYING(6) NOT NULL UNIQUE,
  name CHARACTER VARYING(20) NOT NULL,
  is_default BOOLEAN NOT NULL DEFAULT false
);
COMMENT ON TABLE language
  IS 'The application language.';

CREATE TABLE conference (
  id SERIAL NOT NULL PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  event_date DATE NOT NULL,
  language_id INTEGER NOT NULL REFERENCES language(id) ON UPDATE CASCADE ON DELETE RESTRICT,
  actually_attended_count INTEGER
);
COMMENT ON TABLE conference
  IS 'The conference.';

CREATE TABLE conference_l10n (
  conference_id INTEGER NOT NULL REFERENCES conference(id) ON UPDATE CASCADE ON DELETE CASCADE,
  language_id INTEGER NOT NULL REFERENCES language(id) ON UPDATE CASCADE ON DELETE RESTRICT,
  name CHARACTER VARYING(255) NOT NULL,
  description TEXT NOT NULL,
  location TEXT NOT NULL,
  PRIMARY KEY (conference_id, language_id)
);
COMMENT ON TABLE conference_l10n
  IS 'Localization data for the "conference_l10n" table.';

CREATE TABLE conference_attendee (
  id SERIAL NOT NULL PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  conference_id INTEGER NOT NULL REFERENCES conference(id) ON UPDATE CASCADE ON DELETE CASCADE,
  user_id INTEGER NOT NULL REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE CASCADE,
  UNIQUE (conference_id, user_id)
);
COMMENT ON TABLE conference_attendee
  IS 'Fact that the user will attend the conference.';

CREATE TABLE talk (
  id SERIAL NOT NULL PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  conference_id INTEGER NOT NULL REFERENCES conference(id) ON UPDATE CASCADE ON DELETE CASCADE,
  speaker_id INTEGER REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE SET NULL,
  start_time time without time zone NOT NULL,
  end_time time without time zone NOT NULL,
  UNIQUE (conference_id, start_time),
  CHECK (start_time < end_time)
);
COMMENT ON TABLE talk
  IS 'A talk given at the conference.';

CREATE TABLE talk_l10n (
    talk_id INTEGER NOT NULL REFERENCES talk(id) ON UPDATE CASCADE ON DELETE CASCADE,
    language_id INTEGER NOT NULL REFERENCES language(id) ON UPDATE CASCADE ON DELETE RESTRICT,
    name CHARACTER VARYING(255) NOT NULL,
    description TEXT NOT NULL,
    PRIMARY KEY (talk_id, language_id)
);
COMMENT ON TABLE talk_l10n
  IS 'Localization data for the "talk" table.';

CREATE TABLE talk_speaker_request (
  id SERIAL NOT NULL PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  talk_id INTEGER NOT NULL REFERENCES talk(id) ON UPDATE CASCADE ON DELETE CASCADE,
  speaker_id INTEGER NOT NULL REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE CASCADE,
  UNIQUE (talk_id, speaker_id)
);
COMMENT ON TABLE talk_speaker_request
  IS 'Speaker''s request to hold the talk. Should be approved by the moderator.';

CREATE TABLE talk_speaker_proposal (
  id SERIAL NOT NULL PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  talk_id INTEGER NOT NULL REFERENCES talk(id) ON UPDATE CASCADE ON DELETE CASCADE,
  speaker_id INTEGER NOT NULL REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE CASCADE,
  moderator_id INTEGER NOT NULL REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE CASCADE,
  UNIQUE (talk_id, speaker_id)
);
COMMENT ON TABLE talk_speaker_proposal
  IS 'Moderator''s proposal to the speaker to hold the talk. Should be accepted by the speaker.';

CREATE TABLE new_talk_proposal (
  id SERIAL NOT NULL PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  conference_id INTEGER NOT NULL REFERENCES conference(id) ON UPDATE CASCADE ON DELETE CASCADE,
  speaker_id INTEGER NOT NULL REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE SET NULL,
  duration INTEGER NOT NULL
);
COMMENT ON TABLE new_talk_proposal
  IS 'New talk proposed by the speaker. Should be accepted by a moderator.';

CREATE TABLE new_talk_proposal_l10n (
  talk_proposal_id INTEGER NOT NULL REFERENCES new_talk_proposal(id) ON UPDATE CASCADE ON DELETE CASCADE,
  language_id INTEGER NOT NULL REFERENCES language(id) ON UPDATE CASCADE ON DELETE RESTRICT,
  name CHARACTER VARYING(255) NOT NULL,
  description TEXT NOT NULL,
  PRIMARY KEY (talk_proposal_id, language_id)
);
COMMENT ON TABLE new_talk_proposal_l10n
  IS 'Localization data for the "new_talk_proposal_l10n" table.';
