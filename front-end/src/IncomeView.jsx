import React, { useState } from 'react';
import axios from 'axios';

export default function IncomeView({ onIncomeChange, incomes = [], financialProfileId = 1 }) {
  const [amount, setAmount] = useState('');
  const [category, setCategory] = useState('Salary');
  const [description, setDescription] = useState('');

  const totalIncome = incomes.reduce((sum, item) => sum + (Number(item.amount) || 0), 0);

  const handleAddIncome = async (e) => {
    e.preventDefault();
    if (!amount) return;

    try {
      // Corrected to include financialProfileId path variable matching backend controller
      await axios.post(`http://localhost:8080/api/income/add/${financialProfileId}`, {
        amount: parseFloat(amount),
        category: category,
        description: description || 'Monthly Paycheck'
      });
      setAmount('');
      setDescription('');
      if (onIncomeChange) onIncomeChange();
    } catch (error) {
      console.error('Failed to add income:', error);
    }
  };

  const handleDeleteIncome = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/income/${id}`);
      if (onIncomeChange) onIncomeChange();
    } catch (error) {
      console.error('Failed to delete income:', error);
    }
  };

  return (
    <div style={{ padding: '24px', maxWidth: '1100px', margin: '0 auto' }}>
      <h2>Income Tracker</h2>
      <p style={{ color: '#94a3b8' }}>Log and manage your revenue sources synced with your backend.</p>

      <div style={{ backgroundColor: '#1e293b', padding: '20px', borderRadius: '12px', margin: '20px 0', border: '1px solid rgba(255,255,255,0.07)' }}>
        <h3 style={{ margin: '0 0 12px 0', fontSize: '1rem', color: '#94a3b8' }}>TOTAL LOGGED INCOME</h3>
        <div style={{ fontSize: '2rem', fontWeight: 'bold', color: '#22c55e' }}>
          ${totalIncome.toLocaleString()}
        </div>
      </div>

      <div style={{ backgroundColor: '#1e293b', padding: '20px', borderRadius: '12px', marginBottom: '24px', border: '1px solid rgba(255,255,255,0.07)' }}>
        <h3 style={{ marginTop: 0, fontSize: '1.1rem' }}>Add New Income Source</h3>
        <form onSubmit={handleAddIncome} style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 2fr auto', gap: '12px', alignItems: 'end' }}>
          <div>
            <label style={{ display: 'block', fontSize: '0.8rem', color: '#94a3b8', marginBottom: '6px' }}>Amount ($)</label>
            <input 
              type="number" 
              step="0.01" 
              placeholder="0.00" 
              value={amount} 
              onChange={(e) => setAmount(e.target.value)}
              style={{ width: '100%', padding: '10px', backgroundColor: '#0f172a', border: '1px solid #334155', borderRadius: '8px', color: '#fff', boxSizing: 'border-box' }}
              required 
            />
          </div>
          <div>
            <label style={{ display: 'block', fontSize: '0.8rem', color: '#94a3b8', marginBottom: '6px' }}>Category</label>
            <select 
              value={category} 
              onChange={(e) => setCategory(e.target.value)}
              style={{ width: '100%', padding: '10px', backgroundColor: '#0f172a', border: '1px solid #334155', borderRadius: '8px', color: '#fff', boxSizing: 'border-box' }}
            >
              <option value="Salary">Salary</option>
              <option value="Investments">Investments</option>
              <option value="Gifts">Gifts</option>
              <option value="Freelance">Freelance</option>
              <option value="Other">Other</option>
            </select>
          </div>
          <div>
            <label style={{ display: 'block', fontSize: '0.8rem', color: '#94a3b8', marginBottom: '6px' }}>Description</label>
            <input 
              type="text" 
              placeholder="e.g. Monthly Paycheck" 
              value={description} 
              onChange={(e) => setDescription(e.target.value)}
              style={{ width: '100%', padding: '10px', backgroundColor: '#0f172a', border: '1px solid #334155', borderRadius: '8px', color: '#fff', boxSizing: 'border-box' }}
            />
          </div>
          <button type="submit" style={{ padding: '10px 20px', backgroundColor: '#22c55e', color: '#fff', border: 'none', borderRadius: '8px', fontWeight: '600', cursor: 'pointer', height: '41px' }}>
            Add Income
          </button>
        </form>
      </div>

      <div style={{ backgroundColor: '#1e293b', padding: '20px', borderRadius: '12px', border: '1px solid rgba(255,255,255,0.07)' }}>
        <h3 style={{ marginTop: 0, fontSize: '1.1rem' }}>Revenue History</h3>
        {incomes.length === 0 ? (
          <p style={{ color: '#94a3b8' }}>No income sources logged yet.</p>
        ) : (
          <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '10px' }}>
            <thead>
              <tr style={{ borderBottom: '1px solid #334155', textAlign: 'left', color: '#94a3b8', fontSize: '0.85rem' }}>
                <th style={{ padding: '10px' }}>Category</th>
                <th style={{ padding: '10px' }}>Description</th>
                <th style={{ padding: '10px' }}>Amount</th>
                <th style={{ padding: '10px' }}>Action</th>
              </tr>
            </thead>
            <tbody>
              {incomes.map((item, idx) => (
                <tr key={item.id || idx} style={{ borderBottom: '1px solid rgba(51, 65, 85, 0.4)' }}>
                  <td style={{ padding: '12px 10px', color: '#f8fafc', fontWeight: '500' }}>{item.category}</td>
                  <td style={{ padding: '12px 10px', color: '#94a3b8' }}>{item.description || '-'}</td>
                  <td style={{ padding: '12px 10px', color: '#22c55e', fontWeight: 'bold' }}>${Number(item.amount).toLocaleString()}</td>
                  <td style={{ padding: '12px 10px' }}>
                    <button 
                      onClick={() => handleDeleteIncome(item.id)}
                      style={{ padding: '6px 12px', backgroundColor: '#ef444422', color: '#ef4444', border: '1px solid #ef444455', borderRadius: '6px', cursor: 'pointer', fontSize: '0.8rem' }}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}