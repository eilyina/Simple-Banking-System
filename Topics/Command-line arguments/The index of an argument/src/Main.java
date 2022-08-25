import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Problem {
    public static void main(String[] args) {
        File file = new File("dataset_91007.txt");
        int max = 0;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                int a = scanner.nextInt();
                if (a > max)
                    max = a;
                //System.out.print(scanner.nextLine() + " ");
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + "dataset_91007.txt");
        }
        System.out.println(max);
    }
}