import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.List;

/**
 * ============================================================================
 * PAYMENT GATEWAY SIMULATION - OOT Mini Project
 * ============================================================================
 * 
 * Features Implemented:
 * - Design Patterns: Strategy Pattern (Fee Calculation), Factory Pattern (Payment Method Creation)
 * - OOP Concepts: Classes, Inheritance, Polymorphism, Encapsulation, Abstraction
 * - File I/O: Transaction history saved to CSV file
 * - Exception Handling: Custom exceptions and error handling
 * - GUI: Interactive Swing-based UI with modern design
 * 
 * Compile: javac PaymentGateway.java
 * Run: java PaymentGateway
 * 
 * ============================================================================
 */

// ============================================================================
// 1. CUSTOM EXCEPTION HANDLING
// ============================================================================

class PaymentException extends Exception {
    public PaymentException(String message) {
        super(message);
    }
}

class InsufficientFundsException extends PaymentException {
    public InsufficientFundsException() {
        super("Insufficient funds in account");
    }
}

class InvalidCardException extends PaymentException {
    public InvalidCardException() {
        super("Invalid card details");
    }
}

class InvalidUPIException extends PaymentException {
    public InvalidUPIException() {
        super("Invalid UPI ID");
    }
}

class BankServerException extends PaymentException {
    public BankServerException() {
        super("Bank server is down. Please try again later.");
    }
}

// ============================================================================
// 2. STRATEGY PATTERN - Fee Calculation Strategy
// ============================================================================

interface FeeStrategy {
    double calculateFee(double amount);
    String getFeeName();
}

class CardFeeStrategy implements FeeStrategy {
    @Override
    public double calculateFee(double amount) {
        return amount * 0.02; // 2% fee for cards
    }
    
    @Override
    public String getFeeName() {
        return "Card Processing Fee (2%)";
    }
}

class UPIFeeStrategy implements FeeStrategy {
    @Override
    public double calculateFee(double amount) {
        return 0.0; // No fee for UPI
    }
    
    @Override
    public String getFeeName() {
        return "UPI (No Fee)";
    }
}

class NetBankingFeeStrategy implements FeeStrategy {
    @Override
    public double calculateFee(double amount) {
        return amount * 0.01; // 1% fee for net banking
    }
    
    @Override
    public String getFeeName() {
        return "Net Banking Fee (1%)";
    }
}

class WalletFeeStrategy implements FeeStrategy {
    @Override
    public double calculateFee(double amount) {
        return 0.0; // No fee for wallet
    }
    
    @Override
    public String getFeeName() {
        return "Wallet (No Fee)";
    }
}

// ============================================================================
// 3. ABSTRACT CLASS & INHERITANCE - Payment Method Hierarchy
// ============================================================================

abstract class PaymentMethod {
    protected String methodName;
    protected FeeStrategy feeStrategy;
    
    public PaymentMethod(String methodName, FeeStrategy feeStrategy) {
        this.methodName = methodName;
        this.feeStrategy = feeStrategy;
    }
    
    // Abstract method - must be implemented by subclasses
    public abstract boolean validatePayment() throws PaymentException;
    
    // Abstract method for processing
    public abstract String getPaymentDetails();
    
    // Polymorphism - can be overridden
    public boolean processPayment(double amount) throws PaymentException {
        if (!validatePayment()) {
            throw new PaymentException("Validation failed for " + methodName);
        }
        
        // Simulate random failures (10% chance)
        Random random = new Random();
        if (random.nextInt(10) == 0) {
            throw new BankServerException();
        }
        
        return true;
    }
    
    public double calculateFee(double amount) {
        return feeStrategy.calculateFee(amount);
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public String getFeeName() {
        return feeStrategy.getFeeName();
    }
}

// Credit Card Payment
class CreditCard extends PaymentMethod {
    private String cardNumber;
    private String holderName;
    private String expiryDate;
    private String cvv;
    
    public CreditCard(String cardNumber, String holderName, String expiryDate, String cvv) {
        super("Credit Card", new CardFeeStrategy());
        this.cardNumber = cardNumber;
        this.holderName = holderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }
    
