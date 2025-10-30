# 📘 Payment Gateway Simulation - Complete Project Documentation

## 🎯 Project Report for OOT Mini Project

---

## 1. INTRODUCTION

### 1.1 Project Title
**Payment Gateway Simulation System**

### 1.2 Objective
To develop a comprehensive payment gateway simulation system that demonstrates:
- Object-Oriented Technology (OOT) concepts
- Design Patterns (Strategy and Factory)
- File I/O operations
- Exception handling
- Interactive GUI development

### 1.3 Scope
This project simulates a real-world payment gateway system supporting multiple payment methods including Credit Card, Debit Card, UPI, Net Banking, and Digital Wallets. The system processes transactions, calculates fees, maintains transaction history, and provides an interactive user interface.

### 1.4 Technology Stack
- **Programming Language**: Java (JDK 8+)
- **GUI Framework**: Java Swing
- **Data Storage**: CSV File I/O
- **Design Patterns**: Strategy, Factory
- **Architecture**: Object-Oriented Design

---

## 2. SYSTEM REQUIREMENTS

### 2.1 Hardware Requirements
- **Processor**: Pentium IV or higher
- **RAM**: 512 MB minimum (1 GB recommended)
- **Hard Disk**: 50 MB free space
- **Display**: 1024x768 resolution or higher

### 2.2 Software Requirements
- **Operating System**: Windows 7/8/10/11, Linux, or macOS
- **JDK**: Java Development Kit 8 or higher
- **IDE** (Optional): Eclipse, NetBeans, IntelliJ IDEA, or VS Code

---

## 3. DESIGN PATTERNS IMPLEMENTATION

### 3.1 Strategy Pattern

**Purpose**: To define a family of fee calculation algorithms and make them interchangeable.

**Problem Solved**: Different payment methods have different fee structures. Instead of using if-else conditions, we use Strategy Pattern for flexibility.

**Implementation**:
```java
interface FeeStrategy {
    double calculateFee(double amount);
}

class CardFeeStrategy implements FeeStrategy {
    public double calculateFee(double amount) {
        return amount * 0.02; // 2% fee
    }
}

class UPIFeeStrategy implements FeeStrategy {
    public double calculateFee(double amount) {
        return 0.0; // No fee
    }
}
```

**Benefits**:
- Easy to add new fee strategies
- Decouples fee calculation from payment processing
- Follows Open/Closed Principle (open for extension, closed for modification)

**UML Diagram**:
```
           FeeStrategy (Interface)
                  |
      +-----------+-----------+-----------+
      |           |           |           |
CardFeeStrategy UPIFeeStrategy NetBankingFeeStrategy WalletFeeStrategy
```

### 3.2 Factory Pattern

**Purpose**: To create objects without specifying their exact class.

**Problem Solved**: Creating different payment method objects based on user selection without tight coupling.

**Implementation**:
```java
class PaymentMethodFactory {
    public static PaymentMethod createPaymentMethod(String type, Map<String, String> details) {
        switch (type) {
            case "CREDIT CARD":
                return new CreditCard(details);
            case "UPI":
                return new UPI(details);
            // ... other cases
        }
    }
}
```

**Benefits**:
- Centralized object creation
- Reduces code duplication
- Easy to add new payment methods
- Loose coupling between client and concrete classes

**UML Diagram**:
```
PaymentMethodFactory
         |
         | creates
         v
    PaymentMethod (Abstract)
         |
   +-----+-----+-----+-----+
   |     |     |     |     |
Credit Debit UPI  Net  Wallet
Card   Card      Banking
```

---

## 4. OOP CONCEPTS IMPLEMENTATION

### 4.1 Classes and Objects

**Classes Implemented**:
1. `PaymentMethod` (Abstract)
2. `CreditCard`, `DebitCard`, `UPI`, `NetBanking`, `Wallet` (Concrete)
3. `Transaction`
4. `PaymentProcessor`
5. `PaymentMethodFactory`
6. `FeeStrategy` implementations
7. Exception classes

**Example**:
```java
// Creating objects
PaymentMethod payment = new CreditCard("1234567812345678", "John Doe", "12/25", "123");
Transaction txn = new Transaction("TXN123", "Credit Card", 1000.0, 20.0, "SUCCESS", "Details");
```

