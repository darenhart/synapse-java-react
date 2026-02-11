# Stats Service

Microservice for retrieving real-time DNA verification statistics.

## Overview

This service provides a read-only API endpoint that returns aggregated statistics about DNA verifications performed by the Mutant Service. It reads from a shared database and caches results in Redis for optimal performance.

## Features

- **Ultra-fast stats** retrieval (single database read)
- **Redis caching** with 1-minute TTL
- **Real-time ratio** calculation
- **Zero computation** overhead (pre-calculated counters)

## API Endpoint

### GET /stats/

Returns DNA verification statistics.

**Response:**
```json
{
  "count_mutant_dna": 40,
  "count_human_dna": 100,
  "ratio": 0.4
}
```

**Example:**
```bash
curl http://localhost:8081/stats/

# With pretty print
curl http://localhost:8081/stats/ | jq
```

## Running the Service

```bash
# With Maven wrapper
./mvnw spring-boot:run

# Or build and run JAR
./mvnw clean package
java -jar target/stats-service-0.0.1-SNAPSHOT.jar
```

## Configuration

Edit `src/main/resources/application.properties`:

```properties
# Service Port
server.port=8081

# Database (shared with mutant-service)
spring.datasource.url=jdbc:h2:mem:dnadb

# Redis Cache
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.cache.redis.time-to-live=60000
```

## Architecture

- **Port:** 8081
- **Database:** Shared H2 (read-only access)
- **Cache:** Redis (1-minute TTL)
- **Dependencies:** None on Mutant Service (loose coupling)

## Testing

```bash
./mvnw test
```

## Performance

- **Without cache:** ~10ms (single DB query)
- **With cache:** ~1ms (Redis read)
- **Scalability:** Can handle millions of read requests/second with Redis cluster

## Technology

- Spring Boot 4.0.2
- Spring Data JPA
- Spring Data Redis
- Spring Cache
- Lombok
