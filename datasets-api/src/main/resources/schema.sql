CREATE TABLE records (
  id BIGSERIAL PRIMARY KEY,
  dataset_name TEXT NOT NULL,
  payload JSONB NOT NULL
); 