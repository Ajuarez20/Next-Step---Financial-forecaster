import React from 'react';

export default function SettingsView({ limits = {}, setLimits, userName = 'User' }) {
  const handleLimitChange = (category, value) => {
    // Allows clearing the field completely without forcing it to 0 immediately
    const newValue = value === '' ? '' : Number(value);
    setLimits({ ...limits, [category]: newValue });
  };

  const handleDownloadCSV = () => {
    window.open('http://localhost:8080/api/export-csv', '_blank');
  };

  return (
    <div style={{ maxWidth: '900px', margin: '0 auto' }}>
      <h2>App Settings & Preferences</h2>
      <p style={{ color: '#94a3b8', marginBottom: '24px' }}>Changes made here instantly update your Copilot alert engine.</p>

      <div style={{ 
        backgroundColor: 'rgba(30, 41, 59, 0.65)', 
        backdropFilter: 'blur(12px)', 
        border: '1px solid rgba(255, 255, 255, 0.07)', 
        borderRadius: '12px', 
        padding: '24px', 
        marginBottom: '20px' 
      }}>
        <h3 style={{ marginTop: 0, fontSize: '1.1rem', color: '#f8fafc' }}>👤 Account Profile</h3>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px', marginTop: '16px' }}>
          <div>
            <label style={{ display: 'block', color: '#94a3b8', fontSize: '0.8rem', marginBottom: '4px', textTransform: 'uppercase' }}>Username</label>
            <input 
              type="text" 
              readOnly 
              value={userName} 
              style={{ width: '100%', padding: '10px', backgroundColor: '#0f172a', border: '1px solid #334155', borderRadius: '6px', color: '#94a3b8', boxSizing: 'border-box' }} 
            />
          </div>
          <div>
            <label style={{ display: 'block', color: '#94a3b8', fontSize: '0.8rem', marginBottom: '4px', textTransform: 'uppercase' }}>Account Tier</label>
            <input 
              type="text" 
              readOnly 
              value="Pro Member (Active)" 
              style={{ width: '100%', padding: '10px', backgroundColor: '#0f172a', border: '1px solid #334155', borderRadius: '6px', color: '#22c55e', fontWeight: 'bold', boxSizing: 'border-box' }} 
            />
          </div>
        </div>
      </div>

      <div style={{ 
        backgroundColor: 'rgba(30, 41, 59, 0.65)', 
        backdropFilter: 'blur(12px)', 
        border: '1px solid rgba(255, 255, 255, 0.07)', 
        borderRadius: '12px', 
        padding: '24px', 
        marginBottom: '20px' 
      }}>
        <h3 style={{ marginTop: 0, fontSize: '1.1rem', color: '#f8fafc' }}>🎯 Monthly Category Budget Caps</h3>
        <p style={{ color: '#94a3b8', fontSize: '0.85rem', marginBottom: '16px' }}>If your logged expenses exceed these caps, Copilot will trigger a live warning.</p>
        
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
          {Object.keys(limits).map((cat) => (
            <div key={cat}>
              <label style={{ display: 'block', color: '#94a3b8', fontSize: '0.85rem', marginBottom: '6px', textTransform: 'capitalize' }}>
                {cat} Limit ($)
              </label>
              <input 
                type="number" 
                value={limits[cat]} 
                onChange={(e) => handleLimitChange(cat, e.target.value)}
                style={{ width: '100%', padding: '10px', backgroundColor: '#0f172a', border: '1px solid #334155', borderRadius: '6px', color: '#fff', boxSizing: 'border-box' }} 
              />
            </div>
          ))}
        </div>
      </div>

      <div style={{ 
        backgroundColor: 'rgba(30, 41, 59, 0.65)', 
        backdropFilter: 'blur(12px)', 
        border: '1px solid rgba(255, 255, 255, 0.07)', 
        borderRadius: '12px', 
        padding: '24px' 
      }}>
        <h3 style={{ marginTop: 0, fontSize: '1.1rem', color: '#f8fafc' }}>💾 Data Backup & Export</h3>
        <p style={{ color: '#94a3b8', fontSize: '0.85rem', marginBottom: '16px' }}>Download a complete record of all your logged transaction history in `.csv` format.</p>
        <button 
          onClick={handleDownloadCSV}
          style={{
            padding: '10px 18px',
            backgroundColor: '#3b82f6',
            color: '#fff',
            border: 'none',
            borderRadius: '6px',
            fontWeight: 'bold',
            cursor: 'pointer'
          }}
        >
          📥 Download Expense Statement (.csv)
        </button>
      </div>
    </div>
  );
}