import React, { useState } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend } from 'recharts';

export default function ForecastsView({ monthlyIncome = 0, monthlyExpenses = 0, currentSavings = 0, targetGoalAmount = 0 }) {
  // New granular controls for wealth strategy
  const [savingsRatePercent, setSavingsRatePercent] = useState(80); // % of net income to save
  const [aggressiveSharePercent, setAggressiveSharePercent] = useState(60); // % of savings going to aggressive
  const [projectionMonths, setProjectionMonths] = useState(24);

  const inc = Number(monthlyIncome) || 0;
  const exp = Number(monthlyExpenses) || 0;
  const sav = Number(currentSavings) || 0;
  const goal = Number(targetGoalAmount) || 0;

  // Math calculations
  const netFlow = inc - exp;
  const monthlySavedTotal = Math.max(0, netFlow * (savingsRatePercent / 100));
  
  const aggressiveMonthly = monthlySavedTotal * (aggressiveSharePercent / 100);
  const passiveMonthly = monthlySavedTotal * ((100 - aggressiveSharePercent) / 100);

  // Generate chart data for Aggressive vs Passive vs Baseline
  const generateStrategyData = () => {
    const data = [];
    let totalBalance = sav;

    for (let month = 0; month <= projectionMonths; month++) {
      data.push({
        monthLabel: `Mo ${month}`,
        'Total Portfolio': Math.round(totalBalance),
        Goal: goal
      });
      totalBalance += monthlySavedTotal;
    }
    return data;
  };

  const chartData = generateStrategyData();

  return (
    <div style={{ padding: '24px', maxWidth: '1100px', margin: '0 auto' }}>
      <h2>Advanced Wealth Strategy & Allocation</h2>
      <p style={{ color: '#94a3b8' }}>Customize how your net surplus is split between aggressive growth and passive preservation.</p>

      {/* Breakdown Metrics Grid */}
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))',
        gap: '16px',
        margin: '20px 0'
      }}>
        <div style={{ backgroundColor: '#1e293b', padding: '18px', borderRadius: '12px', border: '1px solid rgba(255,255,255,0.07)' }}>
          <h4 style={{ margin: '0 0 6px 0', color: '#94a3b8', fontSize: '0.8rem' }}>NET MONTHLY SURPLUS</h4>
          <div style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#f8fafc' }}>
            ${netFlow.toLocaleString()} / mo
          </div>
        </div>

        <div style={{ backgroundColor: '#1e293b', padding: '18px', borderRadius: '12px', borderLeft: '4px solid #3b82f6' }}>
          <h4 style={{ margin: '0 0 6px 0', color: '#94a3b8', fontSize: '0.8rem' }}>🔥 AGGRESSIVE BUCKET ({aggressiveSharePercent}%)</h4>
          <div style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#60a5fa' }}>
            ${Math.round(aggressiveMonthly).toLocaleString()} / mo
          </div>
          <span style={{ fontSize: '0.75rem', color: '#94a3b8' }}>Target: Stocks / Crypto / Index Funds</span>
        </div>

        <div style={{ backgroundColor: '#1e293b', padding: '18px', borderRadius: '12px', borderLeft: '4px solid #22c55e' }}>
          <h4 style={{ margin: '0 0 6px 0', color: '#94a3b8', fontSize: '0.8rem' }}>🛡️ PASSIVE BUCKET ({100 - aggressiveSharePercent}%)</h4>
          <div style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#22c55e' }}>
            ${Math.round(passiveMonthly).toLocaleString()} / mo
          </div>
          <span style={{ fontSize: '0.75rem', color: '#94a3b8' }}>Target: HYSA / Emergency Cash</span>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '320px 1fr', gap: '24px', marginTop: '20px' }}>
        {/* Strategy Control Panel */}
        <div style={{ backgroundColor: '#1e293b', padding: '20px', borderRadius: '12px', border: '1px solid rgba(255,255,255,0.07)' }}>
          <h3 style={{ marginTop: 0, fontSize: '1rem' }}>Allocation Sliders</h3>
          
          <div style={{ marginBottom: '20px' }}>
            <label style={{ color: '#94a3b8', display: 'block', marginBottom: '8px', fontSize: '0.85rem' }}>
              Net Savings Rate: <strong>{savingsRatePercent}%</strong>
            </label>
            <input 
              type="range" 
              min="10" 
              max="100" 
              step="5"
              value={savingsRatePercent} 
              onChange={(e) => setSavingsRatePercent(Number(e.target.value))}
              style={{ width: '100%', accentColor: '#3b82f6' }}
            />
            <span style={{ fontSize: '0.75rem', color: '#64748b' }}>Percentage of net cash flow sent to savings.</span>
          </div>

          <div style={{ marginBottom: '20px' }}>
            <label style={{ color: '#94a3b8', display: 'block', marginBottom: '8px', fontSize: '0.85rem' }}>
              Aggressive Share: <strong>{aggressiveSharePercent}%</strong>
            </label>
            <input 
              type="range" 
              min="0" 
              max="100" 
              step="5"
              value={aggressiveSharePercent} 
              onChange={(e) => setAggressiveSharePercent(Number(e.target.value))}
              style={{ width: '100%', accentColor: '#3b82f6' }}
            />
            <span style={{ fontSize: '0.75rem', color: '#64748b' }}>Portion of savings allocated to high-growth assets.</span>
          </div>

          <div style={{ marginBottom: '10px' }}>
            <label style={{ color: '#94a3b8', display: 'block', marginBottom: '8px', fontSize: '0.85rem' }}>
              Timeline: <strong>{projectionMonths} Months</strong>
            </label>
            <input 
              type="range" 
              min="6" 
              max="60" 
              step="6"
              value={projectionMonths} 
              onChange={(e) => setProjectionMonths(Number(e.target.value))}
              style={{ width: '100%', accentColor: '#3b82f6' }}
            />
          </div>
        </div>

        {/* Chart View */}
        <div style={{ backgroundColor: '#1e293b', padding: '20px', borderRadius: '12px', border: '1px solid rgba(255,255,255,0.07)' }}>
          <h3 style={{ marginTop: 0, fontSize: '1rem' }}>Multi-Category Growth Trajectory</h3>
          <div style={{ width: '100%', height: '320px', marginTop: '10px' }}>
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={chartData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
                <XAxis dataKey="monthLabel" stroke="#94a3b8" />
                <YAxis stroke="#94a3b8" />
                <Tooltip 
                  formatter={(val) => `$${Number(val).toLocaleString()}`}
                  contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', borderRadius: '8px', color: '#f8fafc' }}
                />
                <Legend />
                <Line type="monotone" dataKey="Total Portfolio" stroke="#3b82f6" strokeWidth={3} />
                <Line type="dash" dataKey="Goal" stroke="#ef4444" strokeDasharray="5 5" />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>

      {/* Smart Suggestions Box */}
      <div style={{ marginTop: '24px', backgroundColor: '#1e293b', padding: '20px', borderRadius: '12px', borderLeft: '4px solid #3b82f6', border: '1px solid rgba(255,255,255,0.07)' }}>
        <h4 style={{ margin: '0 0 8px 0', color: '#f8fafc', display: 'flex', alignItems: 'center', gap: '8px' }}>
          💡 Strategy Suggestions & AI-Style Breakdown
        </h4>
        <p style={{ margin: 0, color: '#94a3b8', fontSize: '0.9rem', lineHeight: '1.5' }}>
          By saving <strong>{savingsRatePercent}%</strong> of your net monthly surplus, you are channeling <strong>${Math.round(aggressiveMonthly).toLocaleString()}/mo</strong> into aggressive growth channels (like index funds) while keeping <strong>${Math.round(passiveMonthly).toLocaleString()}/mo</strong> safe in passive liquidity reserves. This balanced approach protects your downside while compounding long-term capital.
        </p>
      </div>
    </div>
  );
}