# DNA Mutant Detector - Microservices Architecture

A scalable microservices-based system for detecting mutant DNA sequences and tracking verification statistics. Built to handle high traffic loads (100 to 1M requests/second).

## 📐 Architecture Overview

```
                        ┌──────────────┐
                        │   Frontend   │
                        │  (Port 5173) │
                        └──────┬───────┘
                               │
          ┌────────────────────┼────────────────────┐
          │                    │                    │
   ┌──────▼──────┐      ┌──────▼──────┐     ┌──────▼──────┐
   │   Mutant    │◄────►│  Database   │◄───►│    Stats    │
   │   Service   │      │     (H2)    │     │   Service   │
   │ (Port 8080) │      └─────────────┘     │ (Port 8081) │
   └──────┬──────┘                          └──────┬──────┘
          │                                         │
          └─────────────────┬───────────────────────┘
                            │
                     ┌──────▼──────┐
                     │    Redis    │
                     │   (Cache)   │
                     └─────────────┘
```

### Services

1. **Mutant Service** (`dna-demo/`) - Verifies DNA sequences and persists results
2. **Stats Service** (`stats-service/`) - Provides real-time statistics
3. **Frontend** (`dna-frontend/`) - React web interface for testing

## 🚀 Quick Start

### Prerequisites

- **Java 17** or higher
- **Maven 3.6+** (or use included Maven wrapper)
- **Node.js 18+** (for frontend)
- **Redis** (optional for caching, recommended for production)

### Start Redis (Recommended)
```bash
# Using Docker
docker run -d -p 6379:6379 redis:latest

# Or using Homebrew on Mac
brew services start redis
```

### Option 1: Start All Services (Recommended)
```bash
# Install dependencies (first time only)
npm install
cd dna-frontend && npm install && cd ..

# Start all services in one terminal
npm start
```

This starts:
- **Mutant Service** (Port 8080) - Green logs
- **Stats Service** (Port 8081) - Blue logs
- **Frontend** (Port 5173) - Magenta logs

Press `Ctrl+C` to stop all services.

### Option 2: Start Services Individually
```bash
# Terminal 1: Mutant Service (Port 8080)
cd dna-demo
./mvnw spring-boot:run

# Terminal 2: Stats Service (Port 8081)
cd stats-service
./mvnw spring-boot:run

# Terminal 3: Frontend (Port 5173)
cd dna-frontend
npm run dev
```

## 🌐 Web Interface

Open [http://localhost:5173](http://localhost:5173) in your browser to use the web interface:

- Input DNA sequences (6x6 grid)
- Generate random DNA for testing
- View mutant detection results
- See real-time statistics

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
- [Stats Service Documentation](stats-service/)
- [Frontend Documentation](dna-frontend/README.md)

---

**Built with TDD | Ready for High-Scale Production** 🧬

## Trade offs
Redis for caching and stats storage: Trade-off between speed and complexity. Fast reads/writes but adds infrastructure cost.
H2 vs PostgresSQL: Trade-off between simplicity and features. H2 is easy to set up, PostgresSQL is more robust.

Service communication:
Shared database vs Message queue events vs Stats-service calls Mutant-service API

## Open Questions
Would only “/stats/” receive high traffic, or both endpoints?
