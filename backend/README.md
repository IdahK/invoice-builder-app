# Invoice Builder Backend

RESTful API server for Invoice Builder application. Built with Spring Boot and optimized for performance.

## Purpose

The backend provides a robust, scalable API for managing invoices, customers, and business data. It handles all business logic, data persistence, and PDF generation while maintaining high performance through optimized database queries.

## Architecture

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

## Tech Stack

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

## Getting Started

### Prerequisites
- Docker & Docker Compose (Recommended)
- Java 21+ (for local development)
- Maven 3.8+ (for local development)
- Git

### 1. Environment Setup

**Copy environment variables:**
```bash
cp .env.example .env
# Edit .env with your secure credentials
```

**Important Security Notes:**
- Never commit `.env` file to version control
- Use strong, unique passwords
- Generate random JWT secrets (minimum 32 characters)
- Use app passwords for SMTP, not regular passwords

### 2. Docker Setup (Recommended)

**Development Environment:**
```bash
# From project root (invoice-builder-app/)
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up --build
```

**Production Environment:**
```bash
# From project root
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d --build
```

**Benefits of Docker Setup:**
- Consistent environment across all machines
- No local Java/Maven installation required
- Ready for Kubernetes deployment
- Health checks and automatic restarts
- Volume persistence for data

### 3. Local Development (Optional)

**If you prefer local development:**

Database Setup:
```bash
# Start only database
docker-compose up postgres -d

# Or use local PostgreSQL
createdb invoicebuilder
```

Run Application:
```bash
cd backend
./mvnw spring-boot:run
```

### 4. Access Points

- **API Base URL**: http://localhost:8080/api/v1
- **Health Check**: http://localhost:8080/actuator/health
- **API Documentation**: http://localhost:8080/swagger-ui.html

## Performance Optimization

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

For detailed performance analysis, see [Performance Benchmark Documentation](docs/PERFORMANCE_BENCHMARK.md).

## Testing

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

## API Overview

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

## Development

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

## Monitoring & Debugging

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

## API Versioning

For detailed information about API versioning strategy, evolution, and backward compatibility, see the [API Versioning Guide](docs/API_VERSIONING.md).

### Versioning Strategy
- **Semantic Versioning**: Use MAJOR.MINOR.PATCH format
- **Backward Compatibility**: Maintain compatibility within major versions
- **Deprecation Process**: Clear deprecation timeline for breaking changes
- **Documentation**: Always update API docs with version changes

### Current Version
- **API Version**: v1.0.0
- **Base Path**: `/api/v1/`
- **Status**: Active Development

## Error Handling

### Standard Error Responses

The API uses a consistent error response format across all endpoints:

```json
{
  "timestamp": "2024-02-08T20:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Invoice not found with ID: 550e8400-e29b-41d4-a716-446655440000",
  "path": "/api/v1/invoices/550e8400-e29b-41d4-a716-446655440000"
}
```

### Expected Error Codes

| HTTP Status | Error Code | Description | Example Scenario |
|-------------|-------------|-------------|------------------|
| **400** | Bad Request | Invalid input data | Missing required fields, invalid email format |
| **401** | Unauthorized | Authentication required | Missing or invalid API token |
| **403** | Forbidden | Insufficient permissions | User trying to access another user's data |
| **404** | Not Found | Resource doesn't exist | Invoice or customer ID not found |
| **409** | Conflict | Resource conflict | Duplicate invoice number, customer already exists |
| **422** | Unprocessable Entity | Validation failed | Invalid tax rate, negative amounts |
| **429** | Too Many Requests | Rate limit exceeded | API rate limiting triggered |
| **500** | Internal Server Error | Server error | Database connection failed, unexpected error |

### Error Response Structure

All error responses follow this structure:

```json
{
  "timestamp": "ISO 8601 timestamp",
  "status": "HTTP status code",
  "error": "HTTP error reason",
  "message": "Human-readable error message",
  "path": "Request path that caused the error",
  "details": {  
    "field": "email",
    "rejectedValue": "invalid-email",
    "reason": "Invalid email format"
  }
}
```

### Global Exception Handler

The application uses `@RestControllerAdvice` for centralized error handling:

- **Consistent Format**: All errors use the same response structure
- **Proper HTTP Status Codes**: Appropriate status codes for each error type
- **Input Validation**: Automatic validation error responses
- **Resource Not Found**: Standardized 404 responses for all entities
- **Database Constraints**: Proper handling of unique constraint violations
- **Security**: Secure error responses that don't leak sensitive information

### Client-Side Error Handling

Frontend applications should:

1. **Check HTTP Status** - Handle different status codes appropriately
2. **Parse Error Message** - Display user-friendly messages
3. **Handle Validation Errors** - Show field-specific validation feedback
4. **Implement Retry Logic** - For 5xx server errors
5. **Redirect on Auth Errors** - For 401/403 responses

Example error handling in JavaScript:
```javascript
try {
  const response = await fetch('/api/v1/invoices', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(invoiceData)
  });
  
  if (!response.ok) {
    const error = await response.json();
    handleApiError(error);
    return;
  }
  
  return await response.json();
} catch (error) {
  handleNetworkError(error);
}
```

## Security Features

- **Input Validation**: `@Valid` annotations on all request DTOs
- **SQL Injection Prevention**: JPA parameterized queries
- **XSS Protection**: Input sanitization and secure headers
- **CORS Configuration**: Proper CORS setup for frontend integration
- **Secure Error Responses**: No sensitive data in error messages

## Performance Monitoring

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

## Contributing

1. Follows existing code structure
2. Add tests for new functionality
3. Update API documentation
4. Run performance benchmarks
5. Ensure code quality standards

## Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/)
- [JPA Projections Guide](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections)
- [Performance Benchmark](docs/PERFORMANCE_BENCHMARK.md)
- [API Documentation](http://localhost:8080/swagger-ui.html)

---

**Built for performance, scalability, and maintainability**
