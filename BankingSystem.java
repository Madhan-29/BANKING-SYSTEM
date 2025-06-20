import java.util.*;

class Account {
    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private String pin;
    private boolean isActive;

    public Account(String accountNumber, String accountHolderName, double initialDeposit, String pin) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialDeposit;
        this.pin = pin;
        this.isActive = true;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolderName() { return accountHolderName; }
    public void setAccountHolderName(String name) { this.accountHolderName = name; }
    public double getBalance() { return balance; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public boolean verifyPin(String inputPin) { return this.pin.equals(inputPin); }
    public void changePin(String newPin) { this.pin = newPin; }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void withdraw(double amount, String inputPin) {
        if (!verifyPin(inputPin)) {
            System.out.println("Incorrect PIN.");
            return;
        }
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrew: " + amount);
        } else {
            System.out.println("Invalid withdraw amount or insufficient balance.");
        }
    }

    public void displayAccountDetails() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder: " + accountHolderName);
        System.out.println("Balance: " + balance);
        System.out.println("Account Active: " + (isActive ? "Yes" : "No"));
    }
}

public class BankingSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, Account> accounts = new HashMap<>();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nBanking System Menu:");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Account Details");
            System.out.println("5. Check Balance");
            System.out.println("6. Change PIN");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    deposit();
                    break;
                case 3:
                    withdraw();
                    break;
                case 4:
                    accountDetails();
                    break;
                case 5:
                    checkBalance();
                    break;
                case 6:
                    changePin();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void createAccount() {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter Account Holder Name: ");
        String accountHolderName = scanner.nextLine();
        System.out.print("Enter Initial Deposit: ");
        double initialDeposit = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Set PIN: ");
        String pin = scanner.nextLine();

        Account newAccount = new Account(accountNumber, accountHolderName, initialDeposit, pin);
        accounts.put(accountNumber, newAccount);
        System.out.println("Account created successfully.");
    }

    private static Account getAccount() {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        Account account = accounts.get(accountNumber);
        if (account == null || !account.isActive()) {
            System.out.println("No active account found with the given account number.");
            return null;
        }
        return account;
    }

    private static void deposit() {
        Account account = getAccount();
        if (account == null) return;

        System.out.print("Enter deposit amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        account.deposit(amount);
        System.out.println("Updated Account Details:");
        account.displayAccountDetails();
    }

    private static void withdraw() {
        Account account = getAccount();
        if (account == null) return;

        System.out.print("Enter withdraw amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();
        account.withdraw(amount, pin);
        System.out.println("Updated Account Details:");
        account.displayAccountDetails();
    }

    private static void accountDetails() {
        Account account = getAccount();
        if (account == null) return;

        System.out.println("Account Details:");
        account.displayAccountDetails();
    }

    private static void checkBalance() {
        Account account = getAccount();
        if (account == null) return;

        System.out.println("Current Balance: " + account.getBalance());
    }

    private static void changePin() {
        Account account = getAccount();
        if (account == null) return;

        System.out.print("Enter current PIN: ");
        String currentPin = scanner.nextLine();
        if (account.verifyPin(currentPin)) {
            System.out.print("Enter new PIN: ");
            String newPin = scanner.nextLine();
            account.changePin(newPin);
            System.out.println("PIN changed successfully.");
        } else {
            System.out.println("Incorrect current PIN. PIN change failed.");
        }
    }
}
