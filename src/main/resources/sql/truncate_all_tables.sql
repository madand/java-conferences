-- Truncating roots, cascading should empty all tables.
-- RESTART IDENTITY recursively resets associated sequences.
TRUNCATE TABLE conference RESTART IDENTITY CASCADE;
TRUNCATE TABLE "user" RESTART IDENTITY CASCADE;
TRUNCATE TABLE language RESTART IDENTITY CASCADE;
