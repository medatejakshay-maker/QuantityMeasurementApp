public class QuantityMeasurementApp {

    // -------- ENUM for Units (Base: FEET) --------
    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0),                 // 1 inch = 1/12 feet
        YARD(3.0),                        // 1 yard = 3 feet
        CENTIMETER(0.393701 / 12.0);      // 1 cm = 0.393701 inch -> in feet

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

        private double toFeet() {
            return unit.toFeet(value);
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) return true;

            if (obj == null || getClass() != obj.getClass()) return false;

            Quantity other = (Quantity) obj;

            return Double.compare(this.toFeet(), other.toFeet()) == 0;
        }
    }

    // -------- Helper Method --------
    public static boolean checkEquality(double v1, LengthUnit u1,
                                        double v2, LengthUnit u2) {
        Quantity q1 = new Quantity(v1, u1);
        Quantity q2 = new Quantity(v2, u2);
        return q1.equals(q2);
    }

    // -------- Main Method (All Test Cases) --------
    public static void main(String[] args) {

        // ---- Yard Tests ----
        System.out.println("Yard to Yard Same: " +
                checkEquality(1.0, LengthUnit.YARD, 1.0, LengthUnit.YARD)); // true

        System.out.println("Yard to Yard Different: " +
                checkEquality(1.0, LengthUnit.YARD, 2.0, LengthUnit.YARD)); // false

        System.out.println("Yard to Feet: " +
                checkEquality(1.0, LengthUnit.YARD, 3.0, LengthUnit.FEET)); // true

        System.out.println("Feet to Yard: " +
                checkEquality(3.0, LengthUnit.FEET, 1.0, LengthUnit.YARD)); // true

        System.out.println("Yard to Inch: " +
                checkEquality(1.0, LengthUnit.YARD, 36.0, LengthUnit.INCH)); // true

        System.out.println("Inch to Yard: " +
                checkEquality(36.0, LengthUnit.INCH, 1.0, LengthUnit.YARD)); // true

        System.out.println("Yard Non-Equivalent: " +
                checkEquality(1.0, LengthUnit.YARD, 2.0, LengthUnit.FEET)); // false

        // ---- Centimeter Tests ----
        System.out.println("\nCentimeter Tests:");

        System.out.println("CM to CM Same: " +
                checkEquality(2.0, LengthUnit.CENTIMETER, 2.0, LengthUnit.CENTIMETER)); // true

        System.out.println("CM to Inch: " +
                checkEquality(1.0, LengthUnit.CENTIMETER, 0.393701, LengthUnit.INCH)); // true

        System.out.println("CM to Feet Non-Equivalent: " +
                checkEquality(1.0, LengthUnit.CENTIMETER, 1.0, LengthUnit.FEET)); // false

        // ---- Transitive Property ----
        boolean a = checkEquality(1.0, LengthUnit.YARD, 3.0, LengthUnit.FEET);
        boolean b = checkEquality(3.0, LengthUnit.FEET, 36.0, LengthUnit.INCH);
        boolean c = checkEquality(1.0, LengthUnit.YARD, 36.0, LengthUnit.INCH);

        System.out.println("\nTransitive Property (Yard=Feet & Feet=Inch ⇒ Yard=Inch): "
                + (a && b && c)); // true

        // ---- Same Reference ----
        Quantity q = new Quantity(1.0, LengthUnit.YARD);
        System.out.println("Same Reference: " + q.equals(q)); // true

        // ---- Null Comparison ----
        System.out.println("Null Comparison: " + q.equals(null)); // false

        // ---- Invalid Unit ----
        try {
            Quantity invalid = new Quantity(1.0, null);
        } catch (Exception e) {
            System.out.println("Invalid Unit: " + e.getMessage());
        }

        // ---- Complex Multi-Unit ----
        boolean complex = checkEquality(2.0, LengthUnit.YARD, 6.0, LengthUnit.FEET) &&
                checkEquality(6.0, LengthUnit.FEET, 72.0, LengthUnit.INCH);

        System.out.println("Complex Multi-Unit (2 yd = 6 ft = 72 in): " + complex);

        // ---- Example Outputs ----
        System.out.println("\nExample Outputs:");

        System.out.println("Input: Quantity(1.0, YARD) and Quantity(3.0, FEET)");
        System.out.println("Output: " +
                (checkEquality(1.0, LengthUnit.YARD, 3.0, LengthUnit.FEET)
                        ? "Equal (true)" : "Not Equal (false)"));

        System.out.println("Input: Quantity(1.0, YARD) and Quantity(36.0, INCH)");
        System.out.println("Output: " +
                (checkEquality(1.0, LengthUnit.YARD, 36.0, LengthUnit.INCH)
                        ? "Equal (true)" : "Not Equal (false)"));

        System.out.println("Input: Quantity(1.0, CENTIMETER) and Quantity(0.393701, INCH)");
        System.out.println("Output: " +
                (checkEquality(1.0, LengthUnit.CENTIMETER, 0.393701, LengthUnit.INCH)
                        ? "Equal (true)" : "Not Equal (false)"));
    }
}