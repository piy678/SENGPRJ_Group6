-- =====================================================
-- V4__fix_readiness_score_type.sql
-- Мета: привести тип колонки readiness_score до очікуваного Hibernate
-- Очікується: NUMERIC(5,2), зараз: DOUBLE PRECISION (float8)
-- =====================================================

ALTER TABLE suggestions
ALTER COLUMN readiness_score
    TYPE NUMERIC(5,2)
    USING ROUND(readiness_score::NUMERIC, 2);


SELECT column_name, data_type, numeric_precision, numeric_scale
FROM information_schema.columns
WHERE table_name = 'suggestions'
  AND column_name = 'readiness_score';

DO $$
BEGIN
    RAISE NOTICE 'Migration V4 completed: readiness_score converted to NUMERIC(5,2).';
END $$;
