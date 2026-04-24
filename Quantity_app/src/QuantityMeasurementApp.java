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
            return new Quantity(targetUnit.fromFeet(feet), targetUnit);
        }

        // -------- UC6: Add (result in first operand unit) --------
        public Quantity add(Quantity other) {
            return add(this, other, this.unit);
        }

        // -------- UC7: Add with Explicit Target Unit --------
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

        Quantity f1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity i12 = new Quantity(12.0, LengthUnit.INCH);
        Quantity y1 = new Quantity(1.0, LengthUnit.YARD);
        Quantity cm = new Quantity(2.54, LengthUnit.CENTIMETER);

        // ---- Explicit Target Unit Tests ----
        System.out.println("Feet Target: " +
                Quantity.add(f1, i12, LengthUnit.FEET)); // 2 ft

        System.out.println("Inch Target: " +
                Quantity.add(f1, i12, LengthUnit.INCH)); // 24 in

        System.out.println("Yard Target: " +
                Quantity.add(f1, i12, LengthUnit.YARD)); // ~0.667 yd

        System.out.println("Yard + Feet → Yard: " +
                Quantity.add(y1, new Quantity(3.0, LengthUnit.FEET), LengthUnit.YARD)); // 2 yd

        System.out.println("Inch + Yard → Feet: " +
                Quantity.add(new Quantity(36.0, LengthUnit.INCH), y1, LengthUnit.FEET)); // 6 ft

        System.out.println("CM + Inch → CM: " +
                Quantity.add(cm, new Quantity(1.0, LengthUnit.INCH), LengthUnit.CENTIMETER)); // ~5.08 cm

        // ---- Zero ----
        System.out.println("With Zero: " +
                Quantity.add(new Quantity(5.0, LengthUnit.FEET),
                        new Quantity(0.0, LengthUnit.INCH),
                        LengthUnit.YARD));

        // ---- Negative ----
        System.out.println("Negative Values: " +
                Quantity.add(new Quantity(5.0, LengthUnit.FEET),
                        new Quantity(-2.0, LengthUnit.FEET),
                        LengthUnit.INCH));

        // ---- Commutativity ----
        Quantity r1 = Quantity.add(f1, i12, LengthUnit.YARD);
        Quantity r2 = Quantity.add(i12, f1, LengthUnit.YARD);
        System.out.println("Commutativity: " + r1.equals(r2));

        // ---- Same as UC6 (backward compatibility) ----
        System.out.println("Default Add (UC6): " + f1.add(i12)); // 2 ft

        // ---- Null Target Unit ----
        try {
            Quantity.add(f1, i12, null);
        } catch (Exception e) {
            System.out.println("Null Target Test: " + e.getMessage());
        }

        // ---- Large to Small Scale ----
        System.out.println("Large to Small: " +
                Quantity.add(new Quantity(1000.0, LengthUnit.FEET),
                        new Quantity(500.0, LengthUnit.FEET),
                        LengthUnit.INCH));

        // ---- Small to Large Scale ----
        System.out.println("Small to Large: " +
                Quantity.add(new Quantity(12.0, LengthUnit.INCH),
                        new Quantity(12.0, LengthUnit.INCH),
                        LengthUnit.YARD));

        // ---- Example Outputs ----
        System.out.println("\nExample Outputs:");
        System.out.println("add(1 FT, 12 IN, FT) → " +
                Quantity.add(f1, i12, LengthUnit.FEET));

        System.out.println("add(1 FT, 12 IN, IN) → " +
                Quantity.add(f1, i12, LengthUnit.INCH));

        System.out.println("add(1 FT, 12 IN, YD) → " +
                Quantity.add(f1, i12, LengthUnit.YARD));
    }
}