### 4.2 Inheritance

**Hierarchy**:
```
PaymentMethod (Abstract Base Class)
    |
    +-- CreditCard
    +-- DebitCard
    +-- UPI
    +-- NetBanking
    +-- Wallet
```

**Inherited Members**:
- `methodName` - Name of payment method
- `feeStrategy` - Fee calculation strategy
- `processPayment()` - Payment processing logic
- `calculateFee()` - Fee calculation using strategy

**New Members in Subclasses**:
- Each payment method has specific fields (e.g., `cardNumber` for cards, `upiId` for UPI)
- Implementation of abstract methods `validatePayment()` and `getPaymentDetails()`

### 4.3 Polymorphism

**Method Overriding**:
```java
abstract class PaymentMethod {
    public abstract boolean validatePayment() throws PaymentException;
    public abstract String getPaymentDetails();
}

class CreditCard extends PaymentMethod {
    @Override
    public boolean validatePayment() throws PaymentException {
        // Credit card specific validation
    }
    
    @Override
    public String getPaymentDetails() {
        return "Credit Card - **** **** **** " + cardNumber.substring(12);
    }
}
```

**Dynamic Binding**:
```java
PaymentMethod payment = PaymentMethodFactory.createPaymentMethod(type, details);
payment.validatePayment(); // Calls specific validation based on actual object type
```

### 4.4 Encapsulation

**Data Hiding**:
```java
class Transaction {
    private String transactionId;      // Hidden from outside
    private double amount;              // Hidden
    
    public String getTransactionId() { // Public getter
        return transactionId;
    }
    // No setter - immutable after creation
}
```

**Benefits**:
- Protects data integrity
- Allows controlled access
- Easy to modify internal implementation

### 4.5 Abstraction

**Abstract Class**:
```java
abstract class PaymentMethod {
    // Abstract methods - subclasses must implement
    public abstract boolean validatePayment() throws PaymentException;
    public abstract String getPaymentDetails();
    
    // Concrete method - shared implementation
    public boolean processPayment(double amount) throws PaymentException {
        // Common payment processing logic
    }
}
```

**Interface**:
```java
interface FeeStrategy {
    double calculateFee(double amount);
    String getFeeName();
}
```

---

## 5. EXCEPTION HANDLING

### 5.1 Custom Exception Hierarchy

```
Exception
    |
PaymentException
    |
    +-- InsufficientFundsException
    +-- InvalidCardException
    +-- InvalidUPIException
    +-- BankServerException
```

### 5.2 Exception Classes

```java
class PaymentException extends Exception {
    public PaymentException(String message) {
        super(message);
    }
}

class InvalidCardException extends PaymentException {
    public InvalidCardException() {
        super("Invalid card details");
    }
}
```

### 5.3 Try-Catch Blocks

```java
try {
    PaymentMethod payment = PaymentMethodFactory.createPaymentMethod(type, details);
    Transaction txn = processor.processPayment(payment, amount);
    showSuccessDialog(txn);
} catch (InvalidCardException e) {
    showError("Invalid card details: " + e.getMessage());
} catch (PaymentException e) {
    showError("Payment failed: " + e.getMessage());
}
```

### 5.4 Exception Propagation

1. Validation errors thrown from payment method classes
2. Caught in `PaymentProcessor`
3. Re-thrown to GUI layer
4. Displayed to user with appropriate message

---

## 6. FILE I/O IMPLEMENTATION

### 6.1 Transaction Persistence

**File Format**: CSV (Comma-Separated Values)

**File Structure**:
```csv
TransactionID,PaymentMethod,Amount,Fee,TotalAmount,Status,Timestamp,Details
TXN1730361234567,Credit Card,1000.00,20.00,1020.00,SUCCESS,2025-10-31 14:30:45,Credit Card - **** **** **** 1234
```

### 6.2 Writing to File

```java
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
```

### 6.3 Reading from File

```java
private void loadTransactionHistory() {
    try (BufferedReader br = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
        String line;
        br.readLine(); // Skip header
        
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            // Parse and create Transaction objects
        }
    } catch (IOException e) {
        System.err.println("Error loading: " + e.getMessage());
    }
}
```

