public class QuantityMeasurementApp {

    // -------- Feet Class --------
    static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Feet other = (Feet) obj;
            return Double.compare(this.value, other.value) == 0;
        }
    }

    // -------- Inches Class --------
    static class Inches {
        private final double value;

        public Inches(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Inches other = (Inches) obj;
            return Double.compare(this.value, other.value) == 0;
        }
    }

    // -------- Static Methods (Reduce dependency on main) --------
    public static boolean checkFeetEquality(double a, double b) {
        Feet f1 = new Feet(a);
        Feet f2 = new Feet(b);
        return f1.equals(f2);
    }

    public static boolean checkInchesEquality(double a, double b) {
        Inches i1 = new Inches(a);
        Inches i2 = new Inches(b);
        return i1.equals(i2);
    }

    // -------- Main Method (Test Runner) --------
    public static void main(String[] args) {

        // ---- Feet Tests ----
        System.out.println("Feet Tests:");
        System.out.println("Same Value: " + checkFeetEquality(1.0, 1.0)); // true
        System.out.println("Different Value: " + checkFeetEquality(1.0, 2.0)); // false

        Feet f = new Feet(1.0);
        System.out.println("Null Comparison: " + f.equals(null)); // false
        System.out.println("Same Reference: " + f.equals(f)); // true
        System.out.println("Non-Numeric Input: " + f.equals("abc")); // false

        // ---- Inches Tests ----
        System.out.println("\nInches Tests:");
        System.out.println("Same Value: " + checkInchesEquality(1.0, 1.0)); // true
        System.out.println("Different Value: " + checkInchesEquality(1.0, 2.0)); // false

        Inches i = new Inches(1.0);
        System.out.println("Null Comparison: " + i.equals(null)); // false
        System.out.println("Same Reference: " + i.equals(i)); // true
        System.out.println("Non-Numeric Input: " + i.equals("xyz")); // false

        // ---- Example Output ----
        System.out.println("\nExample:");
        System.out.println("Input: 1.0 inch and 1.0 inch");
        System.out.println("Output: " + (checkInchesEquality(1.0, 1.0) ? "Equal (true)" : "Not Equal (false)"));

        System.out.println("Input: 1.0 ft and 1.0 ft");
        System.out.println("Output: " + (checkFeetEquality(1.0, 1.0) ? "Equal (true)" : "Not Equal (false)"));
    }
}