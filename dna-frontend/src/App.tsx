import { useState } from 'react'

function App() {
  const [dna, setDna] = useState('')
  const [result, setResult] = useState<'mutant' | 'human' | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  const generateRandomDNA = () => {
    const bases = ['A', 'T', 'C', 'G']
    const rows = 6
    const cols = 6
    const dnaArray: string[] = []

    for (let i = 0; i < rows; i++) {
      let row = ''
      for (let j = 0; j < cols; j++) {
        row += bases[Math.floor(Math.random() * bases.length)]
      }
      dnaArray.push(row)
    }

    setDna(dnaArray.join('\n'))
  }

  const checkDNA = async () => {
    setError('')
    setResult(null)
    setLoading(true)

    try {
      const dnaArray = dna.trim().split('\n').filter(line => line.trim())

      if (dnaArray.length !== 6) {
        setError('DNA must have exactly 6 sequences')
        setLoading(false)
        return
      }

      const response = await fetch('/mutant/', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ dna: dnaArray }),
      })

      if (response.status === 200) {
        setResult('mutant')
      } else if (response.status === 403) {
        setResult('human')
      } else {
        setError('Invalid DNA sequence')
      }
    } catch (err) {
      setError('Connection error')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8 px-4">
      <div className="max-w-2xl mx-auto">
        <h1 className="text-3xl font-bold text-gray-900 mb-8 text-center">
          🧬 DNA Mutant Detector
        </h1>

        <div className="bg-white rounded-lg shadow-md p-6 mb-6">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            DNA Sequence (6 lines, 6 letters each)
          </label>

          <textarea
            value={dna}
            onChange={(e) => setDna(e.target.value)}
            placeholder="ATGCGA&#10;CAGTGC&#10;TTATGT&#10;AGAAGG&#10;CCCCTA&#10;TCACTG"
            rows={6}
            className="w-full px-3 py-2 border border-gray-300 rounded-md font-mono text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          />

          <div className="flex gap-3 mt-4">
            <button
              onClick={checkDNA}
              disabled={loading}
              className="flex-1 bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed transition"
            >
              {loading ? 'Checking...' : 'Check DNA'}
            </button>

            <button
              onClick={generateRandomDNA}
              className="bg-gray-200 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-300 transition"
            >
              Generate Random
            </button>
          </div>

          {error && (
            <div className="mt-4 p-3 bg-red-50 border border-red-200 text-red-700 rounded-md text-sm">
              {error}
            </div>
          )}

          {result && (
            <div className={`mt-4 p-4 rounded-md text-center text-lg font-semibold ${result === 'mutant'
              ? 'bg-green-50 border border-green-200 text-green-700'
              : 'bg-yellow-50 border border-yellow-200 text-yellow-700'
              }`}>
              {result === 'mutant' ? '✓ Mutant Detected' : '✗ Human'}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default App
