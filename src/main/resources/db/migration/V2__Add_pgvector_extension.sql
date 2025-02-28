-- Enable pgvector extension
CREATE EXTENSION IF NOT EXISTS vector;

-- Add vector columns to relevant tables
ALTER TABLE product ADD COLUMN IF NOT EXISTS embedding vector(384);
ALTER TABLE category ADD COLUMN IF NOT EXISTS embedding vector(384);
