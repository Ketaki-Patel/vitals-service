-- Create readings table
DROP TABLE IF EXISTS readings;

CREATE TABLE readings (
    reading_id UUID PRIMARY KEY,
    patient_id VARCHAR(255) NOT NULL,
    type VARCHAR(10) NOT NULL CHECK (type IN ('BP', 'HR', 'SPO2')),
    systolic INTEGER,
    diastolic INTEGER,
    hr INTEGER,
    spo2 INTEGER,
    captured_at TIMESTAMP NOT NULL
);

-- Create indexes for better query performance
CREATE INDEX idx_readings_patient_id ON readings(patient_id);
CREATE INDEX idx_readings_type ON readings(type);
CREATE INDEX idx_readings_captured_at ON readings(captured_at);