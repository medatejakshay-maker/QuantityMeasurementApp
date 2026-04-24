public class QuantityMeasurementApp {

    // -------- ENUM (Base Unit: FEET) --------
    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0),
        YARD(3.0),
        CENTIMETER(0.393701 / 12.0);

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }

        public double fromFeet(double feetValue) {
            return feetValue / toFeetFactor;
        }
    }

    // -------- Quantity Class --------
    static class Quantity {
        private final double value;
        private final LengthUnit unit;

        public Quantity(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Invalid numeric value");
            }
            this.value = value;
            this.unit = unit;
        }

        private double toFeet() {
            return unit.toFeet(value);
        }

        // -------- Instance Conversion --------
        public Quantity convertTo(LengthUnit targetUnit) {
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }
            double feetValue = this.toFeet();
            double converted = targetUnit.fromFeet(feetValue);
            return new Quantity(converted, targetUnit);
        }

        // -------- Equals Override --------
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            Quantity other = (Quantity) obj;
            return Double.compare(this.toFeet(), other.toFeet()) == 0;
        }

        // -------- toString Override --------
        @Override
        public String toString() {
            return "Quantity(" + value + ", " + unit + ")";
        }
    }

    // -------- Static Conversion API --------
    public static double convert(double value, LengthUnit source, LengthUnit target) {

        if (source == null || target == null) {
            throw new IllegalArgumentException("Units cannot be null");
        }

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid numeric value");
        }

        // Convert → base (feet)
        double inFeet = source.toFeet(value);

        // Convert → target
        return target.fromFeet(inFeet);
    }

    // -------- Overloaded Demonstration Methods --------
    public static void demonstrateLengthConversion(double value,
                                                   LengthUnit from,
                                                   LengthUnit to) {
        double result = convert(value, from, to);
        System.out.println("convert(" + value + ", " + from + ", " + to + ") → " + result);
    }

    public static void demonstrateLengthConversion(Quantity q,
                                                   LengthUnit to) {
        Quantity converted = q.convertTo(to);
        System.out.println(q + " → " + converted);
    }

    public static void demonstrateLengthEquality(Quantity q1, Quantity q2) {
        System.out.println(q1 + " == " + q2 + " → " + q1.equals(q2));
    }

    // -------- Main Method (All Test Cases) --------
    public static void main(String[] args) {

        // ---- Basic Conversion ----
        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCH); // 12
        demonstrateLengthConversion(3.0, LengthUnit.YARD, LengthUnit.FEET); // 9
        demonstrateLengthConversion(36.0, LengthUnit.INCH, LengthUnit.YARD); // 1
        demonstrateLengthConversion(1.0, LengthUnit.CENTIMETER, LengthUnit.INCH);

        // ---- Zero, Negative ----
        demonstrateLengthConversion(0.0, LengthUnit.FEET, LengthUnit.INCH);
        demonstrateLengthConversion(-1.0, LengthUnit.FEET, LengthUnit.INCH);

        // ---- Same Unit ----
        demonstrateLengthConversion(5.0, LengthUnit.FEET, LengthUnit.FEET);

        // ---- Instance Conversion ----
        Quantity q1 = new Quantity(1.0, LengthUnit.YARD);
        demonstrateLengthConversion(q1, LengthUnit.INCH);

        // ---- Equality ----
        Quantity q2 = new Quantity(3.0, LengthUnit.FEET);
        Quantity q3 = new Quantity(36.0, LengthUnit.INCH);
        demonstrateLengthEquality(q1, q2);
        demonstrateLengthEquality(q2, q3);

        // ---- Round Trip ----
        double original = 5.0;
        double converted = convert(original, LengthUnit.FEET, LengthUnit.INCH);
        double back = convert(converted, LengthUnit.INCH, LengthUnit.FEET);
        System.out.println("Round Trip: " + original + " → " + back);

        // ---- Invalid Inputs ----
        try {
            convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCH);
        } catch (Exception e) {
            System.out.println("NaN Test: " + e.getMessage());
        }

        try {
            convert(1.0, null, LengthUnit.INCH);
        } catch (Exception e) {
            System.out.println("Null Unit Test: " + e.getMessage());
        }

        // ---- Example Outputs ----
        System.out.println("\nExample Outputs:");
        System.out.println("convert(1.0, FEET, INCH) → " +
                convert(1.0, LengthUnit.FEET, LengthUnit.INCH));

        System.out.println("convert(3.0, YARD, FEET) → " +
                convert(3.0, LengthUnit.YARD, LengthUnit.FEET));

        System.out.println("convert(36.0, INCH, YARD) → " +
                convert(36.0, LengthUnit.INCH, LengthUnit.YARD));

        System.out.println("convert(1.0, CENTIMETER, INCH) → " +
                convert(1.0, LengthUnit.CENTIMETER, LengthUnit.INCH));
    }
}