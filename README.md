# 💳 Payment Gateway Simulator

A comprehensive **Payment Gateway Simulation System** built with Java Swing, demonstrating advanced Object-Oriented Programming concepts, Design Patterns, and modern UI/UX principles. This project provides a complete payment processing interface supporting multiple payment methods with real-time transaction management.

> **Built for:** Object-Oriented Technology (OOT) Mini Project  
> **Tech Stack:** Java 8+, Swing GUI Framework  
> **Status:** ✅ Complete & Fully Functional

[![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.java.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](CONTRIBUTING.md)

---

## 🌟 Features

### 💰 Multiple Payment Methods
- **💳 Credit Card** - Full card validation with Luhn algorithm, expiry check, CVV verification
- **🏦 Debit Card** - Secure card processing with bank integration simulation
- **📱 UPI** - UPI ID validation (user@bank format) with instant processing
- **🌐 Net Banking** - Bank account verification and secure transaction processing
- **👛 Digital Wallet** - Support for Paytm, PhonePe, Google Pay, Amazon Pay

### ⚡ Core Functionality
- ✅ Real-time transaction processing with instant feedback
- ✅ Dynamic fee calculation based on payment method
- ✅ Comprehensive transaction history with filtering
- ✅ Advanced input validation & exception handling
- ✅ Modern, intuitive GUI with professional design
- ✅ Transaction persistence using CSV file I/O
- ✅ Clear transaction details with amount, fees, and timestamps

---

## 🏗️ Architecture & Design Patterns

### Design Patterns Implemented

#### 1. **Strategy Pattern** (Behavioral)
```java
interface FeeStrategy {
    double calculateFee(double amount);
}
// Different fee strategies for different payment methods
- CardFeeStrategy: 2% fee
- UPIFeeStrategy: 0% fee
- NetBankingFeeStrategy: 1% fee
- WalletFeeStrategy: 0% fee
```

#### 2. **Factory Pattern** (Creational)
```java
class PaymentMethodFactory {
    static PaymentMethod createPaymentMethod(String type, Map<String, String> details);
}
// Dynamically creates payment objects based on user selection
```

#### 3. **Singleton Pattern** (Creational)
```java
class PaymentProcessor {
    private static PaymentProcessor instance;
    // Ensures single instance for transaction processing
}
```

### 🎯 OOP Concepts Demonstrated

#### **Inheritance** (IS-A Relationship)
```
Abstract PaymentMethod
    ├── CreditCard extends PaymentMethod
    ├── DebitCard extends PaymentMethod
    ├── UPI extends PaymentMethod
    ├── NetBanking extends PaymentMethod
    └── Wallet extends PaymentMethod
```

#### **Polymorphism** (Runtime Binding)
- Method overriding in payment validation
- Dynamic method dispatch for `validate()` and `process()`
- Interface implementation for fee calculation

#### **Encapsulation** (Data Hiding)
- Private fields with public getters/setters
- Protected helper methods
- Information hiding in payment processing

#### **Abstraction** (Abstract Classes & Interfaces)
- Abstract `PaymentMethod` class
- `FeeStrategy` interface
- Hidden implementation details

### 🚨 Exception Handling Hierarchy

```
PaymentException (Parent)
    ├── InvalidCardException
    ├── InvalidUPIException
    ├── BankServerException
    └── InsufficientFundsException
```

---

## 📁 Project Structure

```
payment-gateway-simulator/
│
├── PaymentGateway.java              # Main application (1000+ lines)
│   ├── GUI Components               # Swing UI elements
│   ├── Payment Classes              # CreditCard, DebitCard, UPI, etc.
│   ├── Exception Classes            # Custom exception hierarchy
│   ├── Design Pattern Classes       # Factory, Strategy implementations
│   └── Utility Classes              # Transaction, Processor, etc.
│
├── transaction_history.csv          # Transaction storage (auto-generated)
├── README.md                        # Project documentation (this file)
├── PROJECT_DOCUMENTATION.md         # Detailed technical documentation
├── QUICK_START.md                  # 2-minute setup guide
├── PRESENTATION_GUIDE.md            # 5-minute presentation structure
├── PROJECT_SUMMARY.md               # Complete project overview
└── .gitignore                       # Git ignore rules

```

---

## 🚀 Quick Start

### Prerequisites
- **Java JDK 8 or higher** (Download from [Oracle](https://www.oracle.com/java/technologies/downloads/))
- **Any Java IDE** (Eclipse, IntelliJ IDEA, VS Code) or Command Line

### Installation & Running

#### Method 1: Command Line (Recommended)

```bash
# 1. Clone the repository
git clone https://github.com/YOUR_USERNAME/payment-gateway-simulator.git
cd payment-gateway-simulator

# 2. Compile the code
javac PaymentGateway.java

# 3. Run the application
java PaymentGateway
```

#### Method 2: IDE (Eclipse/IntelliJ)

1. Import project into your IDE
2. Open `PaymentGateway.java`
3. Right-click → Run As → Java Application

### 🎯 First Time Setup

No configuration needed! The application:
- ✅ Auto-creates `transaction_history.csv` on first transaction
- ✅ No external dependencies required
- ✅ Runs on any platform (Windows, macOS, Linux)

---

## 📸 Screenshots

### 🏠 Main Interface
Beautiful, modern payment interface with clean design and intuitive layout.

### 💳 Credit Card Payment
Secure card input with validation, expiry checking, and CVV verification.

### 📱 UPI Payment
Quick UPI payments with instant validation and zero processing fees.

### 📊 Transaction History
Complete transaction log with amount, fees, payment method, and timestamps.

---

## 💡 Usage Guide

### Making a Payment

1. **Enter Amount** 
   - Input the payment amount in ₹ (Indian Rupees)
   - Must be greater than ₹0

2. **Select Payment Method**
   - Choose from: Credit Card, Debit Card, UPI, Net Banking, or Wallet
   - Form changes dynamically based on selection

3. **Fill Payment Details**
   - Complete all required fields for selected method
   - Real-time validation ensures data accuracy

4. **Process Payment**
   - Click "Process Payment" button
   - See instant success/failure message with transaction details

5. **View History**
   - Click "View Transaction History" to see all past transactions
   - Review payment details, fees, and timestamps

### Clear Form
- Click "Clear Form" to reset all fields and start fresh

---

## 🧪 Test Cases & Examples

### ✅ Test Case 1: Credit Card Payment
```
Card Number:     4532015112830366 (Valid test card)
Cardholder Name: John Doe
Expiry Date:     12/25
CVV:             123
Amount:          ₹1000
---
Expected Result: 
✅ Payment Successful
Processing Fee: ₹20 (2%)
Total Amount: ₹1020
```

### ✅ Test Case 2: UPI Payment
```
UPI ID:  test@paytm
Amount:  ₹500
---
Expected Result:
✅ Payment Successful
Processing Fee: ₹0 (0%)
Total Amount: ₹500
```

### ✅ Test Case 3: Net Banking
```
Bank Name:       HDFC Bank
Account Number:  123456789012
Amount:          ₹2000
---
Expected Result:
✅ Payment Successful
Processing Fee: ₹20 (1%)
Total Amount: ₹2020
```

### ✅ Test Case 4: Wallet Payment
```
Wallet Provider: PhonePe
Mobile Number:   9876543210
Amount:          ₹750
---
Expected Result:
✅ Payment Successful
Processing Fee: ₹0 (0%)
Total Amount: ₹750
```

### ❌ Test Case 5: Invalid Card
```
Card Number:  1234567890123456 (Invalid - fails Luhn check)
---
Expected Result:
❌ Invalid Card Number
Exception: InvalidCardException thrown and handled
```

---

## 📊 Transaction Fees Structure

| Payment Method | Processing Fee | Speed        | Best For           |
|----------------|----------------|--------------|-------------------|
| 💳 Credit Card | **2.0%**       | Instant      | Large purchases   |
| 🏦 Debit Card  | **2.0%**       | Instant      | Secure payments   |
| 📱 UPI         | **0.0%** ⭐    | Instant      | Quick payments    |
| 🌐 Net Banking | **1.0%**       | 1-2 minutes  | Bank transfers    |
| 👛 Wallet      | **0.0%** ⭐    | Instant      | Small amounts     |

---

## 🎓 Academic Highlights

### ✅ OOP Concepts Covered

| Concept | Implementation | Example |
|---------|----------------|---------|
| **Classes & Objects** | 15+ classes | `CreditCard`, `Transaction`, `PaymentProcessor` |
| **Inheritance** | Multi-level hierarchy | `PaymentMethod` → `CreditCard` |
| **Polymorphism** | Method overriding | `validate()`, `process()` methods |
| **Abstraction** | Abstract classes & interfaces | `abstract class PaymentMethod` |
| **Encapsulation** | Private fields, public methods | Getters/Setters pattern |
| **Interfaces** | Contract-based design | `FeeStrategy` interface |

### ✅ Advanced Java Topics

- ☑️ **Exception Handling** - try-catch-finally blocks, custom exceptions
- ☑️ **File I/O** - CSV read/write operations with BufferedReader/FileWriter
- ☑️ **Collections Framework** - HashMap, ArrayList usage
- ☑️ **GUI Programming** - Swing components (JFrame, JPanel, JButton, JTextField)
- ☑️ **Event Handling** - ActionListener, ItemListener implementations
- ☑️ **Date/Time APIs** - LocalDateTime, DateTimeFormatter
- ☑️ **String Manipulation** - Validation, formatting, parsing
- ☑️ **Regular Expressions** - Pattern matching for validation

### ✅ Software Engineering Principles

- **DRY** (Don't Repeat Yourself) - Reusable methods and components
- **SOLID** Principles - Single Responsibility, Open/Closed, etc.
- **Separation of Concerns** - GUI, Business Logic, Data layers
- **Clean Code** - Meaningful names, proper comments, formatting

---

## 🛡️ Security Features

### Input Validation
- ✅ Card number validation using **Luhn Algorithm**
- ✅ Expiry date format and validity checking
- ✅ CVV format verification (3-4 digits)
- ✅ UPI ID format validation (user@bank)
- ✅ Account number length verification
- ✅ Mobile number format checking (10 digits)

### Exception Handling
- ✅ Graceful error handling for all user inputs
- ✅ Meaningful error messages for users
- ✅ Transaction rollback on failures
- ✅ Logging of failed transactions

### Data Security
- ✅ Local storage only (no external API calls)
- ✅ Transaction data stored in CSV format
- ✅ Input sanitization to prevent injection

---

## 📈 Future Enhancements

### Phase 1 - Database Integration
- [ ] Replace CSV with MySQL/PostgreSQL database
- [ ] User authentication & authorization
- [ ] Admin panel for transaction management

### Phase 2 - API Development
- [ ] RESTful API using Spring Boot
- [ ] JWT-based authentication
- [ ] API documentation with Swagger

### Phase 3 - Advanced Features
- [ ] Email notifications for transactions
- [ ] SMS alerts for payment confirmation
- [ ] Multi-currency support (USD, EUR, GBP)
- [ ] Payment refund functionality
- [ ] Recurring payments/subscriptions

### Phase 4 - Security Enhancements
- [ ] End-to-end encryption for sensitive data
- [ ] Two-factor authentication (2FA)
- [ ] PCI DSS compliance
- [ ] Fraud detection algorithms

### Phase 5 - UI/UX Improvements
- [ ] Dark mode theme
- [ ] Responsive design for different screen sizes
- [ ] Payment analytics dashboard
- [ ] Export transactions to PDF/Excel

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to check the [issues page](https://github.com/YOUR_USERNAME/payment-gateway-simulator/issues).

### How to Contribute

1. **Fork** the repository
2. **Create** your feature branch
   ```bash
   git checkout -b feature/AmazingFeature
   ```
3. **Commit** your changes
   ```bash
   git commit -m 'Add some AmazingFeature'
   ```
4. **Push** to the branch
   ```bash
   git push origin feature/AmazingFeature
   ```
5. **Open** a Pull Request

### Code Style Guidelines
- Follow Java naming conventions (camelCase for methods, PascalCase for classes)
- Add JavaDoc comments for public methods
- Maintain consistent indentation (4 spaces)
- Write meaningful commit messages

---

## 📝 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

```
MIT License - You are free to:
✅ Use commercially
✅ Modify
✅ Distribute
✅ Use privately
```

---

## 👨‍💻 Author

**Your Name**

- 🌐 Portfolio: [yourportfolio.com](https://yourportfolio.com)
- 💼 LinkedIn: [linkedin.com/in/yourprofile](https://linkedin.com/in/yourprofile)
- 🐙 GitHub: [@yourusername](https://github.com/yourusername)
- 📧 Email: your.email@example.com

---

## 🙏 Acknowledgments

### Inspiration
- Real-world payment gateways: **Razorpay**, **Stripe**, **PayPal**, **Square**
- Modern UI/UX design principles from leading fintech apps

### References
- [Java Swing Documentation](https://docs.oracle.com/javase/tutorial/uiswing/)
- [Design Patterns: Elements of Reusable Object-Oriented Software](https://en.wikipedia.org/wiki/Design_Patterns)
- [Clean Code by Robert C. Martin](https://www.oreilly.com/library/view/clean-code-a/9780136083238/)

### Special Thanks
- **[Your College Name]** - For providing the opportunity to work on this project
- **[Professor Name]** - For guidance and mentorship
- **Open Source Community** - For inspiration and best practices

---

## 📞 Support

Need help? Have questions? Found a bug?

- 📧 **Email**: your.email@example.com
- 💬 **Issues**: [Create an issue](https://github.com/YOUR_USERNAME/payment-gateway-simulator/issues)
- 📖 **Documentation**: Check [PROJECT_DOCUMENTATION.md](PROJECT_DOCUMENTATION.md)
- 🚀 **Quick Start**: See [QUICK_START.md](QUICK_START.md)

---

## ⭐ Show Your Support

If this project helped you learn OOP concepts or inspired your own project, please consider:

- ⭐ **Starring** this repository
- 🔀 **Forking** for your own experiments
- 📢 **Sharing** with fellow students and developers
- 💬 **Providing feedback** through issues

---

## 📊 Project Stats

- **Lines of Code**: 1000+
- **Classes**: 15+
- **Design Patterns**: 3
- **Payment Methods**: 5
- **Exception Types**: 4
- **Development Time**: Educational Project
- **Java Version**: 8+

---

## 🎯 Learning Outcomes

After studying this project, you will understand:

✅ How to implement **Design Patterns** in real-world applications  
✅ **OOP principles** and their practical applications  
✅ Building **GUI applications** with Java Swing  
✅ **Exception handling** and error management  
✅ **File I/O** operations for data persistence  
✅ **Software architecture** and clean code practices  
✅ **Payment gateway** workflow and transaction processing  

---

<div align="center">

### 💻 Made with ❤️ and ☕ using Java & Swing

**⭐ Star this repository if you found it helpful! ⭐**

[🔝 Back to Top](#-payment-gateway-simulator)

</div>
