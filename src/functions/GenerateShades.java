package functions;

import models.Color;
import models.ColorList;

import static functions.Utils.removeHex;

public class GenerateShades {
    /**
     * Takes a given hex color, creates 4 lighter and 4 darker versions of the color.
     * The colors are saved in a list from light to dark, with the original color in the center.
     * The name of each color is generated based on the input name, and each shade is appended with a numerical value.
     *
     * @param name     The base name for each color shade.
     * @param hexColor Input hex color value.
     * @return A list of 9 elements, the first 4 being lighter shades with the naming convention name-100 to name-400,
     * the fifth the original color as name-500, and the last 4 darker versions as name-600 to name-900.
     */
    public static ColorList generate(String name, String hexColor) {
        hexColor = removeHex(hexColor);

        ColorList shades = new ColorList();

        for (int i = -4; i <= 4; i++) {
            if (i == 0) continue; // Skip 0
            boolean isLighter = i < 0;
            int absI = Math.abs(i);
            String shade = adjustColor(hexColor, 0.2 * absI, isLighter);
            int value = isLighter ? 100 * (4 - absI + 1) : 500 + 100 * absI;
            shades.add(new Color(name + "-" + value, shade));
        }

        // Add original color
        shades.add(4, new Color(name + "-500", "#" + hexColor));


        return shades;
    }


    /**
     * Adjusts a given hex color by the specified factor, either making it lighter or darker.
     *
     * @param color     The hex color value without the '#' symbol.
     * @param factor    The factor by which to adjust the color. Ranges from 0 to 1, with higher values making a more drastic change.
     * @param isLighter If true, makes the color lighter; if false, makes it darker.
     * @return The adjusted hex color as a string, including the '#' symbol.
     */
    private static String adjustColor(String color, double factor, boolean isLighter) {
        int red = Integer.parseInt(color.substring(0, 2), 16);
        int green = Integer.parseInt(color.substring(2, 4), 16);
        int blue = Integer.parseInt(color.substring(4, 6), 16);

        if (isLighter) {
            red = Math.min(red + (int) ((255 - red) * factor), 255);
            green = Math.min(green + (int) ((255 - green) * factor), 255);
            blue = Math.min(blue + (int) ((255 - blue) * factor), 255);
        } else {
            red = Math.max(red - (int) (red * factor), 0);
            green = Math.max(green - (int) (green * factor), 0);
            blue = Math.max(blue - (int) (blue * factor), 0);
        }

        return String.format("#%02x%02x%02x", red, green, blue);
    }
}
