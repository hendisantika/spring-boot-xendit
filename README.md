# Spring Boot Xendit Payment Integration

A complete e-commerce application built with Spring Boot 3.5.6, integrating Xendit payment gateway for seamless online
payments in Indonesia.

## 📋 Features

- ✅ Product Management (CRUD operations)
- ✅ Product Image Support
- ✅ Shopping Cart & Checkout
- ✅ Order Management System
- ✅ Xendit Payment Integration
- ✅ Real-time Payment Status Tracking
- ✅ Webhook Support for Payment Notifications
- ✅ Responsive UI with Bootstrap 5
- ✅ PostgreSQL Database Integration
- ✅ Docker Compose Support

## 🛠️ Technologies Used

- **Framework:** Spring Boot 3.5.6
- **Java Version:** JDK 25
- **Database:** PostgreSQL 17.5
- **Template Engine:** Thymeleaf
- **Payment Gateway:** Xendit Java SDK 1.23.0
- **UI Framework:** Bootstrap 5.3.0 + Bootstrap Icons
- **Build Tool:** Maven
- **Containerization:** Docker & Docker Compose

### Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Thymeleaf
- Spring Boot Starter Validation
- Spring Boot Starter WebFlux (for WebClient)
- PostgreSQL Driver
- Xendit Java Library
- Lombok
- Spring Boot DevTools

## 📁 Project Structure

```
spring-boot-xendit/
├── src/main/java/id/my/hendisantika/xendit/
│   ├── config/              # Configuration classes
│   │   ├── DataInitializer.java
│   │   ├── WebClientConfig.java
│   │   └── XenditConfig.java
│   ├── controller/          # Web & REST controllers
│   │   ├── OrderController.java
│   │   ├── PaymentController.java
│   │   ├── PaymentWebController.java
│   │   └── ProductWebController.java
│   ├── dto/                 # Data Transfer Objects
│   │   ├── PaymentRequestDTO.java
│   │   └── ProductDTO.java
│   ├── entity/              # JPA Entities
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   └── Product.java
│   ├── repository/          # Spring Data JPA Repositories
│   │   ├── OrderRepository.java
│   │   ├── OrderItemRepository.java
│   │   └── ProductRepository.java
│   └── service/             # Business Logic
│       ├── OrderService.java
│       ├── ProductService.java
│       └── XenditService.java
├── src/main/resources/
│   ├── templates/           # Thymeleaf Templates
│   │   ├── orders/
│   │   │   ├── list.html
│   │   │   └── detail.html
│   │   ├── payments/
│   │   │   └── checkout.html
│   │   └── products/
│   │       ├── list.html
│   │       ├── detail.html
│   │       └── form.html
│   └── application.properties
├── compose.yaml             # Docker Compose configuration
└── pom.xml                  # Maven dependencies

```

## 🚀 Getting Started

### Prerequisites

- JDK 25 or later
- Maven 3.9.x
- Docker & Docker Compose
- Xendit Account (for API Key)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/hendisantika/spring-boot-xendit.git
   cd spring-boot-xendit
   ```

2. **Configure Xendit API Key**

   Edit `src/main/resources/application.properties`:
   ```properties
   # Xendit Configuration
   xendit.api.key=YOUR_XENDIT_API_KEY
   xendit.webhook.token=YOUR_WEBHOOK_TOKEN
   ```

   Or set as environment variables:
   ```bash
   export XENDIT_API_KEY=your_xendit_api_key_here
   export XENDIT_WEBHOOK_TOKEN=your_webhook_token_here
   ```

3. **Start PostgreSQL Database**
   ```bash
   docker compose up -d
   ```

4. **Run the Application**
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Access the Application**
   ```
   http://localhost:8080
   ```

## 📝 Configuration

### Database Configuration

The application is configured to use PostgreSQL running in Docker:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/xendit_db
spring.datasource.username=yu71
spring.datasource.password=53cret
spring.jpa.hibernate.ddl-auto=update
```

### Xendit Configuration

```properties
xendit.api.key=${XENDIT_API_KEY:your-xendit-api-key-here}
xendit.webhook.token=${XENDIT_WEBHOOK_TOKEN:your-webhook-token-here}
```

**Note:** For production, always use environment variables instead of hardcoding API keys.

## 🎯 Usage

### Product Management

1. **View Products**
    - Navigate to `http://localhost:8080/products`
    - Browse through 10 pre-loaded dummy products

2. **Add New Product**
    - Click "Add Product" button
    - Fill in product details (name, description, price, stock, image URL)
    - Submit the form

3. **Edit Product**
    - Click "Edit" button on any product
    - Update product information
    - Save changes

### Shopping & Payment Flow

1. **Browse Products**
    - View product catalog at `/products`
    - Click "View" to see product details

2. **Checkout**
    - Click "Buy Now" on any product
    - Fill in customer information (name, email)
    - Adjust quantity
    - Click "Proceed to Payment"

3. **Payment**
    - System creates an order and Xendit invoice
    - Redirects to order detail page
    - Click "Pay Now with Xendit" button
    - Complete payment on Xendit payment page

4. **Order Tracking**
    - View all orders at `/orders`
    - Check payment status
    - View order details and items

## 🔌 API Endpoints

### REST API Endpoints

#### Payment API

