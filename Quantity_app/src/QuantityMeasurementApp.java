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
            if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");
            this.value = value;
            this.unit = unit;
        }

        private double toFeet() {
            return unit.toFeet(value);
        }

        // -------- Convert --------
        public Quantity convertTo(LengthUnit targetUnit) {
            if (targetUnit == null) throw new IllegalArgumentException("Target unit null");
            double feet = toFeet();
            double converted = targetUnit.fromFeet(feet);
            return new Quantity(converted, targetUnit);
        }

        // -------- Add (Instance Method) --------
        public Quantity add(Quantity other) {
            if (other == null) throw new IllegalArgumentException("Other cannot be null");

            double sumFeet = this.toFeet() + other.toFeet();
            double result = this.unit.fromFeet(sumFeet);

            return new Quantity(result, this.unit); // result in unit of first operand
        }

        // -------- Static Add (Flexible API) --------
        public static Quantity add(Quantity q1, Quantity q2, LengthUnit targetUnit) {
            if (q1 == null || q2 == null)
                throw new IllegalArgumentException("Operands cannot be null");
            if (targetUnit == null)
                throw new IllegalArgumentException("Target unit cannot be null");

            double sumFeet = q1.toFeet() + q2.toFeet();
            double result = targetUnit.fromFeet(sumFeet);

            return new Quantity(result, targetUnit);
        }

        // -------- Equals --------
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Quantity other = (Quantity) obj;
            return Double.compare(this.toFeet(), other.toFeet()) == 0;
        }

        // -------- toString --------
        @Override
        public String toString() {
            return "Quantity(" + value + ", " + unit + ")";
        }
    }

    // -------- Main Method (All Test Cases) --------
    public static void main(String[] args) {

        // ---- Same Unit ----
        System.out.println("Feet + Feet: " +
                new Quantity(1.0, LengthUnit.FEET)
                        .add(new Quantity(2.0, LengthUnit.FEET))); // 3 ft

        System.out.println("Inch + Inch: " +
                new Quantity(6.0, LengthUnit.INCH)
                        .add(new Quantity(6.0, LengthUnit.INCH))); // 12 in

        // ---- Cross Unit ----
        System.out.println("Feet + Inch: " +
                new Quantity(1.0, LengthUnit.FEET)
                        .add(new Quantity(12.0, LengthUnit.INCH))); // 2 ft

        System.out.println("Inch + Feet: " +
                new Quantity(12.0, LengthUnit.INCH)
                        .add(new Quantity(1.0, LengthUnit.FEET))); // 24 in

        System.out.println("Yard + Feet: " +
                new Quantity(1.0, LengthUnit.YARD)
                        .add(new Quantity(3.0, LengthUnit.FEET))); // 2 yd

        System.out.println("Inch + Yard: " +
                new Quantity(36.0, LengthUnit.INCH)
                        .add(new Quantity(1.0, LengthUnit.YARD))); // 72 in

        System.out.println("CM + Inch: " +
                new Quantity(2.54, LengthUnit.CENTIMETER)
                        .add(new Quantity(1.0, LengthUnit.INCH))); // ~5.08 cm

        // ---- Zero ----
        System.out.println("With Zero: " +
                new Quantity(5.0, LengthUnit.FEET)
                        .add(new Quantity(0.0, LengthUnit.INCH)));

        // ---- Negative ----
        System.out.println("With Negative: " +
                new Quantity(5.0, LengthUnit.FEET)
                        .add(new Quantity(-2.0, LengthUnit.FEET)));

        // ---- Commutativity ----
        Quantity a = new Quantity(1.0, LengthUnit.FEET);
        Quantity b = new Quantity(12.0, LengthUnit.INCH);

        Quantity result1 = a.add(b);
        Quantity result2 = Quantity.add(b, a, LengthUnit.FEET);

        System.out.println("Commutativity Check: " + result1.equals(result2));

        // ---- Large Values ----
        System.out.println("Large Values: " +
                new Quantity(1e6, LengthUnit.FEET)
                        .add(new Quantity(1e6, LengthUnit.FEET)));

        // ---- Small Values ----
        System.out.println("Small Values: " +
                new Quantity(0.001, LengthUnit.FEET)
                        .add(new Quantity(0.002, LengthUnit.FEET)));

        // ---- Null Handling ----
        try {
            new Quantity(1.0, LengthUnit.FEET).add(null);
        } catch (Exception e) {
            System.out.println("Null Test: " + e.getMessage());
        }

        // ---- Example Outputs ----
        System.out.println("\nExample Outputs:");

        System.out.println("add(Quantity(1.0, FEET), Quantity(2.0, FEET)) → " +
                new Quantity(1.0, LengthUnit.FEET)
                        .add(new Quantity(2.0, LengthUnit.FEET)));

        System.out.println("add(Quantity(1.0, FEET), Quantity(12.0, INCH)) → " +
                new Quantity(1.0, LengthUnit.FEET)
                        .add(new Quantity(12.0, LengthUnit.INCH)));

        System.out.println("add(Quantity(12.0, INCH), Quantity(1.0, FEET)) → " +
                new Quantity(12.0, LengthUnit.INCH)
                        .add(new Quantity(1.0, LengthUnit.FEET)));
    }
}