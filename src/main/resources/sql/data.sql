
INSERT INTO language (id, code, name, is_default) VALUES
(1, 'en', 'English', true),
(2, 'uk', 'Українська', false);
ALTER SEQUENCE user_id_seq MINVALUE 3;



INSERT INTO talk_translation(
	talk_id, language_id, name, description)
VALUES (?, ?, ?, ?);
