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
│   └── pom.xml               # Maven configuration
├── frontend/                  # React web application
│   └── invoice-builder-react/ # React + Vite setup
├── compose.yaml              # Docker database setup
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

### Docker Benefits
- **No local setup required** - Just Docker installed
- **Kubernetes ready** - Easy cloud deployment
- **Consistent environment** - Same everywhere
- **Health checks** - Automatic monitoring
- **Hot reload** - Development with live updates

## Documentation

- **[Backend Documentation](backend/README.md)** - API details, setup, and architecture
- **[Performance Benchmark](backend/docs/PERFORMANCE_BENCHMARK.md)** - Performance analysis and optimizations
- **[Frontend Documentation](frontend/invoice-builder-react/README.md)** - UI components and setup

## Who This Is For

### Perfect For
- **Freelancers** - Invoice clients professionally
- **Small Businesses** - Manage billing without expensive software
- **Consultants** - Track project-based billing
- **Finance Teams** - Standardize invoicing processes

### Business Size
- 1-50 employees
- 100-10,000 invoices per year
- Need for simple, reliable invoicing

## Development

### Running Tests
```bash
# Backend tests
cd backend
./mvnw test

# Performance benchmarks
./mvnw test -Dtest=SimplePerformanceTest

# Frontend tests
cd frontend/invoice-builder-react
npm test
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
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

- **Documentation**: Check README files in each module
- **Issues**: Report bugs via GitHub Issues
- **Performance**: See performance benchmark documentation

---

**Built with care for businesses that need reliable, fast invoicing**
