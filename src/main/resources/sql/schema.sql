
-- Clean-up the database
DROP VIEW IF EXISTS v_talk_speaker_proposal;
DROP VIEW IF EXISTS v_talk_speaker_request;
DROP VIEW IF EXISTS v_conference;
DROP VIEW IF EXISTS v_talk;
DROP VIEW IF EXISTS v_new_talk_proposal;

DROP TRIGGER IF EXISTS compute_end_time ON talk;
DROP FUNCTION IF EXISTS compute_end_time;

DROP FUNCTION IF EXISTS ensure_translated(TEXT, INTEGER, TEXT, TEXT);
DROP FUNCTION IF EXISTS get_default_language_id();
DROP TABLE IF EXISTS talk_speaker_request;
DROP TABLE IF EXISTS talk_speaker_proposal;
DROP TABLE IF EXISTS new_talk_proposal_translation;
DROP TABLE IF EXISTS new_talk_proposal;
DROP TABLE IF EXISTS talk_translation;
DROP TABLE IF EXISTS talk;
DROP TABLE IF EXISTS conference_attendee;
DROP TABLE IF EXISTS conference_translation;
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
  name CHARACTER VARYING(50) NOT NULL,
  is_default BOOLEAN NOT NULL DEFAULT false
);
COMMENT ON TABLE language
  IS 'The application language.';

CREATE TABLE conference (
  id SERIAL NOT NULL PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  event_date DATE NOT NULL,
  actually_attended_count INTEGER NOT NULL DEFAULT 0
);
COMMENT ON TABLE conference
  IS 'The conference.';

CREATE TABLE conference_translation (
  language_id INTEGER NOT NULL REFERENCES language(id) ON UPDATE CASCADE ON DELETE CASCADE,
  conference_id INTEGER NOT NULL REFERENCES conference(id) ON UPDATE CASCADE ON DELETE CASCADE,
  name CHARACTER VARYING(255) NOT NULL,
  description TEXT NOT NULL,
  location TEXT NOT NULL,
  PRIMARY KEY (language_id, conference_id)
);
COMMENT ON TABLE conference_translation
  IS 'Localization data for the "conference" table.';

CREATE TABLE conference_attendee (
  conference_id INTEGER NOT NULL REFERENCES conference(id) ON UPDATE CASCADE ON DELETE CASCADE,
  user_id INTEGER NOT NULL REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE CASCADE,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (conference_id, user_id)
);
COMMENT ON TABLE conference_attendee
  IS 'A fact that the user will attend the conference.';

CREATE TABLE talk (
  id SERIAL NOT NULL PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  conference_id INTEGER NOT NULL REFERENCES conference(id) ON UPDATE CASCADE ON DELETE CASCADE,
  speaker_id INTEGER REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE SET NULL,
  start_time TIME WITHOUT TIME ZONE NOT NULL,
  duration INTEGER NOT NULL CHECK (duration > 0),
  end_time TIME WITHOUT TIME ZONE NOT NULL,
  UNIQUE (conference_id, start_time)
);
COMMENT ON TABLE talk
  IS 'A talk given at the conference.';

CREATE TABLE talk_translation (
    language_id INTEGER NOT NULL REFERENCES language(id) ON UPDATE CASCADE ON DELETE CASCADE,
    talk_id INTEGER NOT NULL REFERENCES talk(id) ON UPDATE CASCADE ON DELETE CASCADE,
    name CHARACTER VARYING(255) NOT NULL,
    description TEXT NOT NULL,
    PRIMARY KEY (language_id, talk_id)
);
COMMENT ON TABLE talk_translation
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
  speaker_id INTEGER NOT NULL REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE CASCADE,
  duration INTEGER NOT NULL CHECK (duration > 0)
);
COMMENT ON TABLE new_talk_proposal
  IS 'A new talk proposed by the speaker. Should be accepted by a moderator.';

CREATE TABLE new_talk_proposal_translation (
  language_id INTEGER NOT NULL REFERENCES language(id) ON UPDATE CASCADE ON DELETE CASCADE,
  new_talk_proposal_id INTEGER NOT NULL REFERENCES new_talk_proposal(id) ON UPDATE CASCADE ON DELETE CASCADE,
  name CHARACTER VARYING(255) NOT NULL,
  description TEXT NOT NULL,
  PRIMARY KEY (language_id, new_talk_proposal_id)
);
COMMENT ON TABLE new_talk_proposal_translation
  IS 'Localization data for the "new_talk_proposal" table.';

CREATE OR REPLACE FUNCTION get_default_language_id()
    RETURNS INTEGER
    LANGUAGE 'sql'
  STABLE
