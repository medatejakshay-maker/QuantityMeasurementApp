public class QuantityMeasurementApp {

    // -------- ENUM for Units --------
    enum LengthUnit {
        FEET(1.0),        // base unit
        INCH(1.0 / 12.0); // 1 inch = 1/12 feet

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }
    }

    // -------- Generic Quantity Class --------
    static class Quantity {
        private final double value;
        private final LengthUnit unit;

        public Quantity(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        // Convert to base unit (feet)
        private double toFeet() {
            return unit.toFeet(value);
        }

        // Override equals()
        @Override
        public boolean equals(Object obj) {

            if (this == obj) return true;

            if (obj == null || getClass() != obj.getClass()) return false;

            Quantity other = (Quantity) obj;

            // Compare after conversion to common base unit
            return Double.compare(this.toFeet(), other.toFeet()) == 0;
        }
    }

    // -------- Static Helper Method --------
    public static boolean checkEquality(double v1, LengthUnit u1,
                                        double v2, LengthUnit u2) {
        Quantity q1 = new Quantity(v1, u1);
        Quantity q2 = new Quantity(v2, u2);
        return q1.equals(q2);
    }

    // -------- Main Method (Test Cases) --------
    public static void main(String[] args) {

        // ---- Same Unit Equality ----
        System.out.println("Feet to Feet (Same): " +
                checkEquality(1.0, LengthUnit.FEET, 1.0, LengthUnit.FEET)); // true

        System.out.println("Inch to Inch (Same): " +
                checkEquality(1.0, LengthUnit.INCH, 1.0, LengthUnit.INCH)); // true

        // ---- Cross Unit Equality ----
        System.out.println("Feet to Inch (Equivalent): " +
                checkEquality(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH)); // true

        System.out.println("Inch to Feet (Equivalent): " +
                checkEquality(12.0, LengthUnit.INCH, 1.0, LengthUnit.FEET)); // true

        // ---- Different Values ----
        System.out.println("Feet Different: " +
                checkEquality(1.0, LengthUnit.FEET, 2.0, LengthUnit.FEET)); // false

        System.out.println("Inch Different: " +
                checkEquality(1.0, LengthUnit.INCH, 2.0, LengthUnit.INCH)); // false

        // ---- Null Comparison ----
        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        System.out.println("Null Comparison: " + q1.equals(null)); // false

        // ---- Same Reference ----
        System.out.println("Same Reference: " + q1.equals(q1)); // true

        // ---- Invalid Unit Test ----
        try {
            Quantity invalid = new Quantity(1.0, null);
        } catch (Exception e) {
            System.out.println("Invalid Unit: Exception caught -> " + e.getMessage());
        }

        // ---- Example Output ----
        System.out.println("\nExample:");
        System.out.println("Input: Quantity(1.0, FEET) and Quantity(12.0, INCH)");
        System.out.println("Output: " +
                (checkEquality(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH)
                        ? "Equal (true)" : "Not Equal (false)"));

        System.out.println("Input: Quantity(1.0, INCH) and Quantity(1.0, INCH)");
        System.out.println("Output: " +
                (checkEquality(1.0, LengthUnit.INCH, 1.0, LengthUnit.INCH)
                        ? "Equal (true)" : "Not Equal (false)"));
    }
}