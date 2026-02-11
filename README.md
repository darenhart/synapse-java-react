# DNA Mutant Detector - Microservices Architecture

A scalable microservices-based system for detecting mutant DNA sequences and tracking verification statistics. Built to handle high traffic loads (100 to 1M requests/second).

## 📐 Architecture Overview

```
┌─────────────────┐         ┌──────────────┐         ┌──────────────┐
│  Mutant Service │ ◄─────► │   Database   │ ◄─────► │ Stats Service│
│   (Port 8080)   │         │     (H2)     │         │  (Port 8081) │
└─────────────────┘         └──────────────┘         └──────────────┘
         │                                                     │
         └─────────────────────┬───────────────────────────────┘
                               │
                        ┌──────▼──────┐
                        │    Redis    │
                        │   (Cache)   │
                        └─────────────┘
```

### Services

1. **Mutant Service** (`dna-demo/`) - Verifies DNA sequences and persists results
2. **Stats Service** (`stats-service/`) - Provides real-time statistics

## 🚀 Quick Start

### Prerequisites

- **Java 17** or higher
- **Maven 3.6+** (or use included Maven wrapper)
- **Redis** (optional for caching, recommended for production)

### Start Redis (Recommended)
```bash
# Using Docker
docker run -d -p 6379:6379 redis:latest

# Or using Homebrew on Mac
brew services start redis
```

### Start Both Services
```bash
# Terminal 1: Mutant Service (Port 8080)
cd dna-demo
./mvnw spring-boot:run

# Terminal 2: Stats Service (Port 8081)
cd stats-service
./mvnw spring-boot:run
```

## 📡 API Examples

### Verify Mutant DNA (200 OK)
```bash
curl -X POST http://localhost:8080/mutant/ \
  -H "Content-Type: application/json" \
  -d '{"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]}' \
  -w "%{http_code}\n"
# Output: 200
```

### Verify Human DNA (403 Forbidden)
```bash
curl -X POST http://localhost:8080/mutant/ \
  -H "Content-Type: application/json" \
  -d '{"dna":["ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"]}' \
  -w "%{http_code}\n"
# Output: 403
```

### Get Statistics
```bash
curl http://localhost:8081/stats/
# Output: {"count_mutant_dna":1,"count_human_dna":1,"ratio":0.5}
```

## 🧪 Run Tests

```bash
# Mutant Service (39 tests)
cd dna-demo
./mvnw test

# Stats Service
cd stats-service
./mvnw test
```

## 📚 Full Documentation

See individual service README files:
- [Mutant Service Documentation](dna-demo/README.md)
- Stats Service Documentation (in stats-service/)

---

**Built with TDD | Ready for High-Scale Production** 🧬

## Trade offs
Redis for caching and stats storage: Trade-off between speed and complexity. Fast reads/writes but adds infrastructure cost.
H2 vs PostgresSQL: Trade-off between simplicity and features. H2 is easy to set up, PostgresSQL is more robust.

Service communication:
Shared database vs Message queue events vs Stats-service calls Mutant-service API

## Open Questions
Would only “/stats/” receive high traffic, or both endpoints?