AS $BODY$
  SELECT id FROM language WHERE is_default = true
  $BODY$;

  CREATE OR REPLACE FUNCTION ensure_translated(
    translated_value TEXT,
	  id INTEGER,
	  column_name TEXT,
	  base_name TEXT)
    RETURNS TEXT
    LANGUAGE 'plpgsql'
    STABLE
  AS $BODY$
    DECLARE
    ret TEXT;
  BEGIN
    IF translated_value <> '' THEN
      RETURN translated_value;
    END IF;

    EXECUTE format('SELECT %I FROM %I WHERE %I = $1'
                   ' AND language_id = get_default_language_id()',
                   column_name,
                   base_name || '_translation',
                   base_name || '_id')
      INTO ret USING id;
    RETURN ret;
  END
  $BODY$;

  CREATE OR REPLACE FUNCTION compute_end_time()
    RETURNS trigger
    LANGUAGE 'plpgsql' STABLE
  AS $BODY$
  BEGIN
    NEW.end_time := NEW.start_time + format('PT00:%s:00', NEW.duration)::INTERVAL;
    RETURN NEW;
  END
  $BODY$;

  CREATE TRIGGER compute_end_time
    BEFORE INSERT OR UPDATE ON talk
    FOR EACH ROW EXECUTE FUNCTION compute_end_time();

CREATE VIEW v_conference
    AS
    SELECT t.id,
           t.created_at,
           t.updated_at,
           t.event_date,
           t.actually_attended_count,
           l.language_id,
           ensure_translated(l.name, t.id, 'name', 'conference') as name,
           ensure_translated(l.description, t.id, 'description', 'conference') as description,
           ensure_translated(l.location, t.id, 'location', 'conference') as location
      FROM conference t
           JOIN conference_translation l ON l.conference_id = t.id;
  COMMENT ON VIEW v_conference
    IS 'View that joins conference and its translation.';

CREATE VIEW v_talk
    AS
    SELECT t.id,
           t.created_at,
           t.updated_at,
           t.conference_id,
           t.speaker_id,
           t.start_time,
           t.duration,
           t.end_time,
           l.language_id,
           ensure_translated(l.name, t.id, 'name', 'talk') as name,
           ensure_translated(l.description, t.id, 'description', 'talk') as description
      FROM talk t
           JOIN talk_translation l ON l.talk_id = t.id;
  COMMENT ON VIEW v_talk
    IS 'View that joins talk and its translation.';

CREATE VIEW v_new_talk_proposal
    AS
    SELECT t.id,
           t.created_at,
           t.updated_at,
           t.conference_id,
           t.speaker_id,
           t.duration,
           l.language_id,
           ensure_translated(l.name, t.id, 'name', 'new_talk_proposal') as name,
           ensure_translated(l.description, t.id, 'description', 'new_talk_proposal') as description
      FROM new_talk_proposal t
           JOIN new_talk_proposal_translation l ON l.new_talk_proposal_id = t.id;
  COMMENT ON VIEW v_new_talk_proposal
    IS 'View that joins new_talk_proposal and its translation.';


DROP VIEW v_talk_speaker_request;
CREATE VIEW v_talk_speaker_request
  AS
  SELECT t.id,
         t.created_at,
         t.talk_id,
         t.speaker_id,
         vt.language_id,
         vt.name as talk_name,
         vc.name as conference_name,
         us.real_name as speaker_name,
         us.email as speaker_email
    FROM talk_speaker_request t
         JOIN "user" us ON us.id = t.speaker_id
         JOIN v_talk vt ON vt.id = t.talk_id
         JOIN v_conference vc ON (vc.id = vt.conference_id AND vt.language_id = vc.language_id);
COMMENT ON VIEW v_new_talk_proposal
  IS 'View that joins talk_speaker_request with translated talk and conference.';

DROP VIEW IF EXISTS v_talk_speaker_proposal;
CREATE VIEW v_talk_speaker_proposal
  AS
  SELECT t.id,
         t.created_at,
         t.talk_id,
         t.speaker_id,
         t.moderator_id,
         vt.language_id,
         vt.name as talk_name,
         vc.name as conference_name,
         us.real_name as speaker_name,
         us.email as speaker_email,
         um.real_name as moderator_name,
         um.email as moderator_email
    FROM talk_speaker_proposal t
         JOIN "user" us ON us.id = t.speaker_id
         JOIN "user" um ON um.id = t.moderator_id
         JOIN v_talk vt ON vt.id = t.talk_id
         JOIN v_conference vc ON (vc.id = vt.conference_id AND vt.language_id = vc.language_id);
COMMENT ON VIEW v_new_talk_proposal
  IS 'View that joins talk_speaker_proposal with translated talk, conference and user.';
