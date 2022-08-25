import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        boolean one = a > 0 || c > 0;
        boolean two = b > 0 || c > 0;
        boolean tree = a > 0 || b > 0;
        System.out.println(!(one ^ two ^ tree));
        // put your code here
    }
}