    @Override
    public boolean validatePayment() throws PaymentException {
        if (cardNumber == null || cardNumber.length() != 16) {
            throw new InvalidCardException();
        }
        if (cvv == null || cvv.length() != 3) {
            throw new InvalidCardException();
        }
        if (holderName == null || holderName.trim().isEmpty()) {
            throw new InvalidCardException();
        }
        return true;
    }
    
    @Override
    public String getPaymentDetails() {
        return "Credit Card - **** **** **** " + cardNumber.substring(12) + " | " + holderName;
    }
}

// Debit Card Payment
class DebitCard extends PaymentMethod {
    private String cardNumber;
    private String holderName;
    private String expiryDate;
    private String cvv;
    
    public DebitCard(String cardNumber, String holderName, String expiryDate, String cvv) {
        super("Debit Card", new CardFeeStrategy());
        this.cardNumber = cardNumber;
        this.holderName = holderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }
    
    @Override
    public boolean validatePayment() throws PaymentException {
        if (cardNumber == null || cardNumber.length() != 16) {
            throw new InvalidCardException();
        }
        if (cvv == null || cvv.length() != 3) {
            throw new InvalidCardException();
        }
        return true;
    }
    
    @Override
    public String getPaymentDetails() {
        return "Debit Card - **** **** **** " + cardNumber.substring(12) + " | " + holderName;
    }
}

// UPI Payment
class UPI extends PaymentMethod {
    private String upiId;
    
    public UPI(String upiId) {
        super("UPI", new UPIFeeStrategy());
        this.upiId = upiId;
    }
    
    @Override
    public boolean validatePayment() throws PaymentException {
        if (upiId == null || !upiId.contains("@")) {
            throw new InvalidUPIException();
        }
        return true;
    }
    
    @Override
    public String getPaymentDetails() {
        return "UPI - " + upiId;
    }
}

// Net Banking Payment
class NetBanking extends PaymentMethod {
    private String bankName;
    private String accountNumber;
    
    public NetBanking(String bankName, String accountNumber) {
        super("Net Banking", new NetBankingFeeStrategy());
        this.bankName = bankName;
        this.accountNumber = accountNumber;
    }
    
    @Override
    public boolean validatePayment() throws PaymentException {
        if (bankName == null || bankName.trim().isEmpty()) {
            throw new PaymentException("Bank name is required");
        }
        if (accountNumber == null || accountNumber.length() < 8) {
            throw new PaymentException("Invalid account number");
        }
        return true;
    }
    
    @Override
    public String getPaymentDetails() {
        return "Net Banking - " + bankName + " | A/C: ****" + accountNumber.substring(accountNumber.length() - 4);
    }
}

// Digital Wallet Payment
class Wallet extends PaymentMethod {
    private String walletProvider;
    private String mobileNumber;
    
    public Wallet(String walletProvider, String mobileNumber) {
        super("Wallet", new WalletFeeStrategy());
        this.walletProvider = walletProvider;
        this.mobileNumber = mobileNumber;
    }
    
    @Override
    public boolean validatePayment() throws PaymentException {
        if (mobileNumber == null || mobileNumber.length() != 10) {
            throw new PaymentException("Invalid mobile number");
        }
        return true;
    }
    
    @Override
    public String getPaymentDetails() {
        return "Wallet - " + walletProvider + " | " + mobileNumber;
    }
}

// ============================================================================
// 4. FACTORY PATTERN - Payment Method Factory
// ============================================================================

class PaymentMethodFactory {
    public static PaymentMethod createPaymentMethod(String type, Map<String, String> details) throws PaymentException {
        switch (type.toUpperCase()) {
            case "CREDIT CARD":
                return new CreditCard(
                    details.get("cardNumber"),
                    details.get("holderName"),
                    details.get("expiryDate"),
                    details.get("cvv")
                );
            
            case "DEBIT CARD":
                return new DebitCard(
                    details.get("cardNumber"),
                    details.get("holderName"),
                    details.get("expiryDate"),
                    details.get("cvv")
                );
            
            case "UPI":
                return new UPI(details.get("upiId"));
            
            case "NET BANKING":
                return new NetBanking(
                    details.get("bankName"),
                    details.get("accountNumber")
                );
            
            case "WALLET":
                return new Wallet(
                    details.get("walletProvider"),
                    details.get("mobileNumber")
                );
            
            default:
                throw new PaymentException("Unknown payment method: " + type);
        }
    }
}

// ============================================================================
// 5. TRANSACTION CLASS - Data Encapsulation
// ============================================================================

class Transaction {
    private String transactionId;
    private String paymentMethod;
    private double amount;
    private double fee;
    private double totalAmount;
    private String status;
    private Date timestamp;
    private String paymentDetails;
    
