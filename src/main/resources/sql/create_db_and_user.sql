CREATE USER conferences PASSWORD 'conferences';

CREATE DATABASE conferences
  WITH OWNER = conferences
  template = template0
  ENCODING = 'utf8'
  LC_COLLATE = 'uk_UA.utf8'
  LC_CTYPE = 'uk_UA.utf8';