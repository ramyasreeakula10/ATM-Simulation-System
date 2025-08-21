package ATM_PROJECT;

import java.sql.*;
import java.util.*;

// Step 1: Create interface
interface ATMOperations {
    void balanceEnquiry();
    void depositMoney();
    void withdrawMoney();
    void transactionHistory();
}

// Step 2: Implement interface
class Atm implements ATMOperations {
    private Connection con;
    private String pin;
    private List<String> transactions;
    private Scanner sc;

    public Atm(String pin) {
        this.pin = pin;
        this.transactions = new ArrayList<>();
        this.sc = new Scanner(System.in);

        try {
            //  Establish DB connection
            String url = "jdbc:mysql://127.0.0.1:3306/ATM_DB";
            String uname = "root";    // your MySQL username
            String pass = "ramya";    // your MySQL password
            con = DriverManager.getConnection(url, uname, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  Get balance from DB
    private double getBalance() {
        try {
            String query = "SELECT balance FROM users WHERE pin=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, pin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //  Update balance in DB
    private void updateBalance(double newBalance) {
        try {
            String query = "UPDATE users SET balance=? WHERE pin=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setDouble(1, newBalance);
            ps.setString(2, pin);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void balanceEnquiry() {
        double balance = getBalance();
        System.out.println("Balance Enquiry:\nYour current balance is: " + balance);
        transactions.add("Checked balance: " + balance);
    }

    public void depositMoney() {
        System.out.println("Enter amount to deposit:");
        double amount = sc.nextDouble();
        if (amount > 0) {
            double balance = getBalance() + amount;
            updateBalance(balance);
            System.out.println("Deposited: " + amount);
            System.out.println("Updated Balance: " + balance);
            transactions.add("Deposited: " + amount);
        } else {
            System.out.println("Invalid deposit amount!");
        }
    }

    public void withdrawMoney() {
        System.out.println("Enter amount to withdraw:");
        double amount = sc.nextDouble();
        double balance = getBalance();
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            updateBalance(balance);
            System.out.println("Withdrawn: " + amount);
            System.out.println("Updated Balance: " + balance);
            transactions.add("Withdrew: " + amount);
        } else if (amount > balance) {
            System.out.println("Insufficient balance!");
        } else {
            System.out.println("Invalid withdrawal amount!");
        }
    }

    public void transactionHistory() {
        System.out.println("Transaction History:");
        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (String txn : transactions) {
                System.out.println(txn);
            }
        }
    }
}

// 🔹 Step 3: Main class
public class Atm_Project {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Step 1: PIN validation from DB
        System.out.print("Enter PIN: ");
        String enteredPin = sc.nextLine();

        try {
            String url = "jdbc:mysql://127.0.0.1:3306/ATM_DB";
            String uname = "root";
            String pass = "ramya";
            Connection con = DriverManager.getConnection(url, uname, pass);

            String query = "SELECT * FROM users WHERE pin=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, enteredPin);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("INVALID PIN");
                return;
            }

            System.out.println("Login successful!\n");

            // Step 2: Start ATM for this user
            Atm atm = new Atm(enteredPin);

            while (true) {
                System.out.println("\n===== ATM Menu =====");
                System.out.println("1. Balance Enquiry");
                System.out.println("2. Deposit Money");
                System.out.println("3. Withdraw Money");
                System.out.println("4. Transaction History");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");

                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        atm.balanceEnquiry();
                        break;
                    case 2:
                        atm.depositMoney();
                        break;
                    case 3:
                        atm.withdrawMoney();
                        break;
                    case 4:
                        atm.transactionHistory();
                        break;
                    case 5:
                        System.out.println("Thank you for using the ATM!");
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
