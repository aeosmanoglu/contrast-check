public class Convert2Grayscale {
    public static String convert(String hexColor) {
        return convert(hexColor, .925);
    }

    /**
     * Creates a grayed version of the given hex color.
     *
     * @param hexColor The hex color value, with or without the '#' symbol.
     * @param factor   The factor to gray the color by, ranging from 0 (original color) to 1 (completely gray).
     * @return The grayed version of the hex color as a string, including the '#' symbol.
     */
    public static String convert(String hexColor, double factor) {
        hexColor = Utils.removeHex(hexColor);

        int red = Integer.parseInt(hexColor.substring(0, 2), 16);
        int green = Integer.parseInt(hexColor.substring(2, 4), 16);
        int blue = Integer.parseInt(hexColor.substring(4, 6), 16);

        // Calculating the gray value using a common weighted method
        int grayValue = (int) (0.299 * red + 0.587 * green + 0.114 * blue);

        // Blending the original color with the gray value using the provided factor
        red = (int) (red * (1 - factor) + grayValue * factor);
        green = (int) (green * (1 - factor) + grayValue * factor);
        blue = (int) (blue * (1 - factor) + grayValue * factor);

        return String.format("#%02x%02x%02x", red, green, blue);
    }
}
