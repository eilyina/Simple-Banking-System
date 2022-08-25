import java.util.Scanner;

public class Main {

    public static long factorial(long n) {
        long result = 1;
        // write your code here
        if (n == 0) {
            return 1;
        } else {
            while (n != 0) {

                result = result * n;
                n--;
            }
            return result;
        }
    }

    /* Do not change code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = Integer.parseInt(scanner.nextLine().trim());
        System.out.println(factorial(n));
    }
}