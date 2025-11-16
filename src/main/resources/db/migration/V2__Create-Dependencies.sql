-- =====================================================
-- 1. CREATE NEW leo_dependencies TABLE (for prerequisite graph)
-- =====================================================

CREATE TABLE IF NOT EXISTS leo_dependencies (
    id BIGSERIAL PRIMARY KEY,
    dependent_leo_id BIGINT NOT NULL,
    prerequisite_leo_id BIGINT NOT NULL,
    dependency_type VARCHAR(50) NOT NULL DEFAULT 'PREREQUISITE' 
        CHECK (dependency_type IN ('PREREQUISITE', 'RECOMMENDED', 'RELATED')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign keys
    CONSTRAINT fk_leo_dep_dependent FOREIGN KEY (dependent_leo_id) 
        REFERENCES leos(id) ON DELETE CASCADE,
    CONSTRAINT fk_leo_dep_prerequisite FOREIGN KEY (prerequisite_leo_id) 
        REFERENCES leos(id) ON DELETE CASCADE,
    
    -- Constraints
    CONSTRAINT uk_leo_dependency UNIQUE (dependent_leo_id, prerequisite_leo_id),
    CONSTRAINT check_no_self_dependency CHECK (dependent_leo_id != prerequisite_leo_id)
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_leo_dep_prerequisite ON leo_dependencies(prerequisite_leo_id);
CREATE INDEX IF NOT EXISTS idx_leo_dep_dependent ON leo_dependencies(dependent_leo_id);

-- =====================================================
-- 2. ADD COLUMNS TO suggestions TABLE (priority & dismiss)
-- =====================================================
-- CREATE TABLE suggestions if not exists
CREATE TABLE IF NOT EXISTS suggestions (
                                           id BIGSERIAL PRIMARY KEY,
                                           student_id BIGINT NOT NULL,
                                           leo_id BIGINT NOT NULL,
                                           suggested_reason TEXT,
                                           prerequisites_met INTEGER,
                                           total_prerequisites INTEGER,
                                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           FOREIGN KEY (student_id) REFERENCES users(id),
    FOREIGN KEY (leo_id) REFERENCES leos(id),
    UNIQUE (student_id, leo_id)
    );




ALTER TABLE suggestions
ADD COLUMN IF NOT EXISTS readiness_score DOUBLE PRECISION,
ADD COLUMN IF NOT EXISTS last_prerequisite_reached_days_ago BIGINT,
ADD COLUMN IF NOT EXISTS priority INTEGER,
ADD COLUMN IF NOT EXISTS is_dismissed BOOLEAN DEFAULT false,
ADD COLUMN IF NOT EXISTS dismissed_at TIMESTAMP,
ADD COLUMN IF NOT EXISTS dismissed_by BIGINT;

-- Add foreign key for dismissed_by if not exists
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_suggestion_dismissed_by'
    ) THEN
ALTER TABLE suggestions
    ADD CONSTRAINT fk_suggestion_dismissed_by
        FOREIGN KEY (dismissed_by) REFERENCES users(id) ON DELETE SET NULL;
END IF;
END $$;

-- Create indexes for suggestions
CREATE INDEX IF NOT EXISTS idx_suggestion_student ON suggestions(student_id);
CREATE INDEX IF NOT EXISTS idx_suggestion_leo ON suggestions(leo_id);
CREATE INDEX IF NOT EXISTS idx_suggestion_dismissed ON suggestions(is_dismissed);
CREATE INDEX IF NOT EXISTS idx_suggestion_priority ON suggestions(priority);

-- =====================================================
-- 3. REMOVE OLD leo_prerequisites TABLE (if exists)
-- =====================================================

DROP TABLE IF EXISTS leo_prerequisites CASCADE;

-- =====================================================
-- 4. CREATE AUDIT TRAIL TRIGGERS
-- =====================================================

-- Trigger for leo_dependencies updated_at
CREATE OR REPLACE FUNCTION update_leo_dependencies_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_leo_dependencies_updated_at ON leo_dependencies;
CREATE TRIGGER trg_leo_dependencies_updated_at
BEFORE UPDATE ON leo_dependencies
FOR EACH ROW
EXECUTE FUNCTION update_leo_dependencies_updated_at();

-- Trigger for suggestions updated_at
CREATE OR REPLACE FUNCTION update_suggestions_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_suggestions_updated_at ON suggestions;
CREATE TRIGGER trg_suggestions_updated_at
BEFORE UPDATE ON suggestions
FOR EACH ROW
EXECUTE FUNCTION update_suggestions_updated_at();

-- =====================================================
-- 5. VERIFICATION CHECKS
-- =====================================================

-- Check that leo_dependencies table was created
SELECT COUNT(*) as leo_dependencies_count FROM leo_dependencies;

-- Success message
DO $$
BEGIN
    RAISE NOTICE 'Migration V2 completed successfully!';
    RAISE NOTICE 'New table leo_dependencies created';
    RAISE NOTICE 'Suggestions table enhanced with priority fields';
END $$;
