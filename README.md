# Online Grocery Store - Java Web Application

A complete Java web application built with JSP, Servlets, JDBC, and Apache Derby database. This application follows the MVC architecture pattern and includes features for customer registration, product management, shopping cart, and order processing.

## Technology Stack

- **Java 24** (JDK 24 compatible)
- **JSP** for views
- **Servlets** for controllers  
- **JDBC** with embedded Derby Client
- **Apache Tomcat 9** deployment
- **Maven** build system
- **Bootstrap 5** for UI styling
- **BCrypt** for password hashing

## Architecture & Design Patterns

- **MVC Pattern**: Proper separation of Model, View, and Controller layers
- **DAO Pattern**: Data Access Objects with interfaces and implementations
- **Factory Pattern**: DAOFactory for creating DAO instances
- **Singleton Pattern**: DerbyConnectionManager for database connections
- **Strategy Pattern**: CSV parsing strategies for bulk uploads

## Database Schema

The application uses Apache Derby with the following tables:

- **customer**: Customer information (id, name, email, contact, address)
- **login**: Authentication data (login_id, email, password_hash, user_type, status)
- **product**: Product catalog (product_id, name, description, company, price, stock)
- **orders**: Order information (order_id, customer_id, order_date, status, total_amount)
- **order_item**: Order line items (order_item_id, order_id, product_id, quantity, price_at_order)

## Features

### Customer Features
- Customer registration and authentication
- Browse product catalog with pagination
- Search products by name
- Add/remove items from shopping cart
- Place orders and view order history
- Update customer profile
- Account deactivation/restoration

### Admin Features
- Admin registration (backend service)
- Add single products via form
- Bulk product upload via CSV
- Product management (CRUD operations)
- View all orders and customers

### Technical Features
- Connection pooling with Derby
- Input validation and sanitization
- Exception handling hierarchy
- Session management
- Character encoding (UTF-8)
- Authentication filters
- Responsive design
- CSV file processing with error handling

## Project Structure

```
online-grocery/
├── pom.xml
├── src/main/java/
│   ├── com.store.config/          # Configuration and filters
│   ├── com.store.dao/             # DAO interfaces & implementations
│   ├── com.store.dto/             # DTOs & enums
│   ├── com.store.model/           # Entity POJOs
│   ├── com.store.service/         # Business logic services
│   ├── com.store.servlet/         # Servlet controllers
│   └── com.store.util/            # Utilities and exceptions
├── src/main/resources/
│   └── derby.properties           # Database configuration
├── src/main/webapp/
│   ├── WEB-INF/
│   │   ├── web.xml               # Web application configuration
│   │   └── views/                # JSP view templates
│   ├── static/                   # CSS, JS, images
│   └── index.jsp                 # Home page
└── src/test/java/                # JUnit tests
```

## Setup Instructions

### Prerequisites
- JDK 24 installed
- Apache Tomcat 9
- Eclipse IDE (optional but recommended)
- Maven 3.6+

### Eclipse Setup

1. **Import Project**:
   - File → Import → Existing Maven Projects
   - Browse to the project folder and select it
   - Click Finish

2. **Configure Build Path**:
   - Right-click project → Properties → Java Build Path
   - Ensure JDK 24 is selected in Libraries tab
   - Add Tomcat 9 server runtime if not present

3. **Configure Tomcat Server**:
   - Window → Preferences → Server → Runtime Environments
   - Add Apache Tomcat v9.0
   - Set Tomcat installation directory
   - Select JRE (Java 24)

4. **Deploy to Tomcat**:
   - Right-click project → Run As → Run on Server
   - Select Tomcat 9 server
   - Click Finish

### Manual Setup

1. **Clone/Download** the project
2. **Build with Maven**:
   ```bash
   mvn clean compile
   mvn package
   ```
3. **Deploy WAR file**:
   - Copy `target/online-grocery.war` to Tomcat's `webapps` directory
   - Start Tomcat server
4. **Access Application**:
   - Open browser and go to `http://localhost:8080/online-grocery`

## Database Configuration

The application uses embedded Derby database that creates automatically on first run. Configuration is in `src/main/resources/derby.properties`:

```properties
derby.driver=org.apache.derby.jdbc.EmbeddedDriver
derby.url=jdbc:derby:online_grocery_db;create=true
connection.pool.max.size=20
```

## Usage Examples

### Customer Registration
```
POST /register
Parameters: name, email, password, contact, address
```

### Product Bulk Upload (CSV format)
```
name,description,company,price,stock
Organic Apples,Fresh organic apples,FreshFarms,3.99,100
Whole Milk,Full fat milk 1L,DairyBest,2.49,50
```

### API Endpoints
- `GET /` - Home page
- `GET/POST /register` - Customer registration
- `GET/POST /login` - User authentication
- `GET /products` - Product catalog
- `GET/POST /cart/add` - Add to cart
- `GET /cart/view` - View cart
- `POST /order/place` - Place order
- `GET /order/history` - Order history

## Testing

Run unit tests with Maven:
```bash
mvn test
```

## Security Features

- **Password Hashing**: BCrypt with 12 rounds
- **Input Validation**: Email format, required fields
- **SQL Injection Protection**: Prepared statements
- **XSS Prevention**: Input sanitization
- **Session Management**: HTTP-only cookies
- **Authentication Filters**: Protect secured endpoints

## Error Handling

Custom exception hierarchy:
- `ValidationException` - Input validation errors
- `EmailFormatException` - Email format validation
- `DuplicateEmailException` - Duplicate email registration
- `DatabaseException` - Database operation errors
- `FileProcessingException` - CSV processing errors

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes following the existing patterns
4. Add unit tests for new functionality
5. Submit a pull request

## License

This project is created for educational purposes and follows standard Java web development practices.