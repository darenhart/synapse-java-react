# DNA Mutant Detector API

A Spring Boot REST API that detects if a DNA sequence belongs to a mutant based on patterns of repeated nitrogenous bases.

## Problem Description

Magneto needs to identify mutants to help in his recruitment efforts. A human is classified as a **mutant** if their DNA contains **more than one sequence** of four identical letters (A, T, C, G) arranged:
- Horizontally
- Vertically
- Diagonally (both directions: ↘ and ↙)

### Examples

**Non-Mutant DNA** (only 1 sequence):
```
A T G C G A
C A G T G C
T T A T T T
A G A C G G
G C G T C A
T C A C T G
```

**Mutant DNA** (2+ sequences):
```
A T G C G A
C A G T G C
T T A T G T
A G A A G G  ← Horizontal AAAA
C C C C T A  ← Horizontal CCCC
T C A C T G
```

## Technology Stack

- **Java 17**
- **Spring Boot 4.0.2**
- **Spring Data JPA** (for future database integration)
- **H2 Database** (in-memory)
- **Lombok** (reducing boilerplate)
- **Maven** (build tool)
- **JUnit 5** & **Mockito** (testing)

## Project Structure

```
src/
├── main/
│   └── java/com/example/dna_demo/
│       ├── controller/      # REST controllers
│       ├── service/         # Business logic
│       ├── validator/       # Input validation
│       ├── dto/            # Data transfer objects
│       ├── exception/      # Exception handlers
│       └── DnaDemoApplication.java
└── test/
    └── java/com/example/dna_demo/
        ├── service/        # Service tests
        ├── validator/      # Validator tests
        └── DnaDemoApplicationTests.java
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+ (or use the included Maven wrapper)

### Installation & Running

1. **Clone the repository**
   ```bash
   cd /Users/danielarenhart/repos/mutant/dna-demo
   ```

2. **Run tests**
   ```bash
   ./mvnw test
   ```

3. **Start the application**
   ```bash
   ./mvnw spring-boot:run
   ```

   The API will be available at `http://localhost:8080`

4. **Build the JAR**
   ```bash
   ./mvnw clean package
   java -jar target/dna-demo-0.0.1-SNAPSHOT.jar
   ```

## API Documentation

### POST /mutant/

Detects if a DNA sequence belongs to a mutant.

**Request:**
```bash
POST /mutant/
Content-Type: application/json

{
  "dna": ["ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"]
}
```

**Responses:**

- **200 OK** - DNA is mutant
  ```bash
  curl -X POST http://localhost:8080/mutant/ \
    -H "Content-Type: application/json" \
    -d '{"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]}'
  ```

- **403 Forbidden** - DNA is not mutant
  ```bash
  curl -X POST http://localhost:8080/mutant/ \
    -H "Content-Type: application/json" \
    -d '{"dna":["ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"]}'
  ```

- **400 Bad Request** - Invalid DNA (with error message)
  ```bash
  # Invalid character
  curl -X POST http://localhost:8080/mutant/ \
    -H "Content-Type: application/json" \
    -d '{"dna":["ATGCGA","CAGTGC","TTATXT","AGACGG","GCGTCA","TCACTG"]}'

  # Response:
  # {"error":"Invalid character found in row 2. Only A, T, C, G are allowed"}

  # Non-square matrix
  curl -X POST http://localhost:8080/mutant/ \
    -H "Content-Type: application/json" \
    -d '{"dna":["ATGCGA","CAGTGC","TTATGT"]}'

  # Response:
  # {"error":"DNA matrix must be NxN (square)"}
  ```

## Validation Rules

The API validates all DNA sequences and returns `400 Bad Request` if:

- DNA is `null` or empty
- Matrix is not square (NxN)
- Matrix is smaller than 4x4 (minimum size to detect sequences)
- Contains invalid characters (only A, T, C, G allowed - uppercase)
- Any row is `null`

## Algorithm Complexity

The mutant detection algorithm is optimized for performance:

- **Time Complexity:** O(n²) where n is the matrix dimension
- **Space Complexity:** O(1) - no additional data structures
- **Early Exit:** Stops immediately after finding 2 sequences
- **Efficient Scanning:** Checks each direction only once, avoids overlapping sequences

## Testing

The project follows **Test-Driven Development (TDD)** with comprehensive test coverage:

```bash
./mvnw test
```

**Test Coverage:**
- ✅ DNA Validator Tests (11 tests)
- ✅ Mutant Detector Tests (16 tests)
- ✅ Application Context Test (1 test)

**Total: 28 tests, 100% passing**

### Test Categories

1. **Validation Tests**
   - Valid DNA sequences
   - Invalid characters
   - Non-square matrices
   - Null/empty arrays
   - Minimum size requirements

2. **Mutant Detection Tests**
   - Horizontal sequences
   - Vertical sequences
   - Diagonal sequences (both directions)
   - Multiple sequences
   - Edge cases (4x4, large matrices, all same character)

## Future Enhancements

As noted in the requirements, these features are planned for future iterations:

- [ ] Database persistence for verified DNAs
- [ ] `/stats/` endpoint for usage statistics
- [ ] Horizontal scaling for high traffic (100-1M requests/second)
- [ ] Frontend UI for DNA input
- [ ] Architecture diagram
- [ ] Code coverage reporting (target: >80%)

## Development

Built using **TDD** (Test-Driven Development):
1. Write failing tests first
2. Implement minimum code to pass tests
3. Refactor while keeping tests green

## License

This project is for demonstration purposes.

## Trade offs
Redis for caching and stats storage: Trade-off between speed and complexity. Fast reads/writes but adds infrastructure cost.
H2 vs PostgresSQL: Trade-off between simplicity and features. H2 is easy to set up, PostgresSQL is more robust.

Service communication:
Shared database vs Message queue events vs Stats-service calls Mutant-service API

## Open Questions
Would only “/stats/” receive high traffic, or both endpoints?