### 6.4 Benefits of CSV Storage
- ✅ Human-readable format
- ✅ Easy to import in Excel/Google Sheets
- ✅ Simple to parse
- ✅ No database setup required
- ✅ Platform-independent

---

## 7. GUI DESIGN

### 7.1 Layout Structure

```
JFrame (Main Window)
    |
    +-- Header Panel (BorderLayout.NORTH)
    |   - Title Label
    |   - Subtitle Label
    |
    +-- Payment Panel (BorderLayout.CENTER)
    |   - Amount Field
    |   - Payment Method Combo
    |   - Dynamic Form Panel (CardLayout)
    |       +-- Card Form
    |       +-- UPI Form
    |       +-- Net Banking Form
    |       +-- Wallet Form
    |
    +-- Button Panel (BorderLayout.SOUTH)
        - Pay Now Button
        - History Button
        - Clear Button
```

### 7.2 UI Components

| Component | Type | Purpose |
|-----------|------|---------|
| `amountField` | JTextField | Enter payment amount |
| `paymentMethodCombo` | JComboBox | Select payment method |
| `formPanel` | JPanel | Dynamic form based on selection |
| `payButton` | JButton | Process payment |
| `historyButton` | JButton | View transactions |
| `clearButton` | JButton | Reset form |

### 7.3 CardLayout for Dynamic Forms

```java
cardLayout = new CardLayout();
formPanel = new JPanel(cardLayout);

formPanel.add(createCardForm(), "CARD");
formPanel.add(createUPIForm(), "UPI");
formPanel.add(createNetBankingForm(), "NETBANKING");

// Switch forms based on selection
paymentMethodCombo.addActionListener(e -> {
    String selected = (String) paymentMethodCombo.getSelectedItem();
    if (selected.contains("Card")) {
        cardLayout.show(formPanel, "CARD");
    }
});
```

### 7.4 Event Handling

```java
// Button click event
payButton.addActionListener(e -> processPayment());

// ComboBox selection event
paymentMethodCombo.addActionListener(e -> updateFormPanel());
```

---

## 8. SYSTEM WORKFLOW

### 8.1 Payment Processing Flow

```
User Input
    ↓
Validate Amount
    ↓
Select Payment Method
    ↓
Fill Payment Details
    ↓
Click "Pay Now"
    ↓
Create Payment Method Object (Factory Pattern)
    ↓
Validate Payment Details
    ↓
Calculate Fee (Strategy Pattern)
    ↓
Process Payment
    ↓
Save Transaction to File
    ↓
Display Result
```

### 8.2 Transaction History Flow

```
Click "History" Button
    ↓
Load Transactions from CSV File
    ↓
Create JTable with Data
    ↓
Display in Dialog
    ↓
Show Statistics
```

### 8.3 Error Handling Flow

```
User Input
    ↓
Validation
    ↓
Error? ──Yes─→ Show Error Message
    ↓           Return to Form
    No
    ↓
Process Payment
    ↓
Exception? ──Yes─→ Catch Exception
    ↓              Save Failed Transaction
    No             Show Error Dialog
    ↓
Success → Show Success Dialog
```

---

## 9. CODE STRUCTURE

### 9.1 Package Organization

```
PaymentGateway.java
├── Exception Classes (Lines 20-55)
├── Strategy Pattern (Lines 63-125)
├── Payment Method Classes (Lines 130-280)
├── Factory Pattern (Lines 287-315)
├── Transaction Class (Lines 322-365)
├── Payment Processor (Lines 372-480)
└── GUI Class (Lines 490-1050)
```

### 9.2 Key Methods

| Method | Class | Purpose |
|--------|-------|---------|
| `processPayment()` | PaymentProcessor | Process transaction |
| `validatePayment()` | PaymentMethod | Validate payment details |
| `calculateFee()` | FeeStrategy | Calculate transaction fee |
| `createPaymentMethod()` | Factory | Create payment objects |
| `saveTransaction()` | PaymentProcessor | Save to CSV |
| `showSuccessDialog()` | GUI | Display success message |

