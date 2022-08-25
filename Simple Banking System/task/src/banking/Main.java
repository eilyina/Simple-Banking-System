package banking;
/*ou have created the foundation of our banking system. Now let's take the opportunity to deposit money into an account, make transfers and close an account if necessary.
Now your menu should look like this:
1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
If the user asks for Balance, you should read the balance of the account from the database and output it into the console.
Add income item should allow us to deposit money to the account.
Do transfer item should allow transferring money to another account. You should handle the following errors:
If the user tries to transfer more money than he/she has, output: Not enough money!
If the user tries to transfer money to the same account, output the following message: You can't transfer money to the same account!
If the receiver's card number doesn’t pass the Luhn algorithm, you should output: Probably you made a mistake in the card number. Please try again!
If the receiver's card number doesn’t exist, you should output: Such a card does not exist.
If there is no error, ask the user how much money they want to transfer and make the transaction.
If the user chooses the Close an account item, you should delete that account from the database.
Examples
The symbol > represents the user input. Notice that it's not a part of the input.
Example 1:
1. Create an account
2. Log into account
0. Exit
>1

Your card has been created
Your card number:
4000009455296122
Your card PIN:
1961

1. Create an account
2. Log into account
0. Exit
>1

Your card has been created
Your card number:
4000003305160034
Your card PIN:
5639

1. Create an account
2. Log into account
0. Exit
>2

Enter your card number:
>4000009455296122
Enter your PIN:
>1961

You have successfully logged in!

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>2

Enter income:
>10000
Income was added!

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>1

Balance: 10000

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>3

Transfer
Enter card number:
>4000003305160035
Probably you made a mistake in the card number. Please try again!

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>3

Transfer
Enter card number:
>4000003305061034
Such a card does not exist.

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>3

Transfer
Enter card number:
>4000003305160034
Enter how much money you want to transfer:
>15000
Not enough money!

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>3

Transfer
Enter card number:
>4000003305160034
Enter how much money you want to transfer:
>5000
Success!

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>1

Balance: 5000

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit

>0
Bye!
Example 2:
1. Create an account
2. Log into account
0. Exit
>1

Your card has been created
Your card number:
4000007916053702
Your card PIN:
6263

1. Create an account
2. Log into account
0. Exit
>2

Enter your card number:
>4000007916053702
Enter your PIN:
>6263

You have successfully logged in!

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>4

The account has been closed!

1. Create an account
2. Log into account
0. Exit
>2

Enter your card number:
>4000007916053702
Enter your PIN:
>6263

Wrong card number or PIN!

1. Create an account
2. Log into account
0. Exit
>0

Bye!*/
import java.sql.*;
import java.util.Random;
import java.util.Scanner;

import org.sqlite.SQLiteDataSource;


class Card {
    public StringBuilder cardNumber;
    public String PIN;

    public Card(StringBuilder cardNumber, String PIN) {
        this.cardNumber = cardNumber;
        this.PIN = PIN;
    }

    public StringBuilder getCardNumber() {
        return cardNumber;
    }


    public void setCardNumber() {
        cardNumber = new StringBuilder("");
        this.cardNumber.append("400000");
        int min = 100000000;
        int max = 9_999_999_99;
        int diff = max - min;
        Random random = new Random();
        int i = random.nextInt(diff + 1);
        i += min;
        cardNumber.append(i);
        // i = random.nextInt(9);
        // i += 1;
        // cardNumber.append(i);
        int[] array = new int[15];
        int summEl = 0;
        for (int o = 0; o < cardNumber.length(); o++) {
            array[o] = Integer.parseInt(cardNumber.substring(o, o + 1));
            if (o % 2 == 0) {
                array[o] = array[o] * 2;
            }
            if (array[o] > 9) {
                array[o] = array[o] - 9;
            }
            summEl += array[o];
        }
        int lastIndex = 0;
        while (summEl % 10 != 0) {
            lastIndex++;
            summEl++;
       /*     if (summEl % 10 == 0){
                break;
            }*/
        }
        cardNumber.append(lastIndex);

    }

    public String getPIN() {

        return PIN;
    }

    public void setPIN() {
        int min = 0000;
        int max = 9999;
        int diff = max - min;
        Random random = new Random();
        int i = random.nextInt(diff + 1);
        i += min;
        this.PIN = String.format("%04d", i);
    }
}


public class Main {
    public static boolean isLuhn(String number) {
        int s1 = 0, s2 = 0;
        String reverse = new StringBuffer(number).reverse().toString();
        for (int i = 0; i < reverse.length(); i++) {
            int digit = Character.digit(reverse.charAt(i), 10);
            if (i % 2 == 0) {//this is for odd digits, they are 1-indexed in the algorithm
                s1 += digit;
            } else {//add 2 * digit for 0-4, add 2 * digit - 9 for 5-9
                s2 += 2 * digit;
                if (digit >= 5) {
                    s2 -= 9;
                }
            }
        }
        return (s1 + s2) % 10 == 0;
    }

    public static void printMenuInAccount() {
        System.out.println("1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit");
    }

    public static void printMenu() {
        System.out.println("1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit");
    }

