# Quick Start Guide

## 🚀 One Command to Start Everything

```bash
npm start
```

This starts all three services in one terminal:
- **Mutant Service** (Port 8080) - GREEN logs
- **Stats Service** (Port 8081) - BLUE logs  
- **Frontend** (Port 5173) - MAGENTA logs

Press `Ctrl+C` to stop all services.

---

## 📋 Available Commands

```bash
# Start all services (foreground with color-coded logs)
npm start

# Run all tests
npm test

# Start individual services
npm run start:mutant     # Mutant service only
npm run start:stats      # Stats service only
npm run start:frontend   # Frontend only
```

---

## 🌐 Access Points

Once started, open your browser:

- **Frontend UI**: http://localhost:5173
- **Mutant API**: http://localhost:8080
- **Stats API**: http://localhost:8081

---

## 🔧 First Time Setup

```bash
# 1. Install root dependencies
npm install

# 2. Install frontend dependencies
cd dna-frontend && npm install && cd ..

# 3. Start Redis (optional but recommended)
docker run -d -p 6379:6379 redis:latest

# 4. Start everything
npm start
```

---

## 📝 Log Colors

- **GREEN** = Mutant Service logs
- **BLUE** = Stats Service logs
- **MAGENTA** = Frontend logs

All logs appear in the same terminal for easy monitoring.

---

## ⚠️ Troubleshooting

**Ports already in use?**
```bash
# Find what's using the ports
lsof -i :8080  # Mutant service
lsof -i :8081  # Stats service
lsof -i :5173  # Frontend

# Kill processes if needed
kill -9 <PID>
```

**Services won't start?**
- Ensure Java 17+ is installed
- Ensure Node.js 18+ is installed
- Check Redis is running (optional)
- Frontend needs `npm install` in `dna-frontend/` first

---

## 🎯 Testing the System

1. Open http://localhost:5173
2. Click "Generate Random" 
3. Click "Check DNA"
4. See result and stats update

Or use curl:
```bash
curl -X POST http://localhost:8080/mutant/ \
  -H "Content-Type: application/json" \
  -d '{"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]}'
```

---

**Happy coding! 🧬**
