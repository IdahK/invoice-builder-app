# Invoice Builder Backend

RESTful API server for the Invoice Builder application. Built with Spring Boot and optimized for performance.

## 🎯 Purpose

The backend provides a robust, scalable API for managing invoices, customers, and business data. It handles all business logic, data persistence, and PDF generation while maintaining high performance through optimized database queries.

## 🏗️ Architecture

### Modular Design
The application follows a **Modular Monolith** architecture with clear separation of concerns:

```
src/main/java/org/invoicebuilder/
├── config/           # Configuration classes
├── customers/        # Customer management
├── invoices/         # Invoice operations
├── payments/         # Payment tracking
├── users/           # User management
├── reports/         # Analytics & summaries
└── api/            # REST controllers
```

### Key Patterns
- **Domain-Driven Design**: Clear domain boundaries
- **Repository Pattern**: Clean data access
- **DTO Pattern**: Secure API contracts
- **Service Layer**: Business logic separation

## 🛠️ Tech Stack

### Core Technologies
- **Spring Boot 4.0.2** - Main framework
- **Java 21** - Latest LTS Java version
- **Spring Data JPA** - Database access
- **Hibernate** - ORM implementation
- **PostgreSQL 15** - Primary database
- **Liquibase** - Database migrations

### API & Documentation
- **Spring Web** - REST API framework
- **OpenAPI 3** - API documentation
- **Swagger UI** - Interactive API docs
- **Spring Validation** - Input validation

### Performance & Monitoring
- **Spring Boot Actuator** - Health checks & metrics
- **Database Projections** - Optimized queries (87% faster)
- **Connection Pooling** - Efficient database connections

### Testing
- **JUnit 5** - Unit testing
- **Spring Boot Test** - Integration testing
- **TestContainers** - Database testing
- **Mockito** - Mocking framework

## 🚀 Getting Started

### Prerequisites
- Java 21+
- Maven 3.8+
- PostgreSQL 15 (or Docker)
- Git

### 1. Database Setup

**Option A: Docker (Recommended)**
```bash
# From project root
docker-compose up -d
```

**Option B: Local PostgreSQL**
```bash
# Create database
createdb invoicebuilder

# Update application.yml with your credentials
```

### 2. Configuration

Copy and configure environment variables:
```bash
cp .env.example .env
# Edit .env with your database credentials
```

### 3. Run Application

**Development Mode:**
```bash
./mvnw spring-boot:run
```

**Production Mode:**
```bash
./mvnw clean package
java -jar target/invoicebuilder-0.0.1-SNAPSHOT.jar
```

### 4. Access Points

- **API Base URL**: http://localhost:8080/api/v1
- **Health Check**: http://localhost:8080/actuator/health
- **API Documentation**: http://localhost:8080/swagger-ui.html

## 📊 Performance Optimization

### Database Projections

Our implementation uses **JPA Projections** for significant performance gains:

| Method | Query Time | Memory Usage | When to Use |
|--------|------------|--------------|-------------|
| Full Entities | 137.20 ms | High | Editing, full data needed |
| **Projections** | **17.60 ms** | **59% less** | **List views, APIs** |

**87% faster** invoice list loading with optimized queries.

### Implementation Details
```java
// Fast projection query
@Query("""
    SELECT i.id as id,
           i.invoiceNumber as invoiceNumber,
           c.name as customerName,
           i.totalAmount as totalAmount
    FROM Invoice i
    JOIN i.customer c
    """)
Page<InvoiceProjection> findInvoiceList(Pageable pageable);
```

For detailed performance analysis, see the [Performance Benchmark Documentation](docs/PERFORMANCE_BENCHMARK.md).

## 🧪 Testing

### Run All Tests
```bash
./mvnw test
```

### Run Specific Tests
```bash
# Unit tests
./mvnw test -Dtest=*Test

# Performance benchmarks
./mvnw test -Dtest=SimplePerformanceTest

# Integration tests
./mvnw test -Dtest=*IntegrationTest
```

### Test Coverage
- Unit tests for all service methods
- Integration tests for API endpoints
- Performance benchmarks for critical queries
- Database migration tests

## 📝 API Overview

### Core Endpoints

#### Invoices
- `GET /api/v1/invoices` - List invoices (paginated)
- `POST /api/v1/invoices` - Create new invoice
- `GET /api/v1/invoices/{id}` - Get invoice by ID
- `PUT /api/v1/invoices/{id}` - Update invoice
- `DELETE /api/v1/invoices/{id}` - Delete invoice

#### Customers
- `GET /api/v1/customers` - List customers
- `POST /api/v1/customers` - Create customer
- `GET /api/v1/customers/{id}` - Get customer details
- `PUT /api/v1/customers/{id}` - Update customer

#### Documentation
Interactive API documentation available at `/swagger-ui.html`

## 🔧 Development

### Code Structure

**Controllers** (`api/`)
- Handle HTTP requests
- Input validation
- Response formatting

**Services** (`service/`)
- Business logic
- Transaction management
- Data transformation

**Repositories** (`repository/`)
- Database queries
- Custom queries with projections
- Data access optimization

**DTOs** (`dto/`)
- API request/response objects
- Data transfer between layers
- Validation annotations

### Adding New Features

1. **Create Domain Entity** in appropriate module
2. **Add Repository** with optimized queries
3. **Implement Service** with business logic
4. **Create Controller** with REST endpoints
5. **Add Tests** for all layers
6. **Update Documentation**

### Database Migrations

```bash
# Create new migration
./mvnw liquibase:generateChangeLog

# Run migrations
./mvnw liquibase:update

# Rollback migration
./mvnw liquibase:rollback
```

## 🔍 Monitoring & Debugging

### Health Checks
```bash
curl http://localhost:8080/actuator/health
```

### Application Metrics
```bash
curl http://localhost:8080/actuator/metrics
```

### Database Queries
Enable SQL logging in `application.yml`:
```yaml
logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

## 🚨 Error Handling

### Standardized Error Responses
```json
{
  "timestamp": "2024-02-08T20:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Invoice not found",
  "path": "/api/v1/invoices/123"
}
```

### Global Exception Handler
- `@RestControllerAdvice` for centralized error handling
- Consistent error format across all endpoints
- Proper HTTP status codes

## 🔒 Security Features

- Input validation with `@Valid`
- SQL injection prevention via JPA
- XSS protection in responses
- CORS configuration for frontend integration

## 📈 Performance Monitoring

### Key Metrics
- Response times per endpoint
- Database query performance
- Memory usage patterns
- Connection pool utilization

### Performance Tests
Run benchmarks regularly:
```bash
./mvnw test -Dtest=SimplePerformanceTest
```

## 🤝 Contributing

1. Follow the existing code structure
2. Add tests for new functionality
3. Update API documentation
4. Run performance benchmarks
5. Ensure code quality standards

## 📚 Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/)
- [JPA Projections Guide](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections)
- [Performance Benchmark](docs/PERFORMANCE_BENCHMARK.md)
- [API Documentation](http://localhost:8080/swagger-ui.html)

---

**Built for performance, scalability, and maintainability**
