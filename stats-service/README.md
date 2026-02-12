# Stats Service

**[← Back to Main Documentation](../README.md)**

Statistics aggregation service for DNA verification data.

---

## Overview

Provides real-time statistics about DNA verifications:
- Total mutant DNA count
- Total human DNA count  
- Mutant-to-total ratio

Uses Redis caching (60s TTL) to reduce database load.

---

## Running the Service

```bash
cd stats-service
./mvnw spring-boot:run
```

Service runs on **http://localhost:8081**

---

## API Endpoints

### GET /stats/

Returns verification statistics.

**Response (200 OK):**
```json
{
  "count_mutant_dna": 40,
  "count_human_dna": 100,
  "ratio": 0.28571
}
```

**Example:**
```bash
curl http://localhost:8081/stats/
```

---

## Testing

```bash
./mvnw test
```

---

## Tech Stack

- **Spring Boot 4.0.2**
- **Spring Data JPA** (Database access)
- **Spring Data Redis** (Caching)
- **H2 Database** (Shared with Mutant Service)
- **Java 17**

---

**[← Back to Main Documentation](../README.md)** | **[Architecture](../ARCHITECTURE.md)**
