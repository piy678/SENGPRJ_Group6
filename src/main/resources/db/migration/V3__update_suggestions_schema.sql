-- =====================================================
-- V3__update_suggestions_schema.sql
-- Мета: гарантувати, що в таблиці suggestions
-- є колонки prerequisites_met та total_prerequisites
-- =====================================================

ALTER TABLE suggestions
    ADD COLUMN IF NOT EXISTS prerequisites_met INTEGER,
    ADD COLUMN IF NOT EXISTS total_prerequisites INTEGER;

SELECT column_name
FROM information_schema.columns
WHERE table_name = 'suggestions'
  AND column_name IN ('prerequisites_met', 'total_prerequisites');

DO $$
BEGIN
    RAISE NOTICE 'Migration V3 completed: suggestions schema updated (prerequisites_met, total_prerequisites ensured).';
END $$;
