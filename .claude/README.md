# .claude/ Directory - Project Configuration

This directory contains Claude Code configuration to optimize context and provide quick shortcuts for common development tasks.

## Structure

```
.claude/
├── CLAUDE.md                    # Project context (auto-loaded)
├── README.md                    # This file
└── skills/                      # Custom skills/commands
    ├── test-coverage/           # Run tests + verify coverage
    │   └── skill.json
    └── verify-api/              # Smoke test both services
        └── skill.json
```

## What's Inside

### 📄 CLAUDE.md (Auto-loaded Context)
- **Purpose**: Automatically loaded into Claude's context every conversation
- **Contains**:
  - Architecture overview
  - Service details (ports, endpoints)
  - Project structure
  - Key patterns and conventions
  - Development workflow
- **Benefit**: Claude understands your project without re-reading files

### 🔧 Skills (Custom Commands)

#### `/test-coverage`
Runs tests for both services and verifies coverage meets 80% threshold.

**Usage:**
```
/test-coverage
```

**What it does:**
1. Runs `./mvnw test` in `dna-demo/`
2. Runs `./mvnw test` in `stats-service/`
3. Extracts coverage from JaCoCo reports
4. Verifies both services have ≥80% coverage
5. Shows summary table with results

**Example output:**
```
Service          | Tests | Status | Coverage | Meets 80%?
----------------|-------|--------|----------|------------
Mutant Service  | 39/39 | ✓      | 85.2%    | ✓
Stats Service   | 12/12 | ✓      | 82.1%    | ✓
```

---

#### `/verify-api`
Smoke tests both services with sample API requests.

**Usage:**
```
/verify-api
```

**What it does:**
1. Checks if services are running (ports 8080, 8081)
2. Tests mutant detection with mutant DNA (expects 200)
3. Tests mutant detection with human DNA (expects 403)
4. Tests validation with invalid DNA (expects 400)
5. Tests stats endpoint (expects valid JSON)
6. Shows results table

**Example output:**
```
Endpoint                    | Expected | Actual | Status
----------------------------|----------|--------|--------
POST /mutant/ (mutant)      | 200      | 200    | ✓
POST /mutant/ (human)       | 403      | 403    | ✓
POST /mutant/ (invalid)     | 400      | 400    | ✓
GET /stats/                 | 200      | 200    | ✓
```

## How to Use Skills

In any Claude Code conversation:

```bash
# Run test coverage check
/test-coverage

# Verify APIs are working
/verify-api
```

Skills are invoked using the `/` prefix followed by the skill name.

## Benefits

✅ **Context Optimization**: CLAUDE.md reduces repeated file reads
✅ **Faster Responses**: Claude understands your architecture immediately
✅ **Consistent Testing**: Skills ensure you follow the same verification steps
✅ **Token Efficiency**: Less context loading = more tokens for actual work
✅ **Quality Assurance**: Built-in 80% coverage requirement

## Future Additions (As Project Grows)

Consider adding when needed:
- `/integration-test` - Run integration tests across services
- `/load-test` - Validate 100-1M req/s capacity
- `/deploy` - Deployment automation
- Sub-agents for complex multi-step tasks

## Maintenance

- Update `CLAUDE.md` when architecture changes
- Add new skills for repetitive workflows
- Keep skills focused on single responsibilities

---

**Note**: Skills are only available when using Claude Code. They won't work in the web interface.
