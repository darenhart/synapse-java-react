# Mutant Service

**[← Back to Main Documentation](../README.md)**

DNA sequence verification service with mutant detection algorithm.

---

## Overview

Verifies if a DNA sequence belongs to a mutant or human:
- Validates NxN DNA matrix (A, T, C, G bases)
- Detects mutants: ≥2 sequences of 4 identical consecutive letters
- Caches results in Redis (prevents duplicate processing)
- Persists all verifications to database

**Detection Algorithm:**
- Checks 4 directions: horizontal, vertical, diagonal (↘), anti-diagonal (↙)
- Early exit optimization when 2 sequences found
- O(N²) time complexity

---

## Running the Service

```bash
cd dna-demo
./mvnw spring-boot:run
```

Service runs on **http://localhost:8080**

---

## API Endpoints

### POST /mutant/

Verifies if DNA sequence is mutant.

**Request Body:**
```json
{
  "dna": [
    "ATGCGA",
    "CAGTGC",
    "TTATGT",
    "AGAAGG",
    "CCCCTA",
    "TCACTG"
  ]
}
```

**Responses:**
- **200 OK** - Mutant detected
- **403 Forbidden** - Human (not mutant)
- **400 Bad Request** - Invalid DNA format

**Mutant Example:**
```bash
curl -X POST http://localhost:8080/mutant/ \
  -H "Content-Type: application/json" \
  -d '{"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]}'
# Returns: 200
```

**Human Example:**
```bash
curl -X POST http://localhost:8080/mutant/ \
  -H "Content-Type: application/json" \
  -d '{"dna":["ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"]}'
# Returns: 403
```

---

## Testing

```bash
./mvnw test          
```

**Code Coverage:** See `target/site/jacoco/index.html` after running tests

---

## Tech Stack

- **Spring Boot 4.0.2**
- **Spring Data JPA** (Database access)
- **Spring Data Redis** (Caching)
- **Spring Validation** (Request validation)
- **H2 Database** (Shared with Stats Service)
- **JaCoCo** (Code coverage)
- **Java 17**

---

**[← Back to Main Documentation](../README.md)**
