# Invoice Builder App

A modern, full-stack web application for creating, managing, and downloading professional invoices. Built with performance and scalability in mind.

## What It Does

Invoice Builder helps businesses turn their data into professional invoices with just a few clicks. Perfect for freelancers, small businesses, and finance teams who need reliable invoicing without complexity.

### Key Features
- **Create Professional Invoices** - Build invoices with customer data, line items, and automatic calculations
- **Customer & Sender Management** - Store and reuse customer and business information
- **Smart Calculations** - Automatic totals, taxes, and discounts
- **PDF Generation** - Download beautiful, print-ready invoices
- **Status Tracking** - Monitor invoice status from draft to paid
- **Performance Optimized** - Lightning-fast data loading with 87% faster queries

## Project Structure

```
invoice-builder-app/
├── backend/                    # Spring Boot API server
│   ├── src/                   # Java source code
│   ├── docs/                  # Documentation
│   └── README.md               # Backend setup guide
├── frontend/                  # React web application
│   └── invoice-builder-react/ # React + Vite setup
├── docker-compose.yml           # Main Docker configuration
├── docker-compose.dev.yml       # Development overrides
├── docker-compose.prod.yml      # Production overrides
├── .env.example               # Environment variables template
└── README.md                # This file
```

## Tech Stack

### Backend
- **Framework**: Spring Boot 4.0.2
- **Language**: Java 21
- **Database**: PostgreSQL 15
- **Build Tool**: Maven
- **ORM**: Spring Data JPA with Hibernate
- **Migration**: Liquibase
- **Testing**: JUnit 5, Spring Boot Test

### Frontend
- **Framework**: React 19.2.0
- **Build Tool**: Vite 7.2.4
- **Language**: JavaScript/TypeScript
- **Styling**: CSS3 (planned: Tailwind CSS)

### DevOps & Infrastructure
- **Containerization**: Docker & Docker Compose
- **Database**: PostgreSQL 15 in Docker
- **Version Control**: Git

## Application Modules

### Core Business Modules
- **Invoice Module** - Invoice creation, management, and PDF generation
- **Customer Module** - Customer data management and relationships
- **User Module** - User authentication, roles, and access control
- **Payment Module** - Payment tracking, status, and processing
- **Reports Module** - Analytics, summaries, and dashboard metrics

### Module Integration
- **Domain-Driven Design** - Clear module boundaries
- **API Versioning** - Backward-compatible API evolution
- **Database Relationships** - Proper foreign key constraints
- **Security Layers** - Module-level access control

## Quick Start

### Prerequisites
- Docker & Docker Compose
- Git

### 1. Clone & Setup
```bash
git clone <repository-url>
cd invoice-builder-app
```

### 2. Environment Configuration
```bash
cp .env.example .env
# Edit .env with your secure credentials
```

### 3. Start Application

**Development Environment:**
```bash
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up --build
```

**Production Environment:**
```bash
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d --build
```

### 4. Access Application
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api/v1
- **Database**: localhost:5432 (internal)
- **API Documentation**: http://localhost:8080/swagger-ui.html

## Docker Operations

### Service Management

**Start All Services:**
```bash
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up --build
```

**Start Specific Service:**
```bash
# Start only backend
docker-compose up backend

# Start only database
docker-compose up postgres

# Start only frontend
docker-compose up frontend
```

**Rebuild Specific Service:**
```bash
# Rebuild backend only
docker-compose up --build backend

# Rebuild frontend only
docker-compose up --build frontend

# Force rebuild without cache
docker-compose build --no-cache backend
```

**Stop Services:**
```bash
# Stop all services
docker-compose down

# Stop specific service
docker-compose stop backend

# Remove volumes (clean start)
docker-compose down -v
```

**View Logs:**
```bash
# View all logs
docker-compose logs

# Follow specific service logs
docker-compose logs -f backend

# View last 50 lines
docker-compose logs --tail=50 backend
```

### Environment Switching

**Development to Production:**
```bash
# Stop development
docker-compose down

# Start production
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d --build
```

**Production to Development:**
```bash
# Stop production
docker-compose down

# Start development
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up --build
```

## Documentation

- **[Backend Documentation](backend/README.md)** - API details, setup, architecture, and API versioning
- **[Performance Benchmark](backend/docs/PERFORMANCE_BENCHMARK.md)** - Performance analysis and optimizations
- **[API Versioning Guide](backend/docs/API_VERSIONING.md)** - API evolution and versioning strategy
- **[Frontend Documentation](frontend/invoice-builder-react/README.md)** - UI components and setup

## Development

### Running Tests
```bash
# Backend tests
docker-compose exec backend ./mvnw test

# Performance benchmarks
docker-compose exec backend ./mvnw test -Dtest=SimplePerformanceTest

# Frontend tests
docker-compose exec frontend npm test
```

### Code Quality
- **Backend**: Lombok for clean code, Spring Boot best practices
- **Frontend**: ESLint for code quality
- **Performance**: Optimized database queries with 87% faster loading

## Performance Highlights

Our performance optimizations deliver significant improvements:

| Feature | Improvement | Impact |
|---------|-------------|---------|
| Invoice List Loading | **87% faster** | Better user experience |
| Memory Usage | **59% reduction** | Scalable architecture |
| Database Queries | **Optimized projections** | Faster response times |

See [Performance Benchmark](backend/docs/PERFORMANCE_BENCHMARK.md) for detailed analysis.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Update documentation
6. Submit a pull request

## Support

- **Documentation**: Check README files in each module
- **Issues**: Report bugs via GitHub Issues
- **Performance**: See performance benchmark documentation

---

**Built for performance, scalability, and maintainability**
