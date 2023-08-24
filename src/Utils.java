public class Utils {
    /**
     * Removes the hash symbol ('#') from the beginning of the given hex color string if it exists.
     *
     * @param color The hex color string, with or without the '#' symbol at the start.
     * @return The hex color string without the '#' symbol at the start.
     */
    public static String removeHex(String color) {
        return color.startsWith("#") ? color.substring(1) : color;
    }
}