```
POST   /api/payments/webhook          - Xendit webhook callback
GET    /api/payments/invoice/{id}     - Get invoice status
```

### Web Endpoints

#### Products

```
GET    /products                      - List all products
GET    /products/{id}                 - View product details
GET    /products/new                  - Show add product form
POST   /products                      - Create new product
GET    /products/{id}/edit            - Show edit product form
POST   /products/{id}                 - Update product
GET    /products/{id}/delete          - Delete product
```

#### Orders

```
GET    /orders                        - List all orders
GET    /orders/{id}                   - View order details
GET    /orders/check-status/{id}      - Refresh payment status
```

#### Payments

```
GET    /payments/checkout/{productId} - Show checkout form
POST   /payments/process              - Process payment & create order
```

## 💾 Database Schema

### Products Table

```sql
CREATE TABLE products
(
    id          bigserial PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description VARCHAR(1000),
    price       DECIMAL(19, 2) NOT NULL,
    stock       INTEGER        NOT NULL,
    image_url   VARCHAR(255),
    created_at  TIMESTAMP      NOT NULL,
    updated_at  TIMESTAMP
);
```

### Orders Table

```sql
CREATE TABLE orders
(
    id                 bigserial PRIMARY KEY,
    order_number       VARCHAR(255) UNIQUE NOT NULL,
    customer_name      VARCHAR(255)        NOT NULL,
    customer_email     VARCHAR(255)        NOT NULL,
    customer_phone     VARCHAR(50),
    total_amount       DECIMAL(19, 2)      NOT NULL,
    payment_status     VARCHAR(20)         NOT NULL,
    xendit_invoice_id  VARCHAR(255),
    xendit_invoice_url VARCHAR(500),
    payment_method     VARCHAR(50),
    paid_at            TIMESTAMP,
    expired_at         TIMESTAMP,
    created_at         TIMESTAMP           NOT NULL,
    updated_at         TIMESTAMP
);
```

### Order Items Table

```sql
CREATE TABLE order_items
(
    id            bigserial PRIMARY KEY,
    order_id      bigint         NOT NULL REFERENCES orders (id),
    product_id    bigint         NOT NULL REFERENCES products (id),
    product_name  VARCHAR(255)   NOT NULL,
    product_price DECIMAL(19, 2) NOT NULL,
    quantity      INTEGER        NOT NULL,
    subtotal      DECIMAL(19, 2) NOT NULL
);
```

## 🔐 Payment Status Flow

```
PENDING → User needs to complete payment
   ↓
PAID → Payment successful
   ↓
Order confirmed

PENDING → Payment expired
   ↓
EXPIRED → Need to create new order
```

## 🎨 Sample Data

The application automatically initializes with 10 sample products:

1. Wireless Bluetooth Headphones - Rp 299,000
2. Smart Watch Series 5 - Rp 1,299,000
3. Mechanical Gaming Keyboard - Rp 899,000
4. 4K Ultra HD Webcam - Rp 450,000
5. Portable SSD 1TB - Rp 1,500,000
6. Wireless Gaming Mouse - Rp 550,000
7. USB-C Hub Multi-Port Adapter - Rp 350,000
8. Laptop Stand Aluminum - Rp 250,000
9. LED Monitor 27 inch - Rp 3,500,000
10. Wireless Power Bank 20000mAh - Rp 450,000

## 🧪 Testing

### Manual Testing

1. **Test Product Creation**
   ```bash
   # Navigate to http://localhost:8080/products/new
   # Fill in product details and submit
   ```

2. **Test Payment Flow**
   ```bash
   # Select a product and click "Buy Now"
   # Complete checkout form
   # Verify Xendit invoice creation
   ```

3. **Test Webhook (using Xendit Dashboard)**
    - Use Xendit test mode
    - Configure webhook URL: `http://your-domain/api/payments/webhook`
    - Make test payment
    - Verify order status updates

## 📚 Documentation

### Xendit Resources

- [Xendit API Documentation](https://developers.xendit.co/api-reference/)
- [Xendit Java SDK](https://github.com/xendit/xendit-java)
- [Xendit Webhooks Guide](https://developers.xendit.co/api-reference/#webhooks)

### Project Documentation

- **Author:** hendisantika
- **Email:** hendisantika@yahoo.co.id
- **Telegram:** @hendisantika34
- **Website:** [s.id/hendisantika](https://s.id/hendisantika)

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Failed**
   ```bash
   # Check if PostgreSQL container is running
   docker ps

   # Restart Docker Compose
   docker compose down
   docker compose up -d
   ```

2. **Xendit API Error**
   ```bash
   # Verify API key is correct
   # Check if using test mode or production mode
   # Ensure API key has proper permissions
   ```

3. **Port Already in Use**
   ```bash
   # Change port in application.properties
   server.port=8081

   # Or change PostgreSQL port in compose.yaml
   ports:
     - "5434:5432"
   ```

## 📄 License

This project is created for educational purposes.

## 👨‍💻 Author

**Hendi Santika**

- Link: [s.id/hendisantika](https://s.id/hendisantika)
- Email: hendisantika@yahoo.co.id
- Telegram: @hendisantika34

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

## ⭐ Show your support

Give a ⭐️ if this project helped you!

---

**Created by IntelliJ IDEA**
Project: spring-boot-xendit
Date: 11/10/25
Time: 10.50