    public Transaction(String transactionId, String paymentMethod, double amount, 
                       double fee, String status, String paymentDetails) {
        this.transactionId = transactionId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.fee = fee;
        this.totalAmount = amount + fee;
        this.status = status;
        this.timestamp = new Date();
        this.paymentDetails = paymentDetails;
    }
    
    // Getters (Encapsulation)
    public String getTransactionId() { return transactionId; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getAmount() { return amount; }
    public double getFee() { return fee; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public Date getTimestamp() { return timestamp; }
    public String getPaymentDetails() { return paymentDetails; }
    
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format("%s,%s,%.2f,%.2f,%.2f,%s,%s,%s",
            transactionId, paymentMethod, amount, fee, totalAmount, 
            status, sdf.format(timestamp), paymentDetails);
    }
}

// ============================================================================
// 6. PAYMENT PROCESSOR - Business Logic
// ============================================================================

class PaymentProcessor {
    private List<Transaction> transactionHistory;
    private static final String TRANSACTION_FILE = "transaction_history.csv";
    
    public PaymentProcessor() {
        transactionHistory = new ArrayList<>();
        loadTransactionHistory();
    }
    
    public Transaction processPayment(PaymentMethod paymentMethod, double amount) throws PaymentException {
        // Generate transaction ID
        String txnId = "TXN" + System.currentTimeMillis();
        
        // Calculate fee
        double fee = paymentMethod.calculateFee(amount);
        
        try {
            // Process payment
            paymentMethod.processPayment(amount);
            
            // Create transaction
            Transaction transaction = new Transaction(
                txnId,
                paymentMethod.getMethodName(),
                amount,
                fee,
                "SUCCESS",
                paymentMethod.getPaymentDetails()
            );
            
            // Add to history
            transactionHistory.add(transaction);
            
            // Save to file
            saveTransaction(transaction);
            
            return transaction;
            
        } catch (PaymentException e) {
            // Create failed transaction
            Transaction transaction = new Transaction(
                txnId,
                paymentMethod.getMethodName(),
                amount,
                fee,
                "FAILED",
                paymentMethod.getPaymentDetails()
            );
            
            transactionHistory.add(transaction);
            saveTransaction(transaction);
            
            throw e;
        }
    }
    
    // File I/O - Save transaction
    private void saveTransaction(Transaction transaction) {
        try (FileWriter fw = new FileWriter(TRANSACTION_FILE, true)) {
            File file = new File(TRANSACTION_FILE);
            
            // Write header if file is new
            if (file.length() == 0) {
                fw.write("TransactionID,PaymentMethod,Amount,Fee,TotalAmount,Status,Timestamp,Details\n");
            }
            
            fw.write(transaction.toString() + "\n");
            
        } catch (IOException e) {
            System.err.println("Error saving transaction: " + e.getMessage());
        }
    }
    
    // File I/O - Load transaction history
    private void loadTransactionHistory() {
        File file = new File(TRANSACTION_FILE);
        if (!file.exists()) {
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header
            
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    Transaction txn = new Transaction(
                        parts[0], parts[1], 
                        Double.parseDouble(parts[2]),
                        Double.parseDouble(parts[3]),
                        parts[5], parts[7]
                    );
                    transactionHistory.add(txn);
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error loading transaction history: " + e.getMessage());
        }
    }
    
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }
    
    public double getTotalRevenue() {
        return transactionHistory.stream()
            .filter(t -> "SUCCESS".equals(t.getStatus()))
            .mapToDouble(Transaction::getAmount)
            .sum();
    }
    
