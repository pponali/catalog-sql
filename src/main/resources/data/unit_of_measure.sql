-- Insert basic units of measure
INSERT INTO unit_of_measure (code, name, description, base_unit, conversion_factor)
VALUES 
    ('M', 'Meter', 'Base unit for length', 'M', 1.0),
    ('CM', 'Centimeter', 'Centimeter', 'M', 0.01),
    ('MM', 'Millimeter', 'Millimeter', 'M', 0.001),
    ('KG', 'Kilogram', 'Base unit for mass', 'KG', 1.0),
    ('G', 'Gram', 'Gram', 'KG', 0.001),
    ('L', 'Liter', 'Base unit for volume', 'L', 1.0),
    ('ML', 'Milliliter', 'Milliliter', 'L', 0.001),
    ('PCS', 'Pieces', 'Count of items', 'PCS', 1.0),
    ('INCH', 'Inch', 'Imperial length unit', 'M', 0.0254),
    ('FT', 'Feet', 'Imperial length unit', 'M', 0.3048);
