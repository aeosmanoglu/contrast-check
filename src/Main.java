import models.ColorList;

import java.util.Scanner;

import static functions.Convert2Grayscale.convert;
import static functions.GenerateShades.generate;
import static functions.Utils.removeHex;
import static functions.Writer.toCSV;

public class Main {
    public static void main(String[] args) {

        // Get primary hex color from user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter Primary hex color (ex: #FF5733): ");
        String line = scanner.nextLine();
        scanner.close();

        // Generate color shades
        ColorList shades = generate("primary", line);
        String grayscale = convert(line);
        ColorList grayscaleShades = generate("grayscale", grayscale);

        // Compare color shades to grayscale shades
        System.out.println("Generating color shades...");
        String csvFileName = removeHex(line) + "-contrast-ratios.csv";
        toCSV(shades, grayscaleShades, csvFileName);
        System.out.println(csvFileName + " created!");

    }


}
