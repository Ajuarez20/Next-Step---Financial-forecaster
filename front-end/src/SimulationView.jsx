import React, { useState } from 'react';
import axios from 'axios';

export default function SimulationView({ financialProfileId = 1 }) {
  const [scenarioName, setScenarioName] = useState('Buy a Car');
  const [cost, setCost] = useState(15000);
  const [monthlyImpact, setMonthlyImpact] = useState(300);
  const [termMonths, setTermMonths] = useState(12);
  
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleRunSimulation = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const response = await axios.post(`http://localhost:8080/api/simulation/${financialProfileId}`, {
        goalType: scenarioName,
        upfrontCost: Number(cost),
        monthlyPayment: Number(monthlyImpact),
        termMonths: Number(termMonths)
      });
      setResult(response.data);
    } catch (err) {
      console.error("Simulation failed", err);
      setError("Failed to connect to the simulation server.");
    } finally {
      setLoading(false);
    }
  };

  const getStatusStyle = (status) => {
    switch (status) {
      case 'AFFORDABLE': 
        return { color: '#4ade80', borderColor: 'rgba(74, 222, 128, 0.3)', bg: 'rgba(74, 222, 128, 0.1)' };
      case 'CAUTION': 
        return { color: '#facc15', borderColor: 'rgba(250, 204, 21, 0.3)', bg: 'rgba(250, 204, 21, 0.1)' };
      case 'NOT_RECOMMENDED': 
        return { color: '#f87171', borderColor: 'rgba(248, 113, 113, 0.3)', bg: 'rgba(248, 113, 113, 0.1)' };
      default: 
        return { color: '#60a5fa', borderColor: 'rgba(59, 130, 246, 0.3)', bg: 'rgba(59, 130, 246, 0.1)' };
    }
  };

  return (
    <div className="card" style={{ padding: '24px', backgroundColor: '#1e293b', borderRadius: '12px', border: '1px solid rgba(255,255,255,0.07)' }}>
      <h2 style={{ marginTop: 0, color: '#f8fafc' }}>🧪 Future Scenario Simulator</h2>
      <p style={{ color: '#94a3b8', marginBottom: '24px' }}>
        Test how major life events or large purchases will impact your long-term financial health and score.
      </p>

      <form onSubmit={handleRunSimulation}>
        <div style={{ display: 'grid', gap: '16px', maxWidth: '500px', marginBottom: '24px' }}>
          <div className="control-group">
            <label style={{ display: 'block', marginBottom: '8px', color: '#cbd5e1' }}>Scenario Name</label>
            <input 
              type="text" 
              value={scenarioName} 
              onChange={(e) => setScenarioName(e.target.value)}
              style={{ width: '100%', padding: '10px', borderRadius: '6px', backgroundColor: '#0f172a', border: '1px solid #334155', color: '#fff' }}
              required
            />
          </div>
          <div className="control-group">
            <label style={{ display: 'block', marginBottom: '8px', color: '#cbd5e1' }}>Upfront Cost ($)</label>
            <input 
              type="number" 
              value={cost} 
              onChange={(e) => setCost(e.target.value)}
              style={{ width: '100%', padding: '10px', borderRadius: '6px', backgroundColor: '#0f172a', border: '1px solid #334155', color: '#fff' }}
              required
            />
          </div>
          <div className="control-group">
            <label style={{ display: 'block', marginBottom: '8px', color: '#cbd5e1' }}>New Monthly Expense ($)</label>
            <input 
              type="number" 
              value={monthlyImpact} 
              onChange={(e) => setMonthlyImpact(e.target.value)}
              style={{ width: '100%', padding: '10px', borderRadius: '6px', backgroundColor: '#0f172a', border: '1px solid #334155', color: '#fff' }}
              required
            />
          </div>
          <div className="control-group">
            <label style={{ display: 'block', marginBottom: '8px', color: '#cbd5e1' }}>Timeline (Months)</label>
            <input 
              type="number" 
              value={termMonths} 
              onChange={(e) => setTermMonths(e.target.value)}
              style={{ width: '100%', padding: '10px', borderRadius: '6px', backgroundColor: '#0f172a', border: '1px solid #334155', color: '#fff' }}
              required
            />
          </div>
        </div>

        <button 
          type="submit" 
          disabled={loading}
          style={{ padding: '10px 20px', backgroundColor: '#3b82f6', color: '#fff', border: 'none', borderRadius: '6px', cursor: 'pointer', fontWeight: 'bold', marginBottom: '24px' }}
        >
          {loading ? 'Analyzing Scenario...' : 'Run Simulation'}
        </button>
      </form>

      {error && <p style={{ color: '#f87171', marginBottom: '16px' }}>{error}</p>}

      {result && (
        <div style={{ 
          padding: '20px', 
          backgroundColor: getStatusStyle(result.status).bg, 
          border: `1px solid ${getStatusStyle(result.status).borderColor}`, 
          borderRadius: '8px' 
        }}>
          <h3 style={{ margin: '0 0 12px 0', color: getStatusStyle(result.status).color }}>
            Verdict: {result.status}
          </h3>
          <p style={{ margin: '4px 0', color: '#f8fafc' }}>
            <strong>Recommendation:</strong> {result.recommendation}
          </p>
          <hr style={{ borderColor: '#334155', margin: '12px 0' }} />
          <p style={{ margin: '4px 0', color: '#cbd5e1' }}>
            Projected Savings: <strong>${result.projectedSavings?.toLocaleString()}</strong>
          </p>
          <p style={{ margin: '4px 0', color: '#cbd5e1' }}>
            Projected Remaining Income: <strong>${result.projectedRemainingIncome?.toLocaleString()}</strong>
          </p>
          <p style={{ margin: '4px 0', color: '#cbd5e1' }}>
            Current Financial Score: <strong>{result.currentFinancialScore} / 100</strong>
          </p>
          <p style={{ margin: '4px 0', color: '#cbd5e1' }}>
            Projected Financial Score: <strong>{result.projectedFinancialScore} / 100</strong>
          </p>
        </div>
      )}
    </div>
  );
}