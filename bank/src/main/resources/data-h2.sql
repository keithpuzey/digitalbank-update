-- Create sequence tables if they don't exist
CREATE TABLE IF NOT EXISTS account_number_seq (next_val BIGINT NOT NULL);
CREATE TABLE IF NOT EXISTS transaction_number_seq (next_val BIGINT NOT NULL);

-- Ensure they have exactly one starting value
-- These 'INSERT IGNORE' or 'DELETE/INSERT' patterns ensure it works on every restart
DELETE FROM account_number_seq;
INSERT INTO account_number_seq (next_val) VALUES (1000001);

DELETE FROM transaction_number_seq;
INSERT INTO transaction_number_seq (next_val) VALUES (5000001);

-- Fix the Hibernate 6 'Unique' relationship bug
-- Note: These might fail if the table doesn't exist yet, but Hibernate 
-- usually creates entities before running import.sql.
ALTER TABLE account DROP INDEX IF EXISTS account_standing_id;
ALTER TABLE account DROP INDEX IF EXISTS account_type_id;
ALTER TABLE account DROP INDEX IF EXISTS ownership_type_id;