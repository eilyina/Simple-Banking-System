import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        if (a == b | b ==c | a== c){
            System.out.println(false);
        }
        else {
            System.out.println(true);
        }
        // put your code here
    }
}