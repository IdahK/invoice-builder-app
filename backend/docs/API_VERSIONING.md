# API Versioning Implementation

This document describes the API versioning strategy implemented for the Invoice Builder application.

## Overview

The application uses **URI Path Versioning** as the primary versioning strategy, with support for additional versioning methods.

## Current Version

- **Current Version**: `v1`
- **Default Version**: `v1`
- **Supported Versions**: `v1`

## Versioning Methods

### 1. URI Path Versioning (Primary)
```
/api/v1/customers
/api/v1/invoices
/api/v1/senders
```

### 2. Header Versioning
```
GET /api/customers
Headers:
  API-Version: v1
```

### 3. Query Parameter Versioning
```
GET /api/customers?version=v1
```

## API Endpoints

### Version Discovery
- `GET /api/versions` - Get supported API versions and documentation URLs
- `GET /api/health` - Get API health status
- `GET /api/{resource}/version` - Get current version information for a resource

### Version-Specific Endpoints
- `GET /api/v1/customers` - Customer management (v1)
- `GET /api/v1/invoices` - Invoice management (v1)
- `GET /api/v1/senders` - Sender management (v1)

## Implementation Components

### Core Classes

1. **ApiVersionConfig** - Configuration properties for versioning
2. **ApiVersionInterceptor** - Intercepts requests to handle version extraction and validation
3. **ApiVersionUtils** - Utility class for version management
4. **BaseApiController** - Base controller with common versioning functionality
5. **UnsupportedApiVersionException** - Custom exception for invalid versions

### Configuration Files

- `application-api.yml` - API versioning configuration
- `WebMvcConfig` - Registers the versioning interceptor

## Usage Examples

### Making Versioned Requests

#### Using URI Path (Recommended)
```bash
# v1 API
curl -X GET "http://localhost:8080/api/v1/customers"
```

#### Using Header
```bash
curl -X GET "http://localhost:8080/api/customers" \
  -H "API-Version: v1"
```

#### Using Query Parameter
```bash
curl -X GET "http://localhost:8080/api/customers?version=v1"
```

### Version Discovery
```bash
# Get supported versions
curl -X GET "http://localhost:8080/api/versions"

# Get API health
curl -X GET "http://localhost:8080/api/health"

# Get current version info
curl -X GET "http://localhost:8080/api/v1/customers/version"
```

## Response Headers

All API responses include version headers:
```
API-Version: v1
API-Supported-Versions: v1
```

## Error Handling

### Unsupported Version
If an unsupported version is requested:
```json
{
  "status": "NOT_FOUND",
  "errorType": "ERROR",
  "message": "API version 'v2' is not supported. Supported versions: v1",
  "errors": ["API version not supported"],
  "developerMessage": "org.invoicebuilder.exception.UnsupportedApiVersionException: API version 'v2' is not supported. Supported versions: v1"
}
```

Headers included:
```
X-Supported-Versions: v1
API_ERROR_TYPE: UnsupportedApiVersionException
```

## Migration Strategy

### Phase 1: Current State (v1)
- ✅ Implemented URI path versioning
- ✅ Comprehensive exception handling
- ✅ Version discovery endpoints
- ✅ Swagger documentation

### Phase 2: Introduce v2 (Future)
- Create parallel `/api/v2/` controllers
- Implement breaking changes in v2
- Add deprecation warnings to v1
- Provide migration guides

### Phase 3: Deprecation (Future)
- Add deprecation headers to v1 endpoints
- Monitor v1 usage
- Plan v1 sunset

## Configuration

### application-api.yml
```yaml
api:
  version: v1
  default-version: v1
  enable-version-header: true
  enable-version-parameter: true
  supported-versions: v1,v2
```

### Enable Swagger UI
Update `application.yml`:
```yaml
springdoc:
  swagger-ui:
    enabled: true
    path: /swagger-ui.html
  api-docs:
    path: /api-docs
```

## Best Practices

1. **Semantic Versioning**: Use semantic versioning (v1.0, v1.1, v2.0)
2. **Backward Compatibility**: Maintain v1 until v2 is stable
3. **Clear Documentation**: Document breaking changes
4. **Gradual Migration**: Allow clients to migrate gradually
5. **Deprecation Timeline**: Communicate deprecation clearly

## Testing

### Version Validation Tests
```java
@Test
void testSupportedVersion() {
    assertTrue(ApiVersionUtils.isVersionSupported("v1"));
}

@Test
void testUnsupportedVersion() {
    assertThrows(UnsupportedApiVersionException.class, 
        () -> ApiVersionUtils.validateVersion("v99"));
}
```

### Integration Tests
```java
@Test
void testVersionHeaders() {
    ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/customers", String.class);
    assertEquals("v1", response.getHeaders().getFirst("API-Version"));
}
```

## Future Enhancements

1. **Rate Limiting by Version**: Different rate limits for different versions
2. **Feature Flags**: Enable/disable features by version
3. **Analytics**: Track version usage patterns
4. **Auto-Migration**: Automatic response format migration
5. **Version-Specific Caching**: Different cache strategies per version

## Support

For questions about API versioning:
- Check the Swagger documentation: `/swagger-ui.html`
- Use the version discovery endpoint: `/api/versions`
- Review the API health status: `/api/health`
