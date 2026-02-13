# DNA Mutant Detector - Project Context

## Overview
Microservices-based system for detecting mutant DNA sequences and tracking statistics. Designed for high-scale production (100-1M req/s).

## Architecture

**Event-Driven Microservices Architecture:**

```
dna-demo (Port 8080)                    stats-service (Port 8081)
       ↓                                         ↓
   DnaRecord table                         DnaStats table
       ↓                                         ↑
  Publishes event  →  Redis Pub/Sub  →  Subscribes to events
                     (dna-events channel)
       ↓
   Redis Cache (DNA results)
```

**Key architectural decisions:**
- **Event-Driven Communication**: Services communicate via Redis Pub/Sub (eventual consistency)
- **Service Independence**: Each service owns its database tables and domain logic
- **No Shared Code**: Intentional duplication for service autonomy (e.g., DnaVerifiedEvent)
- **JSON Serialization**: Events use JSON for cross-service compatibility
- **Cache Eviction**: Stats cache is automatically cleared when events are processed

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
├── dna-demo/                    # Mutant verification service (Publisher)
│   ├── src/main/java/.../
│   │   ├── controller/          # MutantController
│   │   ├── service/             # DnaService, MutantDetector
│   │   ├── repository/          # DnaRecordRepository
│   │   ├── entity/              # DnaRecord
│   │   ├── event/               # DnaVerifiedEvent, DnaEventPublisher (NEW)
│   │   ├── validator/           # DnaValidator
│   │   ├── util/                # DnaHashUtil
│   │   └── config/              # RedisConfig (JSON serialization)
│   └── src/test/                # 49 unit tests
│
├── stats-service/               # Statistics service (Subscriber)
│   ├── src/main/java/.../
│   │   ├── controller/          # StatsController
│   │   ├── service/             # StatsService, StatsEventProcessor (NEW)
│   │   ├── repository/          # DnaStatsRepository
│   │   ├── entity/              # DnaStats
│   │   ├── event/               # DnaVerifiedEvent, DnaEventSubscriber (NEW)
│   │   └── config/              # RedisConfig, DatabaseInitializer
│   └── src/test/                # 27 unit tests
│
└── dna-typescript/              # TypeScript reference implementation
    ├── dna.ts                   # Core logic
    └── dna.test.ts              # Tests
```

**Service Ownership:**
- `dna-demo` owns: `dna_record` table, DNA verification logic, event publishing
- `stats-service` owns: `dna_stats` table, statistics calculation, event subscription

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
- H2 in-memory database (file-based persistence at `/tmp/dnadb`)
- Shared database file, but separate table ownership:
  - `dna-demo` owns `dna_record` table
  - `stats-service` owns `dna_stats` table
- Each service initializes its own tables on startup

### Event-Driven Communication
- **Channel**: Redis Pub/Sub channel `dna-events`
- **Event Format**: JSON (using GenericJackson2JsonRedisSerializer)
- **Event Schema**: `{dnaHash: string, isMutant: boolean, timestamp: long}`
- **Flow**: dna-demo publishes → Redis → stats-service subscribes
- **Eventual Consistency**: Stats update within milliseconds of DNA verification
- **Cache Eviction**: Stats cache automatically cleared when events processed

## Development Workflow

### Prerequisites
- Java 17+
- Maven 3.6+ (or use `./mvnw`)
- **Redis running on localhost:6379** (REQUIRED - must start first)
- Docker (optional, for running Redis: `docker run -d -p 6379:6379 redis:latest`)

### Start Services (IMPORTANT: Order Matters!)
```bash
# Terminal 1: Redis (must start FIRST)
docker run -d -p 6379:6379 redis:latest

# Terminal 2: Stats Service (must start BEFORE dna-demo to subscribe to events)
cd stats-service && ./mvnw spring-boot:run

# Terminal 3: Mutant Service (start LAST)
cd dna-demo && ./mvnw spring-boot:run
```

**Why order matters:** stats-service must subscribe to the `dna-events` channel before dna-demo starts publishing events, otherwise early events may be lost.

### Run Tests
```bash
cd dna-demo && ./mvnw test        # 49 tests
cd stats-service && ./mvnw test   # 27 tests
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

### Event-Driven Architecture (Redis Pub/Sub)
- **Choice**: Redis Pub/Sub for service communication
- **Reason**: Decouples services, supports 100-1M req/s scale, easy to add new consumers
- **Trade-off**: Eventual consistency (stats lag by ~100ms), events lost if subscriber is down
- **Alternative considered**: Shared database writes (removed due to tight coupling)

### JSON Serialization for Events
- **Choice**: GenericJackson2JsonRedisSerializer
- **Reason**: Cross-service compatibility, human-readable, supports intentional duplication
- **Trade-off**: Slightly larger payload than binary serialization
- **Alternative considered**: Java serialization (incompatible across packages)

### Intentional Code Duplication
- **Choice**: Duplicate `DnaVerifiedEvent` in both services
- **Reason**: Service autonomy > DRY principle in microservices
- **Trade-off**: Need to keep event schemas compatible manually
- **Alternative considered**: Shared library (creates deployment coupling)

### Redis for Caching
- **Choice**: Redis
- **Reason**: Shared cache across service restarts, production-ready, also used for Pub/Sub
- **Trade-off**: Added infrastructure complexity

### H2 vs PostgreSQL
- **Choice**: H2 (currently)
- **Reason**: Zero-config, fast development, shared file at `/tmp/dnadb`
- **Trade-off**: PostgreSQL would be more robust for production

### Cache Eviction Strategy
- **Choice**: @CacheEvict on event processing
- **Reason**: Ensures stats are always fresh after database updates
- **Trade-off**: Cache miss on next request after update

## Event Schema

### DnaVerifiedEvent
```json
{
  "dnaHash": "sha256_hash_of_dna_sequence",
  "isMutant": true|false,
  "timestamp": 1234567890
}
```

**Published by:** dna-demo (after saving DNA verification result)
**Consumed by:** stats-service (updates stats counters)
**Channel:** `dna-events` (Redis Pub/Sub)

### Monitoring Events
```bash
# Check subscriber count
docker exec <redis-container> redis-cli PUBSUB NUMSUB dna-events
# Expected output: "dna-events" 1

# Monitor events in real-time
docker exec <redis-container> redis-cli MONITOR
```

## Open Questions / Future Work
- **Event Persistence**: Switch to Redis Streams for guaranteed delivery
- **Dead Letter Queue**: Handle failed event processing with retry logic
- **Event Versioning**: Strategy for evolving event schema without breaking changes
- Load/performance testing (100-1M req/s validation)
- Contract testing between services (ensure event schema compatibility)
- End-to-end API testing with event verification
- Cloud deployment strategy (ensure proper service startup ordering)
- Reconciliation job (detect/fix stats drift if events are lost)

## When Working on This Project

1. **Always run tests** before suggesting changes (49 tests in dna-demo, 27 in stats-service)
2. **Maintain >80% coverage** when adding new code (measured by JaCoCo)
3. **Test event flow** when changing event-related code (verify both publisher and subscriber)
4. **Follow service ownership** - don't cross boundaries:
   - dna-demo: owns `dna_record` table, publishes events only
   - stats-service: owns `dna_stats` table, processes events only
5. **Event schema changes** must be backward compatible (both services may not deploy simultaneously)
6. **Start services in order**: Redis → stats-service → dna-demo
7. **Check logs** for event publishing/receiving when debugging stats issues
8. **Cache considerations**: Remember `@CacheEvict` when updating cached data