    public int getSuccessfulTransactionCount() {
        return (int) transactionHistory.stream()
            .filter(t -> "SUCCESS".equals(t.getStatus()))
            .count();
    }
}

// ============================================================================
// 7. GUI - MAIN APPLICATION
// ============================================================================

public class PaymentGateway extends JFrame {
    private PaymentProcessor processor;
    private JTextField amountField;
    private JComboBox<String> paymentMethodCombo;
    private CardLayout cardLayout;
    private JPanel formPanel;
    private Map<String, JTextField> inputFields;
    
    // Colors
    private static final Color PRIMARY_COLOR = new Color(26, 115, 232);
    private static final Color SUCCESS_COLOR = new Color(52, 168, 83);
    private static final Color ERROR_COLOR = new Color(234, 67, 53);
    private static final Color BG_COLOR = new Color(248, 249, 250);
    
    public PaymentGateway() {
        processor = new PaymentProcessor();
        inputFields = new HashMap<>();
        
        setTitle("Payment Gateway Simulator - OOT Mini Project");
        setSize(600, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        initComponents();
        setVisible(true);
    }
    
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Payment Form
        JPanel paymentPanel = createPaymentPanel();
        mainPanel.add(paymentPanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Payment Gateway", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Secure Payment Processing", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 180));
        
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(subtitleLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(218, 220, 224)),
            new EmptyBorder(30, 40, 30, 40)
        ));
        
        // Amount Section
        JLabel amountLabel = createLabel("Enter Amount (₹)");
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(amountLabel);
        panel.add(Box.createVerticalStrut(5));
        amountField = createTextField();
        amountField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(amountField);
        panel.add(Box.createVerticalStrut(20));
        
        // Payment Method Section
        JLabel methodLabel = createLabel("Select Payment Method");
        methodLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(methodLabel);
        panel.add(Box.createVerticalStrut(5));
        String[] methods = {"Credit Card", "Debit Card", "UPI", "Net Banking", "Wallet"};
        paymentMethodCombo = new JComboBox<>(methods);
        paymentMethodCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        paymentMethodCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        paymentMethodCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        paymentMethodCombo.addActionListener(e -> updateFormPanel());
        panel.add(paymentMethodCombo);
        panel.add(Box.createVerticalStrut(20));
        
        // Dynamic Form Panel
        cardLayout = new CardLayout();
        formPanel = new JPanel(cardLayout);
        formPanel.setBackground(Color.WHITE);
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        formPanel.add(createCardForm(), "CARD");
        formPanel.add(createUPIForm(), "UPI");
        formPanel.add(createNetBankingForm(), "NETBANKING");
        formPanel.add(createWalletForm(), "WALLET");
        
        panel.add(formPanel);
        
        return panel;
    }
    
    private JPanel createCardForm() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel cardNumLabel = createLabel("Card Number");
        cardNumLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(cardNumLabel);
        panel.add(Box.createVerticalStrut(5));
        JTextField cardNumber = createTextField();
        inputFields.put("cardNumber", cardNumber);
        panel.add(cardNumber);
        panel.add(Box.createVerticalStrut(12));
        
        JLabel holderLabel = createLabel("Cardholder Name");
        holderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(holderLabel);
        panel.add(Box.createVerticalStrut(5));
        JTextField holderName = createTextField();
        inputFields.put("holderName", holderName);
        panel.add(holderName);
        panel.add(Box.createVerticalStrut(12));
        
        JPanel row = new JPanel(new GridLayout(1, 2, 15, 0));
        row.setBackground(Color.WHITE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel expiryPanel = new JPanel();
        expiryPanel.setLayout(new BoxLayout(expiryPanel, BoxLayout.Y_AXIS));
        expiryPanel.setBackground(Color.WHITE);
        JLabel expiryLabel = createLabel("Expiry (MM/YY)");
        expiryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        expiryPanel.add(expiryLabel);
        expiryPanel.add(Box.createVerticalStrut(5));
        JTextField expiry = createTextField();
        inputFields.put("expiryDate", expiry);
        expiryPanel.add(expiry);
        
        JPanel cvvPanel = new JPanel();
        cvvPanel.setLayout(new BoxLayout(cvvPanel, BoxLayout.Y_AXIS));
        cvvPanel.setBackground(Color.WHITE);
        JLabel cvvLabel = createLabel("CVV");
        cvvLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cvvPanel.add(cvvLabel);
        cvvPanel.add(Box.createVerticalStrut(5));
        JTextField cvv = createTextField();
        inputFields.put("cvv", cvv);
        cvvPanel.add(cvv);
        
        row.add(expiryPanel);
        row.add(cvvPanel);
        panel.add(row);
        
        mainPanel.add(panel, BorderLayout.WEST);
        return mainPanel;
    }
    
    private JPanel createUPIForm() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel upiLabel = createLabel("UPI ID (e.g., user@paytm)");
        upiLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(upiLabel);
        panel.add(Box.createVerticalStrut(5));
        JTextField upiId = createTextField();
        inputFields.put("upiId", upiId);
        panel.add(upiId);
        panel.add(Box.createVerticalStrut(60));
        
        mainPanel.add(panel, BorderLayout.WEST);
        return mainPanel;
    }
    
    private JPanel createNetBankingForm() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel bankLabel = createLabel("Bank Name");
        bankLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(bankLabel);
        panel.add(Box.createVerticalStrut(5));
        JTextField bankName = createTextField();
        inputFields.put("bankName", bankName);
        panel.add(bankName);
        panel.add(Box.createVerticalStrut(12));
        
        JLabel accountLabel = createLabel("Account Number");
        accountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(accountLabel);
        panel.add(Box.createVerticalStrut(5));
        JTextField accountNumber = createTextField();
        inputFields.put("accountNumber", accountNumber);
        panel.add(accountNumber);
        panel.add(Box.createVerticalStrut(20));
        
        mainPanel.add(panel, BorderLayout.WEST);
        return mainPanel;
    }
    
    private JPanel createWalletForm() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel walletLabel = createLabel("Wallet Provider");
        walletLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(walletLabel);
        panel.add(Box.createVerticalStrut(5));
        String[] wallets = {"Paytm", "PhonePe", "Google Pay", "Amazon Pay"};
        JComboBox<String> walletCombo = new JComboBox<>(wallets);
        walletCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        walletCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        walletCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputFields.put("walletProvider", new JTextField() {
            @Override
            public String getText() {
                return (String) walletCombo.getSelectedItem();
            }
        });
        panel.add(walletCombo);
        panel.add(Box.createVerticalStrut(12));
        
        JLabel mobileLabel = createLabel("Mobile Number");
        mobileLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(mobileLabel);
        panel.add(Box.createVerticalStrut(5));
        JTextField mobile = createTextField();
        inputFields.put("mobileNumber", mobile);
        panel.add(mobile);
        panel.add(Box.createVerticalStrut(20));
        
        mainPanel.add(panel, BorderLayout.WEST);
        return mainPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 0));
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton payButton = createButton("Pay Now", PRIMARY_COLOR);
        payButton.addActionListener(e -> processPayment());
        
        JButton historyButton = createButton("History", new Color(95, 99, 104));
        historyButton.addActionListener(e -> showTransactionHistory());
        
        JButton clearButton = createButton("Clear", new Color(95, 99, 104));
        clearButton.addActionListener(e -> clearForm());
        
        panel.add(payButton);
        panel.add(historyButton);
        panel.add(clearButton);
        
        return panel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }
    
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(218, 220, 224)),
            new EmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 45));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        return button;
    }
    
    private void updateFormPanel() {
        String selected = (String) paymentMethodCombo.getSelectedItem();
        
        if (selected.contains("Card")) {
            cardLayout.show(formPanel, "CARD");
        } else if (selected.equals("UPI")) {
            cardLayout.show(formPanel, "UPI");
        } else if (selected.equals("Net Banking")) {
            cardLayout.show(formPanel, "NETBANKING");
        } else if (selected.equals("Wallet")) {
            cardLayout.show(formPanel, "WALLET");
        }
    }
    
    private void processPayment() {
        try {
            // Validate amount
            String amountText = amountField.getText().trim();
            if (amountText.isEmpty()) {
                showError("Please enter an amount");
                return;
            }
            
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showError("Amount must be greater than 0");
                return;
            }
            
            // Get payment details
            String methodType = (String) paymentMethodCombo.getSelectedItem();
            Map<String, String> details = new HashMap<>();
            
            for (Map.Entry<String, JTextField> entry : inputFields.entrySet()) {
                details.put(entry.getKey(), entry.getValue().getText().trim());
            }
            
            // Create payment method using Factory Pattern
            PaymentMethod paymentMethod = PaymentMethodFactory.createPaymentMethod(methodType, details);
            
            // Show processing dialog
            JDialog processingDialog = showProcessingDialog();
            
            // Process payment in background
            new Thread(() -> {
                try {
                    Thread.sleep(1500); // Simulate processing time
                    
                    Transaction transaction = processor.processPayment(paymentMethod, amount);
                    
                    SwingUtilities.invokeLater(() -> {
                        processingDialog.dispose();
                        showSuccessDialog(transaction);
                        clearForm();
                    });
                    
                } catch (PaymentException e) {
                    SwingUtilities.invokeLater(() -> {
                        processingDialog.dispose();
                        showError("Payment Failed: " + e.getMessage());
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            
        } catch (NumberFormatException e) {
            showError("Invalid amount format");
        } catch (PaymentException e) {
            showError("Error: " + e.getMessage());
        }
    }
    
    private JDialog showProcessingDialog() {
        JDialog dialog = new JDialog(this, "Processing Payment", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel label = new JLabel("Processing your payment...", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setMaximumSize(new Dimension(280, 25));
        progressBar.setForeground(PRIMARY_COLOR);
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(20));
        panel.add(progressBar);
        
        dialog.add(panel);
        
        new Thread(() -> dialog.setVisible(true)).start();
        
        return dialog;
    }
    
    private void showSuccessDialog(Transaction transaction) {
        String message = String.format(
            "<html><div style='text-align: center; padding: 15px;'>" +
            "<h2 style='color: #34A853;'>Payment Successful!</h2>" +
            "<p style='font-size: 12px; color: #666; margin-top: 10px;'>Transaction ID: %s</p>" +
            "<table style='margin: 15px auto; text-align: left;'>" +
            "<tr><td style='padding: 5px;'><b>Amount:</b></td><td style='padding: 5px;'>₹%.2f</td></tr>" +
            "<tr><td style='padding: 5px;'><b>Fee:</b></td><td style='padding: 5px;'>₹%.2f</td></tr>" +
            "<tr><td style='padding: 5px;'><b>Total:</b></td><td style='padding: 5px;'><b>₹%.2f</b></td></tr>" +
            "<tr><td style='padding: 5px;'><b>Method:</b></td><td style='padding: 5px;'>%s</td></tr>" +
            "</table></div></html>",
            transaction.getTransactionId(),
            transaction.getAmount(),
            transaction.getFee(),
            transaction.getTotalAmount(),
            transaction.getPaymentMethod()
        );
        
        JOptionPane.showMessageDialog(this, message, "Payment Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showTransactionHistory() {
        List<Transaction> history = processor.getTransactionHistory();
        
        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transactions yet!", "Transaction History", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create table
        String[] columns = {"Transaction ID", "Method", "Amount", "Fee", "Status", "Date"};
        Object[][] data = new Object[history.size()][6];
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (int i = 0; i < history.size(); i++) {
            Transaction txn = history.get(i);
            data[i][0] = txn.getTransactionId();
            data[i][1] = txn.getPaymentMethod();
            data[i][2] = String.format("₹%.2f", txn.getAmount());
            data[i][3] = String.format("₹%.2f", txn.getFee());
            data[i][4] = txn.getStatus();
            data[i][5] = sdf.format(txn.getTimestamp());
        }
        
        JTable table = new JTable(data, columns);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 400));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Statistics
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        statsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        statsPanel.add(new JLabel(String.format("Total Transactions: %d", history.size())));
        statsPanel.add(new JLabel(String.format("Successful: %d", processor.getSuccessfulTransactionCount())));
        panel.add(statsPanel, BorderLayout.SOUTH);
        
        JOptionPane.showMessageDialog(this, panel, "Transaction History", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void clearForm() {
        amountField.setText("");
        for (JTextField field : inputFields.values()) {
            field.setText("");
        }
        paymentMethodCombo.setSelectedIndex(0);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // ============================================================================
    // MAIN METHOD
    // ============================================================================
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PaymentGateway());
    }
}
