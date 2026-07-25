import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function IncomeView({ onIncomeChange, incomes = [] }) {
  const [errorMsg, setErrorMsg] = useState('');
  const [form, setForm] = useState({
    amount: '',
    category: '',
    description: '',
    date: new Date().toISOString().split('T')[0]
  });

  const fetchIncomes = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/income/all');
      const items = response.data || [];
      const total = items.reduce((sum, item) => sum + (Number(item.amount) || 0), 0);
      
      if (onIncomeChange) {
        onIncomeChange(total, items);
      }
    } catch (error) {
      console.error('Failed to fetch incomes:', error);
      setErrorMsg('Could not connect to backend server.');
    }
  };

  useEffect(() => {
    fetchIncomes();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMsg('');

    if (!form.amount || !form.category) {
      setErrorMsg('Please select a Category and enter an Amount.');
      return;
    }

    try {
      await axios.post('http://localhost:8080/api/income/add/1', {
        ...form,
        amount: Number(form.amount)
      });
      
      setForm({ 
        amount: '', 
        category: '', 
        description: '', 
        date: new Date().toISOString().split('T')[0] 
      });

      fetchIncomes();
    } catch (error) {
      console.error('Failed to add income:', error);
      setErrorMsg('Failed to save income to the backend.');
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/income/${id}`);
      fetchIncomes();
    } catch (error) {
      console.error('Failed to delete income:', error);
      setErrorMsg('Failed to delete income item.');
    }
  };

  const totalSum = incomes.reduce((sum, item) => sum + (Number(item.amount) || 0), 0);

  const fieldStyle = {
    width: '100%',
    height: '42px',
    padding: '0 12px',
    backgroundColor: '#0f172a',
    border: '1px solid #334155',
    borderRadius: '6px',
    color: '#fff',
    boxSizing: 'border-box',
    outline: 'none',
    fontSize: '0.9rem'
  };

  const selectStyle = {
    ...fieldStyle,
    appearance: 'none',
    WebkitAppearance: 'none',
    MozAppearance: 'none',
    cursor: 'pointer',
    backgroundImage: `url("data:image/svg+xml;charset=UTF-8,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='%2394a3b8' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3e%3cpolyline points='6 9 12 15 18 9'%3e%3c/polyline%3e%3c/svg%3e")`,
    backgroundRepeat: 'no-repeat',
    backgroundPosition: 'right 12px center',
    backgroundSize: '16px',
    paddingRight: '36px'
  };

  return (
    <div style={{ maxWidth: '1000px', margin: '0 auto', color: '#f8fafc' }}>
      <h2>Income Tracker</h2>
      <p style={{ color: '#94a3b8', marginBottom: '24px' }}>Log and manage your monthly revenue sources.</p>

      {errorMsg && (
        <div style={{ backgroundColor: '#7f1d1d', border: '1px solid #ef4444', color: '#fca5a5', padding: '12px 16px', borderRadius: '8px', marginBottom: '20px', fontSize: '0.9rem' }}>
          ⚠️ {errorMsg}
        </div>
      )}

      {/* Total Card */}
      <div style={{ 
        backgroundColor: 'rgba(30, 41, 59, 0.65)', 
        backdropFilter: 'blur(12px)', 
        border: '1px solid rgba(255,255,255,0.07)', 
        borderRadius: '12px', 
        padding: '20px 24px', 
        marginBottom: '24px',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        borderLeft: '4px solid #10b981'
      }}>
        <h3 style={{ margin: 0, fontSize: '1.1rem', color: '#94a3b8' }}>Total Logged Income:</h3>
        <div style={{ fontSize: '2rem', fontWeight: 'bold', color: '#10b981' }}>
          ${totalSum.toLocaleString()}
        </div>
      </div>

      {/* Add Income Form */}
      <form onSubmit={handleSubmit} style={{ 
        backgroundColor: 'rgba(30, 41, 59, 0.65)', 
        backdropFilter: 'blur(12px)', 
        border: '1px solid rgba(255,255,255,0.07)', 
        borderRadius: '12px', 
        padding: '20px', 
        marginBottom: '24px',
        display: 'grid',
        gridTemplateColumns: '1fr 1fr 1fr auto',
        gap: '12px',
        alignItems: 'end'
      }}>
        <div>
          <label style={{ display: 'block', color: '#94a3b8', fontSize: '0.8rem', marginBottom: '6px' }}>Amount ($)</label>
          <input 
            type="number" 
            placeholder="0.00"
            value={form.amount}
            onChange={(e) => setForm({ ...form, amount: e.target.value })}
            style={fieldStyle}
          />
        </div>

        <div>
          <label style={{ display: 'block', color: '#94a3b8', fontSize: '0.8rem', marginBottom: '6px' }}>Category</label>
          <select 
            value={form.category}
            onChange={(e) => setForm({ ...form, category: e.target.value })}
            style={selectStyle}
          >
            <option value="" disabled style={{ backgroundColor: '#0f172a', color: '#94a3b8' }}>Select category...</option>
            <option value="salary" style={{ backgroundColor: '#0f172a', color: '#fff' }}>Salary</option>
            <option value="freelance" style={{ backgroundColor: '#0f172a', color: '#fff' }}>Freelance</option>
            <option value="investments" style={{ backgroundColor: '#0f172a', color: '#fff' }}>Investments</option>
            <option value="gifts" style={{ backgroundColor: '#0f172a', color: '#fff' }}>Gifts</option>
            <option value="other" style={{ backgroundColor: '#0f172a', color: '#fff' }}>Other</option>
          </select>
        </div>

        <div>
          <label style={{ display: 'block', color: '#94a3b8', fontSize: '0.8rem', marginBottom: '6px' }}>Description</label>
          <input 
            type="text" 
            placeholder="Monthly Paycheck"
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
            style={fieldStyle}
          />
        </div>

        <button 
          type="submit"
          style={{ padding: '0 20px', backgroundColor: '#10b981', color: '#fff', border: 'none', borderRadius: '6px', fontWeight: 'bold', cursor: 'pointer', height: '42px', display: 'flex', alignItems: 'center', justifyContent: 'center' }}
        >
          Add Income
        </button>
      </form>

      {/* Income Table */}
      <div style={{ backgroundColor: 'rgba(30, 41, 59, 0.65)', backdropFilter: 'blur(12px)', border: '1px solid rgba(255,255,255,0.07)', borderRadius: '12px', overflow: 'hidden' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
          <thead>
            <tr style={{ borderBottom: '1px solid #334155', color: '#94a3b8', fontSize: '0.85rem' }}>
              <th style={{ padding: '14px 20px' }}>Category</th>
              <th style={{ padding: '14px 20px' }}>Description</th>
              <th style={{ padding: '14px 20px' }}>Amount</th>
              <th style={{ padding: '14px 20px' }}>Date</th>
              <th style={{ padding: '14px 20px', textAlign: 'right' }}>Action</th>
            </tr>
          </thead>
          <tbody>
            {incomes.length === 0 ? (
              <tr>
                <td colSpan="5" style={{ padding: '24px', textAlign: 'center', color: '#94a3b8' }}>No income logged yet.</td>
              </tr>
            ) : (
              incomes.map((item) => (
                <tr key={item.id} style={{ borderBottom: '1px solid rgba(255,255,255,0.04)' }}>
                  <td style={{ padding: '14px 20px', fontWeight: '500', textTransform: 'capitalize' }}>{item.category}</td>
                  <td style={{ padding: '14px 20px', color: '#94a3b8' }}>{item.description || '-'}</td>
                  <td style={{ padding: '14px 20px', fontWeight: 'bold', color: '#10b981' }}>${Number(item.amount).toLocaleString()}</td>
                  <td style={{ padding: '14px 20px', color: '#94a3b8' }}>{item.date}</td>
                  <td style={{ padding: '14px 20px', textAlign: 'right' }}>
                    <button 
                      onClick={() => handleDelete(item.id)}
                      style={{ padding: '6px 12px', backgroundColor: '#ef4444', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer', fontSize: '0.8rem' }}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}