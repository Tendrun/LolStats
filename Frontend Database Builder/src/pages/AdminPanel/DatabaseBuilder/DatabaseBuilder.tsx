import { ChangeEvent, FormEvent, useMemo, useState } from 'react'
import './DatabaseBuilder.css'

export default function DatabaseBuilder() {
  const regionOptions = useMemo(
    () => [
      'BR1',
      'EUN1',
      'EUW1',
      'JP1',
      'KR',
      'LA1',
      'LA2',
      'ME1',
      'NA1',
      'OC1',
      'RU',
      'SG2',
      'TR1',
      'TW2',
      'VN2',
    ],
    []
  )

  const tierOptions = useMemo(
    () => [
      'IRON',
      'BRONZE',
      'SILVER',
      'GOLD',
      'PLATINUM',
      'EMERALD',
      'DIAMOND',
      'MASTER',
      'GRANDMASTER',
      'CHALLENGER',
    ],
    []
  )

  const divisionOptions = useMemo(() => ['I', 'II', 'III', 'IV'], [])

  const queueOptions = useMemo(
    () => ['RANKED_SOLO_5x5', 'RANKED_FLEX_SR', 'RANKED_FLEX_TT'],
    []
  )

  type FetchStatus = 'idle' | 'loading' | 'success' | 'error'

  type StepResult = {
    requestStatus: string
    stepName: string
    message: string
  }

  const [status, setStatus] = useState<FetchStatus>('idle')
  const [formValues, setFormValues] = useState({
    region: 'EUN1',
    tier: 'PLATINUM',
    division: 'II',
    queue: 'RANKED_SOLO_5x5',
    page: '1',
  })
  const [result, setResult] = useState<Record<string, StepResult[]> | null>(null)
  const [error, setError] = useState<string | null>(null)

  function handleChange(event: ChangeEvent<HTMLSelectElement | HTMLInputElement>) {
    const target = event.target as HTMLSelectElement | HTMLInputElement
    setFormValues((prev) => ({
      ...prev,
      [target.name]: target.value,
    }))
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setStatus('loading')
    setError(null)
    setResult(null)

    try {
      const response = await fetch(
        'http://localhost:4020/api/database/FetchPlayers',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(formValues),
        }
      )

      if (!response.ok) {
        const fallbackMessage = `Request failed with status ${response.status}`
        const text = await response.text()
        throw new Error(text || fallbackMessage)
      }

      const data = (await response.json()) as Record<string, StepResult[]>
      setResult(data)
      setStatus('success')
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unknown error occurred')
      setStatus('error')
    }
  }

  return (
    <div className="db-builder-page">
      <h1>Database Builder</h1>
      <p>
        Configure a player fetch request and send it to the backend database
        builder service.
      </p>

      <div className="db-controls">
        <form className="db-form" onSubmit={handleSubmit}>
          <label>
            Region
            <select
              name="region"
              value={formValues.region}
              onChange={handleChange}
            >
              {regionOptions.map((region) => (
                <option key={region} value={region}>
                  {region}
                </option>
              ))}
            </select>
          </label>

          <label>
            Tier
            <select name="tier" value={formValues.tier} onChange={handleChange}>
              {tierOptions.map((tier) => (
                <option key={tier} value={tier}>
                  {tier}
                </option>
              ))}
            </select>
          </label>

          <label>
            Division
            <select
              name="division"
              value={formValues.division}
              onChange={handleChange}
            >
              {divisionOptions.map((division) => (
                <option key={division} value={division}>
                  {division}
                </option>
              ))}
            </select>
          </label>

          <label>
            Queue
            <select
              name="queue"
              value={formValues.queue}
              onChange={handleChange}
            >
              {queueOptions.map((queue) => (
                <option key={queue} value={queue}>
                  {queue}
                </option>
              ))}
            </select>
          </label>

          <label>
            Page
            <input
              type="number"
              min="1"
              name="page"
              value={formValues.page}
              onChange={handleChange}
            />
          </label>

          <button
            type="submit"
            className="btn-primary"
            disabled={status === 'loading'}
          >
            {status === 'loading' ? 'Sending...' : 'Send'}
          </button>
        </form>
        <div className="status">Status: {status}</div>
      </div>

      {error && (
        <div className="db-error" role="alert">
          {error}
        </div>
      )}

      {result && (
        <div className="db-result">
          <h2>Step Results</h2>
          {Object.entries(result).map(([stepName, stepResults]) => (
            <div key={stepName} className="db-result-step">
              <h3>{stepName}</h3>
              <ul>
                {stepResults.map((step, index) => (
                  <li key={`${step.stepName}-${index}`}>
                    <strong>{step.stepName}</strong>: {step.requestStatus} - {step.message}
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}