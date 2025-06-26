import java.awt.*;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class BankGUI extends JFrame {
    private JTextField accNumField, nameField, depositField;
    private JPasswordField pinField, confirmPinField, pinInputField;
    private JButton createBtn, depositBtn, withdrawBtn, balanceBtn, changePinBtn;
    private Map<String, Account> accounts = new HashMap<>();

    public BankGUI() {
        setTitle("Simple Banking System");
        setSize(600, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Create Account Panel
        JPanel createPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        accNumField = new JTextField();
        nameField = new JTextField();
        depositField = new JTextField();
        pinField = new JPasswordField();
        confirmPinField = new JPasswordField();
        createBtn = new JButton("Create Account");

        createPanel.add(new JLabel("Account Number:"));
        createPanel.add(accNumField);
        createPanel.add(new JLabel("Account Holder Name:"));
        createPanel.add(nameField);
        createPanel.add(new JLabel("Initial Deposit:"));
        createPanel.add(depositField);
        createPanel.add(new JLabel("Set PIN:"));
        createPanel.add(pinField);
        createPanel.add(new JLabel("Confirm PIN:"));
        createPanel.add(confirmPinField);
        createPanel.add(new JLabel());
        createPanel.add(createBtn);
        tabbedPane.add("Create Account", createPanel);

        // Deposit Panel
        JPanel depositPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField depAccField = new JTextField();
        JTextField depAmountField = new JTextField();
        depositBtn = new JButton("Deposit");

        depositPanel.add(new JLabel("Account Number:"));
        depositPanel.add(depAccField);
        depositPanel.add(new JLabel("Amount to Deposit:"));
        depositPanel.add(depAmountField);
        depositPanel.add(new JLabel());
        depositPanel.add(depositBtn);
        tabbedPane.add("Deposit", depositPanel);

        // Withdraw Panel
        JPanel withdrawPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField withAccField = new JTextField();
        JTextField withAmountField = new JTextField();
        pinInputField = new JPasswordField();
        withdrawBtn = new JButton("Withdraw");

        withdrawPanel.add(new JLabel("Account Number:"));
        withdrawPanel.add(withAccField);
        withdrawPanel.add(new JLabel("Amount to Withdraw:"));
        withdrawPanel.add(withAmountField);
        withdrawPanel.add(new JLabel("Enter PIN:"));
        withdrawPanel.add(pinInputField);
        withdrawPanel.add(new JLabel());
        withdrawPanel.add(withdrawBtn);
        tabbedPane.add("Withdraw", withdrawPanel);

        // Balance Panel
        JPanel balancePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField balAccField = new JTextField();
        balanceBtn = new JButton("Check Balance");

        balancePanel.add(new JLabel("Account Number:"));
        balancePanel.add(balAccField);
        balancePanel.add(new JLabel());
        balancePanel.add(balanceBtn);
        tabbedPane.add("Check Balance", balancePanel);

        // Change PIN Panel
        JPanel pinPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField pinAccField = new JTextField();
        JPasswordField currPinField = new JPasswordField();
        JPasswordField newPinField = new JPasswordField();
        changePinBtn = new JButton("Change PIN");

        pinPanel.add(new JLabel("Account Number:"));
        pinPanel.add(pinAccField);
        pinPanel.add(new JLabel("Current PIN:"));
        pinPanel.add(currPinField);
        pinPanel.add(new JLabel("New PIN:"));
        pinPanel.add(newPinField);
        pinPanel.add(new JLabel());
        pinPanel.add(changePinBtn);
        tabbedPane.add("Change PIN", pinPanel);

        // Account Details Panel
        JPanel detailsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton detailsBtn = new JButton("Show All Accounts");

        detailsPanel.add(new JLabel("Click to display all account details:"));
        detailsPanel.add(detailsBtn);
        tabbedPane.add("Account Details", detailsPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // --- Listeners ---

        createBtn.addActionListener(e -> {
            String acc = accNumField.getText().trim();
            String name = nameField.getText().trim();
            if (acc.isEmpty() || name.isEmpty()) {
                showMessage("Account number and name cannot be empty."); return;
            }
            double dep;
            try {
                dep = Double.parseDouble(depositField.getText());
                if (dep < 0) throw new Exception();
            } catch (Exception ex) {
                showMessage("Invalid deposit amount."); return;
            }
            String pin = new String(pinField.getPassword());
            String cpin = new String(confirmPinField.getPassword());
            if (!pin.equals(cpin) || pin.length() != 4) {
                showMessage("PINs do not match or are not 4 digits."); return;
            }
            accounts.put(acc, new Account(acc, name, dep, pin));
            showMessage("✅ Account created!\nName: " + name + "\nBalance: $" + String.format("%.2f", dep));
        });

        depositBtn.addActionListener(e -> {
            Account acc = accounts.get(depAccField.getText().trim());
            if (acc == null) {
                showMessage("⚠️ Account not found."); return;
            }
            double amt;
            try {
                amt = Double.parseDouble(depAmountField.getText());
                if (amt <= 0) throw new Exception();
            } catch (Exception ex) {
                showMessage("Invalid deposit amount."); return;
            }
            acc.deposit(amt);
            showMessage("💰 Deposited $" + amt + "\nNew Balance: $" + String.format("%.2f", acc.getBalance()));
        });

        withdrawBtn.addActionListener(e -> {
            Account acc = accounts.get(withAccField.getText().trim());
            if (acc == null) {
                showMessage("⚠️ Account not found."); return;
            }
            double amt;
            try {
                amt = Double.parseDouble(withAmountField.getText());
                if (amt <= 0) throw new Exception();
            } catch (Exception ex) {
                showMessage("Invalid withdrawal amount."); return;
            }
            String pin = new String(pinInputField.getPassword());
            if (!acc.verifyPin(pin)) {
                showMessage("❌ Incorrect PIN."); return;
            }
            if (!acc.withdraw(amt)) {
                showMessage("⚠️ Insufficient balance."); return;
            }
            showMessage("💸 Withdrawn $" + amt + "\nNew Balance: $" + String.format("%.2f", acc.getBalance()));
        });

        balanceBtn.addActionListener(e -> {
            Account acc = accounts.get(balAccField.getText().trim());
            if (acc == null) {
                showMessage("⚠️ Account not found."); return;
            }
            showMessage("📊 Current Balance: $" + String.format("%.2f", acc.getBalance()));
        });

        changePinBtn.addActionListener(e -> {
            Account acc = accounts.get(pinAccField.getText().trim());
            if (acc == null) {
                showMessage("⚠️ Account not found."); return;
            }
            String oldPin = new String(currPinField.getPassword());
            String newPin = new String(newPinField.getPassword());
            if (!acc.verifyPin(oldPin)) {
                showMessage("❌ Incorrect current PIN."); return;
            }
            if (newPin.length() != 4) {
                showMessage("PIN must be exactly 4 digits."); return;
            }
            acc.changePin(newPin);
            showMessage("🔑 PIN changed successfully.");
        });

        detailsBtn.addActionListener(e -> {
            if (accounts.isEmpty()) {
                showMessage("📂 No accounts available.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("📋 All Account Details:\n");
            for (Account acc : accounts.values()) {
                sb.append("Account Number: ").append(acc.getAccountNumber()).append("\n");
                sb.append("Name: ").append(acc.getAccountHolderName()).append("\n");
                sb.append("Balance: $").append(String.format("%.2f", acc.getBalance())).append("\n");
                sb.append("-----------------------------\n");
            }
            showMessage(sb.toString());
        });

        setVisible(true);
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public static void main(String[] args) {
        new BankGUI();
    }
}

class Account {
    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private String pinHash;

    public Account(String accNo, String holderName, double initialDeposit, String pin) {
        this.accountNumber = accNo;
        this.accountHolderName = holderName;
        this.balance = initialDeposit;
        this.pinHash = hash(pin);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public boolean verifyPin(String inputPin) {
        return pinHash.equals(hash(inputPin));
    }

    public void changePin(String newPin) {
        this.pinHash = hash(newPin);
    }

    public void deposit(double amount) {
        if (amount > 0) balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    private String hash(String pin) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(pin.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
}