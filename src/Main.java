import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Get primary hex color from user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter Primary hex color (ex: #FF5733): ");
        String line = scanner.nextLine();
        scanner.close();

        // Generate color shades
        List<String> shades = generateColorShades(line);
        String grayscale = convertToGrayscale(line);
        List<String> grayscaleShades = generateColorShades(grayscale);


        // Print results
        // Color shades
        System.out.println("Generating color shades...");
        for (String shade : shades) {
            System.out.println(shade);
        }
        System.out.println("Done!");
        System.out.println();

        // Grayscale
        System.out.println("Converting to grayscale...");
        System.out.println(grayscale);
        System.out.println("Done!");
        System.out.println();

        // Grayscale shades
        System.out.println("Generating grayscale shades...");
        for (String shade : grayscaleShades) {
            System.out.println(shade);
        }
        System.out.println("Done!");
        System.out.println();

    }

    public static String convertToGrayscale(String hexColor) {
        return convertToGrayscale(hexColor, .925);
    }

    /**
     * Creates a grayed version of the given hex color.
     *
     * @param hexColor The hex color value, with or without the '#' symbol.
     * @param factor   The factor to gray the color by, ranging from 0 (original color) to 1 (completely gray).
     * @return The grayed version of the hex color as a string, including the '#' symbol.
     */
    public static String convertToGrayscale(String hexColor, double factor) {
        if (hexColor.startsWith("#")) {
            hexColor = hexColor.substring(1);
        }

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


    /**
     * Takes a given hex color, creates 4 lighter and 4 darker versions of the color.
     * The colors are saved in a list from light to dark, with the original color in the center.
     *
     * @param hexColor Input hex color value.
     * @return A list of 9 elements, the first 4 being lighter, the fifth the original color, and the last 4 darker versions.
     */
    public static List<String> generateColorShades(String hexColor) {
        if (hexColor.startsWith("#")) {
            hexColor = hexColor.substring(1);
        }

        List<String> shades = new ArrayList<>();

        // Create lighter color versions
        for (int i = 4; i >= 1; i--) {
            shades.add(adjustColor(hexColor, 0.2 * i, true));
        }

        // Add original color
        shades.add("#" + hexColor);

        // Create darker color versions
        for (int i = 1; i <= 4; i++) {
            shades.add(adjustColor(hexColor, 0.2 * i, false));
        }

        return shades;
    }


    /**
     * Adjusts a given hex color by the specified factor, either making it lighter or darker.
     *
     * @param color   The hex color value without the '#' symbol.
     * @param factor  The factor by which to adjust the color. Ranges from 0 to 1, with higher values making a more drastic change.
     * @param lighter If true, makes the color lighter; if false, makes it darker.
     * @return The adjusted hex color as a string, including the '#' symbol.
     */
    private static String adjustColor(String color, double factor, boolean lighter) {
        int red = Integer.parseInt(color.substring(0, 2), 16);
        int green = Integer.parseInt(color.substring(2, 4), 16);
        int blue = Integer.parseInt(color.substring(4, 6), 16);

        if (lighter) {
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
