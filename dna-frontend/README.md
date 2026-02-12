# DNA Mutant Detector - Frontend

**[← Back to Main Documentation](../README.md)**

React web interface for testing the DNA Mutant Detector system.

---

## Overview

Minimal single-page application for:
- Inputting DNA sequences (6x6 grid)
- Generating random test data
- Verifying mutant/human classification
- Viewing real-time statistics

---

## Running the Frontend

### With All Services
```bash
# From repository root
npm start
```

### Individually
```bash
cd dna-frontend
npm run dev
```

Open **http://localhost:5173**

---

## Features

✅ **DNA Input** - Textarea with 6 lines (6 letters each: A, T, C, G)  
✅ **Generate Random** - Quick test data generation  
✅ **Check DNA** - Submit to backend API  
✅ **Result Display** - Green for mutant, yellow for human  
✅ **Live Stats** - Real-time counts and ratio  
✅ **Error Handling** - Basic validation and error messages  

---

## Tech Stack

- **React 18** with TypeScript
- **Vite 7** (fast dev server + HMR)
- **Tailwind CSS 3** (utility-first styling)
- **Fetch API** (HTTP requests via Vite proxy)

---

## Development

### Install Dependencies
```bash
npm install
```

### Start Dev Server
```bash
npm run dev
```

### Build for Production
```bash
npm run build
npm run preview  # Preview production build
```

---

## API Integration

Frontend uses **Vite proxy** to avoid CORS:

```typescript
// Relative URLs go through proxy
fetch('/mutant/')  → http://localhost:8080/mutant/
fetch('/stats/')   → http://localhost:8081/stats/
```

**Proxy Config:** `vite.config.ts`

---

## Project Structure

```
dna-frontend/
├── src/
│   ├── App.tsx          # Main component (all logic)
│   ├── main.tsx         # Entry point
│   └── index.css        # Tailwind directives
├── vite.config.ts       # Vite + proxy config
├── tailwind.config.js   # Tailwind setup
├── tsconfig.json        # TypeScript config
└── package.json
```

---

**[← Back to Main Documentation](../README.md)** | **[Architecture](../ARCHITECTURE.md)**
