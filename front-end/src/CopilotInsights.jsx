import React from 'react';

export default function CopilotInsights({ inputs, expenses = [], categoryLimits = {} }) {
  const inc = Number(inputs.monthlyIncome) || 0;
  const exp = Number(inputs.monthlyExpenses) || 0;
  const sav = Number(inputs.currentSavings) || 0;
  const goal = Number(inputs.targetGoalAmount) || 0;
  const net = inc - exp;

  const insights = [];

  // Group and normalize expense categories (e.g., "Food" becomes "food")
  const totalsByCategory = expenses.reduce((acc, item) => {
    const cat = (item.category || 'Uncategorized').trim().toLowerCase();
    acc[cat] = (acc[cat] || 0) + (Number(item.amount) || 0);
    return acc;
  }, {});

  // Check each setting limit against total spent in that category
  Object.keys(categoryLimits).forEach(rawCat => {
    const cat = rawCat.trim().toLowerCase();
    const spent = totalsByCategory[cat] || 0;
    const limit = Number(categoryLimits[rawCat]) || 0;

    if (limit > 0 && spent > limit) {
      insights.push({
        type: 'danger',
        icon: '🚨',
        title: `Budget Exceeded: ${rawCat.toUpperCase()}`,
        message: `You've spent $${spent.toLocaleString()} on ${rawCat}, surpassing your monthly limit of $${limit.toLocaleString()}.`
      });
    }
  });

  if (net < 0) {
    insights.push({
      type: 'danger',
      icon: '⚠️',
      title: 'Cash Deficit Warning',
      message: `You are spending $${Math.abs(net).toLocaleString()} more than you earn each month.`
    });
  }

  if (net > 0 && goal > sav) {
    const monthsToGoal = Math.ceil((goal - sav) / net);
    insights.push({
      type: 'success',
      icon: '🚀',
      title: 'Goal On Track',
      message: `At your current saving pace, you will hit your $${goal.toLocaleString()} goal in approximately ${monthsToGoal} months!`
    });
  }

  const getBorderColor = (type) => {
    switch (type) {
      case 'danger': return '#ef4444';
      case 'warning': return '#f59e0b';
      case 'success': return '#22c55e';
      default: return '#3b82f6';
    }
  };

  return (
    <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.65)', backdropFilter: 'blur(12px)', border: '1px solid rgba(255,255,255,0.07)', padding: '20px', borderRadius: '12px', marginBottom: '24px' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '16px' }}>
        <h3 style={{ margin: 0, color: '#f8fafc' }}>🤖 Connected Copilot Insights</h3>
        <span style={{ fontSize: '0.75rem', backgroundColor: '#3b82f622', color: '#60a5fa', padding: '2px 8px', borderRadius: '10px' }}>
          Live Sync Active
        </span>
      </div>

      {insights.length === 0 ? (
        <p style={{ color: '#94a3b8', margin: 0, fontSize: '0.9rem' }}>All categories are within your set budget limits. Great financial discipline!</p>
      ) : (
        <div style={{ display: 'grid', gap: '12px' }}>
          {insights.map((item, idx) => (
            <div key={idx} style={{
              display: 'flex',
              gap: '12px',
              alignItems: 'flex-start',
              padding: '14px 16px',
              backgroundColor: '#0f172a',
              borderRadius: '8px',
              borderLeft: `4px solid ${getBorderColor(item.type)}`
            }}>
              <span style={{ fontSize: '1.4rem', lineHeight: '1' }}>{item.icon}</span>
              <div>
                <h4 style={{ margin: '0 0 4px 0', fontSize: '0.95rem', color: '#f8fafc' }}>{item.title}</h4>
                <p style={{ margin: 0, fontSize: '0.85rem', color: '#94a3b8', lineHeight: '1.4' }}>
                  {item.message}
                </p>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}