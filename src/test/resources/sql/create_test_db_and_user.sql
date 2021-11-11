CREATE USER conferences_test PASSWORD 'conferences_test';

CREATE DATABASE conferences_test
  WITH OWNER = conferences_test
  template = template0
  ENCODING = 'utf8'
  LC_COLLATE = 'uk_UA.utf8'
  LC_CTYPE = 'uk_UA.utf8';