---

## 10. TESTING

### 10.1 Test Cases

| Test ID | Description | Input | Expected Output |
|---------|-------------|-------|-----------------|
| TC01 | Valid Credit Card | Card: 1234567812345678, Amount: 1000 | Success, Fee: ₹20 |
| TC02 | Invalid Card Number | Card: 123, Amount: 1000 | Error: Invalid card details |
| TC03 | Empty Amount | Amount: (empty) | Error: Please enter amount |
| TC04 | UPI Payment | UPI: user@paytm, Amount: 500 | Success, Fee: ₹0 |
| TC05 | Invalid UPI ID | UPI: userpaytm, Amount: 500 | Error: Invalid UPI ID |
| TC06 | Net Banking | Bank: HDFC, A/C: 12345678, Amount: 2000 | Success, Fee: ₹20 |
| TC07 | Wallet Payment | Wallet: Paytm, Mobile: 9876543210, Amount: 300 | Success, Fee: ₹0 |
| TC08 | Transaction History | Click History | Show all transactions |
| TC09 | Clear Form | Click Clear | Reset all fields |
| TC10 | Bank Server Down | Random (10% chance) | Error: Bank server down |

### 10.2 Testing Procedure

1. **Unit Testing**
   - Test each payment method class independently
   - Verify fee calculation for each strategy
   - Test exception throwing for invalid inputs

2. **Integration Testing**
   - Test Factory Pattern with all payment types
   - Verify file I/O operations
   - Test GUI event handling

3. **System Testing**
   - End-to-end payment flow
   - Multiple transactions
   - History viewing

4. **User Acceptance Testing**
   - User-friendly error messages
   - Intuitive UI navigation
   - Response time < 2 seconds

---

## 11. RESULTS & SCREENSHOTS

### 11.1 Main Payment Screen
- Clean, modern interface
- Payment method selection dropdown
- Dynamic form fields
- Action buttons

### 11.2 Processing Dialog
- Loading animation
- "Processing payment..." message
- 1.5 second simulation delay

### 11.3 Success Dialog
- Success icon
- Transaction ID
- Amount breakdown (Amount + Fee = Total)
- Payment method details

### 11.4 Transaction History
- Table view with all transactions
- Columns: ID, Method, Amount, Fee, Status, Date
- Statistics: Total and successful transactions

### 11.5 Error Handling
- Clear error messages
- Validation feedback
- User-friendly language

---

## 12. ADVANTAGES

### 12.1 Design Advantages
1. **Modular Architecture** - Easy to maintain and extend
2. **Design Patterns** - Industry-standard implementation
3. **Loose Coupling** - Components are independent
4. **High Cohesion** - Each class has single responsibility

### 12.2 User Advantages
1. **Multiple Payment Options** - 5 different methods
2. **Zero Fee Options** - UPI and Wallet
3. **Transaction History** - View past transactions
4. **Clear Fee Breakdown** - Transparent pricing
5. **Intuitive UI** - Easy to use

### 12.3 Developer Advantages
1. **Easy to Extend** - Add new payment methods easily
2. **Well-Documented** - Comments and README
3. **Exception Handling** - Robust error management
4. **File-Based Storage** - No database setup
5. **Cross-Platform** - Runs on Windows/Linux/Mac

---

## 13. LIMITATIONS

### 13.1 Current Limitations
1. **No Real Payment Processing** - Simulation only
2. **Single User** - No multi-user support
3. **CSV Storage** - Not scalable for large data
4. **No Encryption** - Data not encrypted
5. **No Network Communication** - Local only
6. **Basic Security** - Not production-ready
7. **Random Failures** - 10% simulated failure

### 13.2 Scalability Issues
- CSV file grows with each transaction
- No indexing or query optimization
- Memory constraints for large history
- No concurrent access handling

---

## 14. FUTURE ENHANCEMENTS

### 14.1 Short-Term Enhancements
1. **Database Integration** - MySQL or SQLite
2. **User Authentication** - Login system
3. **Password Encryption** - Secure storage
4. **Export to PDF** - Transaction receipts
5. **Search & Filter** - Transaction history

