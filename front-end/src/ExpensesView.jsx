import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend } from 'recharts';

export default function ExpensesView({ onExpenseChange, expenses = [], categoryLimits = {} }) {
  const [errorMsg, setErrorMsg] = useState('');
  const [form, setForm] = useState({
    amount: '',
    category: '',
    description: '',
    date: new Date().toISOString().split('T')[0]
  });

  const fetchExpenses = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/expenses/all');
      const items = response.data || [];
      const total = items.reduce((sum, item) => sum + (Number(item.amount) || 0), 0);
      
      if (onExpenseChange) {
        onExpenseChange(total, items);
      }
    } catch (error) {
      console.error('Failed to fetch expenses:', error);
      setErrorMsg('Could not connect to backend server.');
    }
  };

  useEffect(() => {
    fetchExpenses();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMsg('');

    if (!form.amount || !form.category) {
      setErrorMsg('Please select a Category and enter an Amount.');
      return;
    }

    try {
      await axios.post('http://localhost:8080/api/expenses/add/1', {
        ...form,
        amount: Number(form.amount)
      });
      
      setForm({ 
        amount: '', 
        category: '', 
        description: '', 
        date: new Date().toISOString().split('T')[0] 
      });

      fetchExpenses();
    } catch (error) {
      console.error('Failed to add expense:', error);
      setErrorMsg('Failed to save expense to the backend.');
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/expenses/${id}`);
      fetchExpenses();
    } catch (error) {
      console.error('Failed to delete expense:', error);
      setErrorMsg('Failed to delete expense item.');
    }
  };

  const totalSum = expenses.reduce((sum, item) => sum + (Number(item.amount) || 0), 0);

  // Fallback default limits if none are passed from the parent component
  const defaultLimits = {
    food: 500,
    rent: 1200,
    entertainment: 200,
    utilities: 150,
    transportation: 300,
    other: 100
  };

  const activeLimits = Object.keys(categoryLimits).length > 0 ? categoryLimits : defaultLimits;

  // Calculate category totals dynamically from expenses list
  const categoryTotals = expenses.reduce((acc, item) => {
    const cat = item.category ? item.category.toLowerCase() : 'other';
    acc[cat] = (acc[cat] || 0) + (Number(item.amount) || 0);
    return acc;
  }, {});

  // Prepare chart data for Recharts
  const chartData = Object.entries(activeLimits).map(([cat, limit]) => ({
    category: cat.charAt(0).toUpperCase() + cat.slice(1),
    Spent: categoryTotals[cat] || 0,
    Limit: limit
  }));

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
      <h2>Expense Tracker</h2>
      <p style={{ color: '#94a3b8', marginBottom: '24px' }}>Log and manage your monthly transactions.</p>

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
        borderLeft: '4px solid #ef4444'
      }}>
        <h3 style={{ margin: 0, fontSize: '1.1rem', color: '#94a3b8' }}>Total Logged Expenses:</h3>
        <div style={{ fontSize: '2rem', fontWeight: 'bold', color: '#ef4444' }}>
          ${totalSum.toLocaleString()}
        </div>
      </div>

      {/* Category Spending Breakdown & Chart Section */}
      <div style={{ 
        backgroundColor: 'rgba(30, 41, 59, 0.65)', 
        backdropFilter: 'blur(12px)', 
        border: '1px solid rgba(255,255,255,0.07)', 
        borderRadius: '12px', 
        padding: '20px 24px', 
        marginBottom: '24px' 
      }}>
        <h3 style={{ marginBottom: '16px', fontSize: '1.1rem', color: '#f8fafc' }}>Category Spending vs Limits</h3>
        
        {/* Visual Bar Chart */}
        <div style={{ width: '100%', height: '260px', minHeight: '260px', marginBottom: '24px' }}>
          <ResponsiveContainer width="100%" height={260}>
            <BarChart data={chartData} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
              <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
              <XAxis dataKey="category" stroke="#94a3b8" fontSize={12} />
              <YAxis stroke="#94a3b8" fontSize={12} />
              <Tooltip formatter={(value) => `$${value.toLocaleString()}`} contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', borderRadius: '8px', color: '#f8fafc' }} />
              <Legend wrapperStyle={{ fontSize: '0.85rem' }} />
              <Bar dataKey="Spent" fill="#3b82f6" radius={[4, 4, 0, 0]} />
              <Bar dataKey="Limit" fill="#64748b" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>

        {/* Progress Cards Grid */}
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '16px' }}>
          {Object.entries(activeLimits).map(([cat, limit]) => {
            const spent = categoryTotals[cat] || 0;
            const percentage = limit > 0 ? Math.min(Math.round((spent / limit) * 100), 100) : 0;
            const isOver = spent > limit;

            return (
              <div key={cat} style={{ backgroundColor: '#0f172a', border: '1px solid #334155', borderRadius: '8px', padding: '14px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
                  <span style={{ textTransform: 'capitalize', fontWeight: '600', color: '#f8fafc', fontSize: '0.9rem' }}>{cat}</span>
                  <span style={{ fontSize: '0.8rem', color: isOver ? '#ef4444' : '#94a3b8', fontWeight: 'bold' }}>
                    ${spent.toLocaleString()} / ${limit.toLocaleString()}
                  </span>
                </div>
                {/* Progress Bar */}
                <div style={{ width: '100%', height: '6px', backgroundColor: '#1e293b', borderRadius: '3px', overflow: 'hidden' }}>
                  <div style={{ 
                    width: `${percentage}%`, 
                    height: '100%', 
                    backgroundColor: isOver ? '#ef4444' : '#3b82f6',
                    borderRadius: '3px',
                    transition: 'width 0.3s ease'
                  }}></div>
                </div>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '6px', fontSize: '0.7rem', color: '#94a3b8' }}>
                  <span>{percentage}% used</span>
                  {isOver && <span style={{ color: '#ef4444', fontWeight: 'bold' }}>Over limit</span>}
                </div>
              </div>
            );
          })}
        </div>
      </div>

      {/* Add Expense Form */}
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
            <option value="food" style={{ backgroundColor: '#0f172a', color: '#fff' }}>Food</option>
            <option value="rent" style={{ backgroundColor: '#0f172a', color: '#fff' }}>Rent</option>
            <option value="entertainment" style={{ backgroundColor: '#0f172a', color: '#fff' }}>Entertainment</option>
            <option value="utilities" style={{ backgroundColor: '#0f172a', color: '#fff' }}>Utilities</option>
            <option value="transportation" style={{ backgroundColor: '#0f172a', color: '#fff' }}>Transportation</option>
            <option value="other" style={{ backgroundColor: '#0f172a', color: '#fff' }}>Other</option>
          </select>
        </div>

        <div>
          <label style={{ display: 'block', color: '#94a3b8', fontSize: '0.8rem', marginBottom: '6px' }}>Description</label>
          <input 
            type="text" 
            placeholder="Lunch"
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
            style={fieldStyle}
          />
        </div>

        <button 
          type="submit"
          style={{ padding: '0 20px', backgroundColor: '#3b82f6', color: '#fff', border: 'none', borderRadius: '6px', fontWeight: 'bold', cursor: 'pointer', height: '42px', display: 'flex', alignItems: 'center', justifyContent: 'center' }}
        >
          Add Expense
        </button>
      </form>

      {/* Expenses Table */}
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
            {expenses.length === 0 ? (
              <tr>
                <td colSpan="5" style={{ padding: '24px', textAlign: 'center', color: '#94a3b8' }}>No expenses logged yet.</td>
              </tr>
            ) : (
              expenses.map((item) => (
                <tr key={item.id} style={{ borderBottom: '1px solid rgba(255,255,255,0.04)' }}>
                  <td style={{ padding: '14px 20px', fontWeight: '500', textTransform: 'capitalize' }}>{item.category}</td>
                  <td style={{ padding: '14px 20px', color: '#94a3b8' }}>{item.description || '-'}</td>
                  <td style={{ padding: '14px 20px', fontWeight: 'bold', color: '#ef4444' }}>${Number(item.amount).toLocaleString()}</td>
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