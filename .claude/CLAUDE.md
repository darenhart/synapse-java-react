# DNA Mutant Detector - Project Context

## Overview
Microservices-based system for detecting mutant DNA sequences and tracking statistics. Designed for high-scale production (100-1M req/s).

## Architecture

```
Mutant Service (8080) ←→ Database (H2) ←→ Stats Service (8081)
         ↓                                        ↓
         └────────────→ Redis (Cache) ←──────────┘
```

## Services

### 1. Mutant Service (`dna-demo/`)
- **Port**: 8080
- **Purpose**: Verifies DNA sequences, persists results
- **Tech**: Spring Boot, H2, Redis, Java 17
- **Key Endpoint**: `POST /mutant/`
  - Returns 200 if mutant detected
  - Returns 403 if human (non-mutant)
- **Detection Logic**: Finds 4+ identical consecutive letters (horizontal, vertical, diagonal)

### 2. Stats Service (`stats-service/`)
- **Port**: 8081
- **Purpose**: Provides real-time verification statistics
- **Tech**: Spring Boot, H2, Redis, Java 17
- **Key Endpoint**: `GET /stats/`
  - Returns: `{"count_mutant_dna": N, "count_human_dna": M, "ratio": R}`

## Project Structure

```
.
├── dna-demo/                    # Mutant verification service
│   ├── src/main/java/.../
│   │   ├── controller/          # MutantController
│   │   ├── service/             # DnaService, MutantDetector
│   │   ├── repository/          # DnaRecordRepository
│   │   ├── entity/              # DnaRecord, DnaStats
│   │   ├── validator/           # DnaValidator
│   │   └── util/                # DnaHashUtil
│   └── src/test/                # 39 unit tests
│
├── stats-service/               # Statistics service
│   ├── src/main/java/.../
│   │   ├── controller/          # StatsController
│   │   ├── service/             # StatsService
│   │   ├── repository/          # DnaStatsRepository
│   │   └── entity/              # DnaStats
│   └── src/test/                # Unit tests
│
└── dna-typescript/              # TypeScript reference implementation
    ├── dna.ts                   # Core logic
    └── dna.test.ts              # Tests
```

## Key Patterns & Conventions

### DNA Validation
- Must be NxN matrix (square)
- Only valid bases: A, T, C, G (case-insensitive)
- Validated by `DnaValidator` before processing

### Mutant Detection Algorithm
- Checks 4 directions: horizontal, vertical, diagonal (↘), anti-diagonal (↙)
- Needs ≥2 sequences of 4 identical consecutive letters
- Optimized for early exit when 2 sequences found

### Caching Strategy
- Uses Redis for DNA verification results
- Hash-based keys (SHA-256 of DNA sequence)
- Prevents duplicate processing of same DNA

### Database
- H2 in-memory database (file-based persistence)
- Shared between both services
- Tables: `dna_record`, `dna_stats`

## Development Workflow

### Prerequisites
- Java 17+
- Maven 3.6+ (or use `./mvnw`)
- Redis running on localhost:6379

### Start Services
```bash
# Terminal 1: Mutant Service
cd dna-demo && ./mvnw spring-boot:run

# Terminal 2: Stats Service
cd stats-service && ./mvnw spring-boot:run
```

### Run Tests
```bash
cd dna-demo && ./mvnw test        # 39 tests
cd stats-service && ./mvnw test
```

### Test Coverage Requirement
- **Minimum**: 80% code coverage
- Measured by JaCoCo (Maven plugin)

## API Examples

### Verify Mutant DNA (200)
```bash
curl -X POST http://localhost:8080/mutant/ \
  -H "Content-Type: application/json" \
  -d '{"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]}'
```

### Verify Human DNA (403)
```bash
curl -X POST http://localhost:8080/mutant/ \
  -H "Content-Type: application/json" \
  -d '{"dna":["ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"]}'
```

### Get Statistics
```bash
curl http://localhost:8081/stats/
# {"count_mutant_dna":1,"count_human_dna":1,"ratio":0.5}
```

## Trade-offs & Decisions

### Redis vs In-Memory Cache
- **Choice**: Redis
- **Reason**: Shared cache across service restarts, production-ready
- **Trade-off**: Added infrastructure complexity

### H2 vs PostgreSQL
- **Choice**: H2 (currently)
- **Reason**: Zero-config, fast development
- **Trade-off**: PostgreSQL would be more robust for production

### Service Communication
- **Current**: Shared database
- **Alternatives considered**: Message queue events, REST API calls
- **Trade-off**: Shared DB is simple but creates coupling

## Open Questions / Future Work
- Integration tests across both services
- Load/performance testing (100-1M req/s validation)
- Contract testing between services
- End-to-end API testing
- Cloud deployment strategy

## When Working on This Project

1. **Always run tests** before suggesting changes
2. **Maintain >80% coverage** when adding new code
3. **Test both services** if changes affect shared entities (DnaStats)
4. **Follow existing patterns** (validators, repositories, services)
5. **Consider caching** for performance-critical operations