### 14.2 Long-Term Enhancements
1. **Web Application** - Spring Boot + React
2. **Mobile App** - Android/iOS
3. **Real Payment API** - Razorpay/Stripe integration
4. **Analytics Dashboard** - Charts and graphs
5. **Multi-Currency** - USD, EUR, GBP support
6. **Refund Functionality** - Cancel transactions
7. **Notification System** - Email/SMS alerts
8. **Admin Panel** - Manage transactions
9. **API Development** - RESTful APIs
10. **Cloud Deployment** - AWS/Azure hosting

---

## 15. CONCLUSION

### 15.1 Project Summary
This Payment Gateway Simulation successfully demonstrates all core OOT concepts including:
- ✅ Design Patterns (Strategy and Factory)
- ✅ Object-Oriented Programming (Classes, Inheritance, Polymorphism, Encapsulation, Abstraction)
- ✅ Exception Handling (Custom exceptions and error recovery)
- ✅ File I/O (CSV-based transaction persistence)
- ✅ GUI Development (Interactive Swing interface)

### 15.2 Learning Outcomes
1. Understanding of design patterns and their practical application
2. Hands-on experience with OOP principles
3. Error handling and recovery strategies
4. File-based data persistence
5. GUI event-driven programming
6. Software architecture design

### 15.3 Real-World Relevance
This project provides foundational knowledge for:
- E-commerce platforms
- Banking applications
- Payment processing systems
- Financial technology (FinTech) applications
- Enterprise application development

### 15.4 Skills Gained
- **Technical**: Java, Swing, Design Patterns, OOP
- **Analytical**: Problem-solving, System design
- **Soft Skills**: Documentation, Presentation, Testing

---

## 16. REFERENCES

### 16.1 Books
1. "Design Patterns: Elements of Reusable Object-Oriented Software" - Gang of Four
2. "Head First Design Patterns" - Eric Freeman & Elisabeth Robson
3. "Effective Java" - Joshua Bloch
4. "Java: The Complete Reference" - Herbert Schildt

### 16.2 Online Resources
1. Oracle Java Documentation - https://docs.oracle.com/javase/
2. Java Swing Tutorial - https://docs.oracle.com/javase/tutorial/uiswing/
3. Design Patterns - https://refactoring.guru/design-patterns
4. Razorpay Documentation - https://razorpay.com/docs/
5. Stripe Documentation - https://stripe.com/docs

### 16.3 Inspirations
- Razorpay Payment Gateway
- Paytm Wallet & Payments
- Google Pay UPI System
- Stripe Payment Processing
- PayPal Checkout Flow

---

## 17. APPENDIX

### 17.1 Complete Feature List
- [x] Credit Card Payment
- [x] Debit Card Payment
- [x] UPI Payment
- [x] Net Banking Payment
- [x] Digital Wallet Payment
- [x] Strategy Pattern Implementation
- [x] Factory Pattern Implementation
- [x] Custom Exception Handling
- [x] File I/O (CSV)
- [x] Transaction History
- [x] Fee Calculation
- [x] Input Validation
- [x] Success/Failure Dialogs
- [x] Processing Animation
- [x] Statistics Display
- [x] Modern UI Design
- [x] Cross-Platform Compatibility

### 17.2 File Structure
```
Payment Gateway Solution/
│
├── PaymentGateway.java          (Main source code)
├── PaymentGateway.class         (Compiled bytecode)
├── transaction_history.csv      (Transaction data)
├── README.md                    (User guide)
└── PROJECT_DOCUMENTATION.md     (This document)
```

### 17.3 Command Reference
```bash
# Compile
javac PaymentGateway.java

# Run
java PaymentGateway

# View transactions
type transaction_history.csv    # Windows
cat transaction_history.csv     # Linux/Mac
```

---

**End of Documentation**

---

**Project Details**:
- **Course**: Object-Oriented Technology using Java
- **Project Type**: Mini Project
- **Lines of Code**: ~1000+
- **Classes**: 15+
- **Design Patterns**: 2 (Strategy, Factory)
- **Development Time**: Academic Semester Project

---

**Document Version**: 1.0  
**Last Updated**: October 31, 2025  
**Status**: Complete and Ready for Submission