    public static void main(String[] args) throws SQLException {

        String url = "jdbc:sqlite:" + args[1];
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);


        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (\n" +
                        "id INTEGER PRIMARY KEY,\n" +
                        "number TEXT NOT NULL\n," +
                        "pin TEXT NOT NULL,\n" +
                        "balance INTEGER DEFAULT 0)");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        Scanner scanner = new Scanner(System.in);
        /*int elementMenu = scanner.nextInt();*/
        StringBuilder olo = new StringBuilder("");
        Card card = new Card(olo, "");
        // card.setCardNumber();
        boolean logout = true;
        while (true) {
            printMenu();
            scanner = new Scanner(System.in);
            int elementMenu = scanner.nextInt();
            switch (elementMenu) {
                case (1):

                    card.setCardNumber();
                    card.setPIN();
                    try (Connection con = dataSource.getConnection()) {

                        // Statement creation
                        try (Statement statement = con.createStatement()) {
                            // Statement execution

                            statement.executeUpdate("INSERT INTO card( number, pin) VALUES " +
                                    "(" + card.cardNumber + "," + card.PIN + ")");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Your card has been created\n" +
                            "Your card number:\n" +
                            card.getCardNumber() + "\n" +
                            "Your card PIN:\n" + card.getPIN());
                    break;
                case (2):

                    System.out.println("Enter your card number:");
                    String userCardNumber = scanner.next();
                    System.out.println("Enter your PIN:");
                    String PIN = scanner.next();

                    try (Connection con = dataSource.getConnection()) {

                        // Statement creation
                        try (Statement statement = con.createStatement()) {
                            // Statement execution

                            ResultSet result = statement.executeQuery("SELECT * FROM card " +
                                    "WHERE number = '"
                                    + userCardNumber + "' AND PIN = '" + PIN + "'"
                            );
                            if (result.next()) {
                                System.out.println("You have successfully logged in!");

                                int elementMenu2 = 3;
                                while (elementMenu2 != 5 && logout) {
                                    printMenuInAccount();
                                    scanner = new Scanner(System.in);
                                    elementMenu2 = scanner.nextInt();
                                    result = statement.executeQuery("SELECT * FROM card " +
                                            "WHERE number = '"
                                            + userCardNumber + "' AND PIN = '" + PIN + "'"
                                    );
                                    int balance = result.getInt("balance");
                                    switch (elementMenu2) {
                                        case (1):
                                            //   System.out.println(" Balance: 0");

                                            System.out.printf("Balance: %d%n", balance);
                                            break;
                                        case (2):
                                            System.out.println("Enter income:");
                                            int income = scanner.nextInt();
                                            int summ = income + result.getInt("balance");


                                            statement.executeUpdate("UPDATE card SET balance = '" +
                                                    summ +
                                                    "' WHERE id = '" + result.getInt("id") + "'");


                                            System.out.println("Income was added!");
                                            break;
                                        case (3):
                                            System.out.println("Transfer\n" +
                                                    "Enter card number:");
                                            String recipientCardNumber = scanner.next();
                                            if (recipientCardNumber.equals(userCardNumber)) {
                                                System.out.println("You can't transfer money to the same account!");
                                                break;
                                            }
                                            if (!(isLuhn(recipientCardNumber))) {
                                                System.out.println("Probably you made a mistake in the card number. Please try again!");
                                                break;
                                            } else {
                                                ResultSet recipientBalance = statement.executeQuery("SELECT number FROM card " +
                                                        "WHERE number = '"
                                                        + recipientCardNumber + "'"
                                                );
                                                if (!(recipientBalance.next())) {
                                                    System.out.println("Such a card does not exist.");
                                                    break;

                                                }//условие если карта не найдена
                                            }

                                            System.out.println("Enter how much money you want to transfer:");
                                            int transferSum = scanner.nextInt();
                                            if (transferSum > balance) {
                                                System.out.println("Not enough money!");
                                                break;


                                            } else {
                                                ResultSet recipientBalance = statement.executeQuery("SELECT number, balance FROM card " +
                                                        "WHERE number = '"
                                                        + recipientCardNumber + "'"
                                                );
                                                int totalResp = transferSum + recipientBalance.getInt("balance");
                                                int totalUser = balance - transferSum;
                                                statement.executeUpdate("UPDATE card SET balance = '" +
                                                        totalUser +
                                                        "' WHERE number = '" + userCardNumber + "'");
                                                statement.executeUpdate("UPDATE card SET balance = '" +
                                                        totalResp +
                                                        "' WHERE number = '" + recipientCardNumber + "'");
                                                System.out.println("Success!");
                                                break;
                                            }

                                        case (4):
                                            statement.executeUpdate("DELETE FROM card WHERE number = " + userCardNumber);
                                            System.out.println("The account has been closed!");
                                            logout = false;
                                            break;
                                        case (5):
                                            logout = false;
                                            System.out.println("You have successfully logged out!");
                                            break;
                                        case (0):
                                            System.out.println("Bye !");
                                            con.close();
                                            scanner.close();
                                            System.exit(0);

                                    }
                                }
                            } else {
                                System.out.println("Wrong card number or PIN!");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                    break;

                case (0):
                    System.out.println("Bye !");
                    System.exit(0);

            }
        }

    }
}
