// UC8 FINAL IMPLEMENTATION (Covers UC1–UC7 also)

enum LengthUnit {
    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double toFeetFactor;

    LengthUnit(double toFeetFactor) {
        this.toFeetFactor = toFeetFactor;
    }

    // Convert given value to base unit (feet)
    public double convertToBaseUnit(double value) {
        return value * toFeetFactor;
    }

    // Convert from base unit (feet) to this unit
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / toFeetFactor;
    }
}

class QuantityLength {
    private final double value;
    private final LengthUnit unit;
    private static final double EPSILON = 1e-6;

    public QuantityLength(double value, LengthUnit unit) {
        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");
        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Invalid value");

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
    }

    // Convert to another unit
    public QuantityLength convertTo(LengthUnit targetUnit) {
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double baseValue = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(baseValue);

        return new QuantityLength(converted, targetUnit);
    }

    // Static conversion method (UC5)
    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (source == null || target == null)
            throw new IllegalArgumentException("Units cannot be null");
        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Invalid value");

        double base = source.convertToBaseUnit(value);
        return target.convertFromBaseUnit(base);
    }

    // Addition (UC6 - result in first operand unit)
    public QuantityLength add(QuantityLength other) {
        return add(other, this.unit);
    }

    // Addition with explicit target unit (UC7)
    public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
        if (other == null || targetUnit == null)
            throw new IllegalArgumentException("Invalid input");

        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        double sumBase = base1 + base2;
        double result = targetUnit.convertFromBaseUnit(sumBase);

        return new QuantityLength(result, targetUnit);
    }

    // Equality (UC1–UC4)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        QuantityLength other = (QuantityLength) obj;

        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        return Math.abs(base1 - base2) < EPSILON;
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}

// MAIN APPLICATION
public class QuantityMeasurementApp {

    // Demo Methods (API style)
    public static void demonstrateLengthEquality(QuantityLength a, QuantityLength b) {
        System.out.println(a + " == " + b + " → " + a.equals(b));
    }

    public static void demonstrateLengthConversion(double value, LengthUnit from, LengthUnit to) {
        double result = QuantityLength.convert(value, from, to);
        System.out.println("convert(" + value + ", " + from + ", " + to + ") → " + result);
    }

    public static void demonstrateLengthConversion(QuantityLength q, LengthUnit to) {
        System.out.println(q + " → " + q.convertTo(to));
    }

    public static void demonstrateAddition(QuantityLength a, QuantityLength b, LengthUnit target) {
        System.out.println("add(" + a + ", " + b + ", " + target + ") → " + a.add(b, target));
    }

    public static void main(String[] args) {

        // UC1 & UC2 Equality
        QuantityLength f1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength f2 = new QuantityLength(1.0, LengthUnit.FEET);
        demonstrateLengthEquality(f1, f2);

        QuantityLength i1 = new QuantityLength(12.0, LengthUnit.INCHES);
        demonstrateLengthEquality(f1, i1);

        // UC5 Conversion
        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES);
        demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET);

        // UC6 Addition
        demonstrateAddition(f1, i1, LengthUnit.FEET);

        // UC7 Addition with target unit
        demonstrateAddition(f1, i1, LengthUnit.INCHES);
        demonstrateAddition(f1, i1, LengthUnit.YARDS);

        // UC4 Extended units
        QuantityLength y1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength cm = new QuantityLength(30.48, LengthUnit.CENTIMETERS);

        demonstrateLengthEquality(y1, f1.add(f1)); // 1 yard == 2 feet? false
        demonstrateLengthEquality(cm, f1); // 30.48 cm == 1 ft

        // UC8 Conversion using enum
        System.out.println("Base conversion (INCHES → FEET): " +
                LengthUnit.INCHES.convertToBaseUnit(12.0));
    }
}