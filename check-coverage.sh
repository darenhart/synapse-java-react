#!/bin/bash
echo "🧪 Running tests with coverage..."
echo ""

# Mutant Service
echo "================================"
echo "MUTANT SERVICE (dna-demo)"
echo "================================"
cd dna-demo
./mvnw clean test > /dev/null 2>&1

if [ -f target/site/jacoco/index.html ]; then
    coverage=$(grep -A 1 "Total" target/site/jacoco/index.html | grep "ctr2" | head -1 | sed -n 's/.*>\([0-9]*\)%.*/\1/p')
    echo "✅ Tests passed"
    echo "📊 Coverage: ${coverage}%"
    
    if [ "$coverage" -ge 80 ]; then
        echo "✓ Meets 80% target"
    else
        needed=$((80 - coverage))
        echo "✗ Need ${needed}% more to reach 80%"
    fi
    echo ""
    echo "📁 Full report: dna-demo/target/site/jacoco/index.html"
else
    echo "❌ Coverage report not found"
fi

cd ..
echo ""

# Stats Service  
echo "================================"
echo "STATS SERVICE"
echo "================================"
cd stats-service
./mvnw clean test > /dev/null 2>&1

if [ -f target/site/jacoco/index.html ]; then
    coverage=$(grep -A 1 "Total" target/site/jacoco/index.html | grep "ctr2" | head -1 | sed -n 's/.*>\([0-9]*\)%.*/\1/p')
    echo "✅ Tests passed"
    echo "📊 Coverage: ${coverage}%"
    
    if [ "$coverage" -ge 80 ]; then
        echo "✓ Meets 80% target"
    else
        needed=$((80 - coverage))
        echo "✗ Need ${needed}% more to reach 80%"
    fi
    echo ""
    echo "📁 Full report: stats-service/target/site/jacoco/index.html"
else
    echo "❌ Coverage report not found"
fi

cd ..
echo ""
echo "================================"
echo "To view detailed reports, open:"
echo "  open dna-demo/target/site/jacoco/index.html"
echo "  open stats-service/target/site/jacoco/index.html"
echo "================================"
