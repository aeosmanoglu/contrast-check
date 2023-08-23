import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
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
        List<Color> shades = generateColorShades("primary", line);
        String grayscale = convertToGrayscale(line);
        List<Color> grayscaleShades = generateColorShades("grayscale", grayscale);

        // Compare color shades to grayscale shades
        System.out.println("Generating color shades...");
        String csvFileName = removeHex(line) + "-contrast-ratios.csv";
        compareColorListsToCSV(shades, grayscaleShades, csvFileName);
        System.out.println(csvFileName + " created!");

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
        hexColor = removeHex(hexColor);

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
     * The name of each color is generated based on the input name, and each shade is appended with a numerical value.
     *
     * @param name     The base name for each color shade.
     * @param hexColor Input hex color value.
     * @return A list of 9 elements, the first 4 being lighter shades with the naming convention name-100 to name-400,
     * the fifth the original color as name-500, and the last 4 darker versions as name-600 to name-900.
     */
    public static List<Color> generateColorShades(String name, String hexColor) {
        hexColor = removeHex(hexColor);

        List<Color> shades = new LinkedList<>();

        // Create lighter color versions
        for (int i = 4; i >= 1; i--) {
            String shade = adjustColor(hexColor, 0.2 * i, true);
            shades.add(new Color(name + "-" + (100 * (4 - i + 1)), shade));
        }

        // Add original color
        shades.add(new Color(name + "-500", "#" + hexColor));

        // Create darker color versions
        for (int i = 1; i <= 4; i++) {
            String shade = adjustColor(hexColor, 0.2 * i, false);
            shades.add(new Color(name + "-" + (500 + 100 * i), shade));
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


    /**
     * Compares two lists of Color objects, calculates the contrast ratio between each pair of corresponding colors
     * according to W3C standards, and writes the results to a CSV file.
     * The contrast ratios are determined based on the relative luminance of the colors.
     *
     * @param list1       The first list of Color objects.
     * @param list2       The second list of Color objects.
     * @param csvFileName The name of the CSV file where the contrast ratios will be written.
     */
    public static void compareColorListsToCSV(List<Color> list1, List<Color> list2, String csvFileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFileName))) {
            writer.write("Name, Color, Name, Color, Ratio\n");
            for (Color color1 : list1) {
                for (Color color2 : list2) {
                    double contrast = calculateContrast(color1.getHexCode(), color2.getHexCode());
                    writer.write(color1.getName() + ", " + color1.getHexCode() + ", " + color2.getName() + ", " + color2.getHexCode() + ", " + contrast + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Calculates the contrast ratio between two given hex color strings.
     * The contrast ratio is determined according to the W3C recommendations
     * and is based on the relative luminance of the colors. It's useful in
     * determining the legibility of text and the separation of visual elements.
     *
     * @param color1 The first hex color string, with or without the '#' symbol at the start.
     * @param color2 The second hex color string, with or without the '#' symbol at the start.
     * @return The contrast ratio between the two colors, a value typically in the range of 1 (no contrast) to 21 (maximum contrast).
     */
    public static double calculateContrast(String color1, String color2) {
        double l1 = calculateLuminance(color1);
        double l2 = calculateLuminance(color2);

        return (Math.max(l1, l2) + 0.05) / (Math.min(l1, l2) + 0.05);
    }


    /**
     * Calculates the relative luminance of a given hex color according to the sRGB color space.
     * The calculated luminance is based on human perception and follows the W3C recommendations.
     * It considers the gamma correction and weights the color channels according to their contribution to perceived brightness.
     *
     * @param color The hex color string, with or without the '#' symbol at the start.
     * @return The relative luminance of the color, a value between 0 (black) and 1 (white).
     */
    private static double calculateLuminance(String color) {
        color = removeHex(color);

        double red = Integer.parseInt(color.substring(0, 2), 16) / 255.0;
        double green = Integer.parseInt(color.substring(2, 4), 16) / 255.0;
        double blue = Integer.parseInt(color.substring(4, 6), 16) / 255.0;

        red = (red <= 0.03928) ? red / 12.92 : Math.pow((red + 0.055) / 1.055, 2.4);
        green = (green <= 0.03928) ? green / 12.92 : Math.pow((green + 0.055) / 1.055, 2.4);
        blue = (blue <= 0.03928) ? blue / 12.92 : Math.pow((blue + 0.055) / 1.055, 2.4);

        return 0.2126 * red + 0.7152 * green + 0.0722 * blue;
    }

    /**
     * Removes the hash symbol ('#') from the beginning of the given hex color string if it exists.
     *
     * @param color The hex color string, with or without the '#' symbol at the start.
     * @return The hex color string without the '#' symbol at the start.
     */
    private static String removeHex(String color) {
        return color.startsWith("#") ? color.substring(1) : color;
    }


}
