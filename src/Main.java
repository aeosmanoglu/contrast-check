import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Get primary hex color from user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter Primary hex color (ex: #FF5733): ");
        String line = scanner.nextLine();
        scanner.close();

        // Generate color shades
        LinkedList<Color> shades = GenerateShades.generate("primary", line);
        String grayscale = Convert2Grayscale.convert(line);
        LinkedList<Color> grayscaleShades = GenerateShades.generate("grayscale", grayscale);

        // Compare color shades to grayscale shades
        System.out.println("Generating color shades...");
        String csvFileName = Utils.removeHex(line) + "-contrast-ratios.csv";
        Writer.toCSV(shades, grayscaleShades, csvFileName);
        System.out.println(csvFileName + " created!");

    }


}
