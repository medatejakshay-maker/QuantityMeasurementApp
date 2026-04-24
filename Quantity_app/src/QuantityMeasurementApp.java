public class QuantityMeasurementApp {

    // Inner class to represent Feet measurement
    static class Feet {
        private final double value;

        // Constructor
        public Feet(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        // Overriding equals() method
        @Override
        public boolean equals(Object obj) {

            // Same reference check
            if (this == obj) return true;

            // Null and type check
            if (obj == null || getClass() != obj.getClass()) return false;

            // Type casting
            Feet other = (Feet) obj;

            // Floating-point comparison
            return Double.compare(this.value, other.value) == 0;
        }
    }

    // Main method (acts like test runner)
    public static void main(String[] args) {

        // Test 1: Same Value
        Feet f1 = new Feet(1.0);
        Feet f2 = new Feet(1.0);
        System.out.println("Test Same Value: " + f1.equals(f2)); // true

        // Test 2: Different Value
        Feet f3 = new Feet(2.0);
        System.out.println("Test Different Value: " + f1.equals(f3)); // false

        // Test 3: Null Comparison
        System.out.println("Test Null Comparison: " + f1.equals(null)); // false

        // Test 4: Same Reference
        System.out.println("Test Same Reference: " + f1.equals(f1)); // true

        // Test 5: Non-numeric input
        String nonNumeric = "abc";
        System.out.println("Test Non-Numeric Input: " + f1.equals(nonNumeric)); // false

        // Example Output format
        System.out.println("\nExample:");
        System.out.println("Input: 1.0 ft and 1.0 ft");
        System.out.println("Output: " + (f1.equals(f2) ? "Equal (true)" : "Not Equal (false)"));
    }
}