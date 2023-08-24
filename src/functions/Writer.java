package functions;

import models.Color;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import static functions.Utils.removeHex;

public class Writer {
    /**
     * Compares two lists of Color objects, calculates the contrast ratio between each pair of corresponding colors
     * according to W3C standards, and writes the results to a CSV file.
     * The contrast ratios are determined based on the relative luminance of the colors.
     *
     * @param list1       The first list of Color objects.
     * @param list2       The second list of Color objects.
     * @param csvFileName The name of the CSV file where the contrast ratios will be written.
     */
    public static void toCSV(LinkedList<Color> list1, LinkedList<Color> list2, String csvFileName) {
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
    private static double calculateContrast(String color1, String color2) {
        double l1 = calculateLuminance(color1);
        double l2 = calculateLuminance(color2);
        double contrast =  (Math.max(l1, l2) + 0.05) / (Math.min(l1, l2) + 0.05);

        return Math.round(contrast * 100.0) / 100.0;
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
}
