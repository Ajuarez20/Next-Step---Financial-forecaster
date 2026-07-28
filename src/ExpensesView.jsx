import React, { useState } from 'react';
import axios from 'axios';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend } from 'recharts';

export default function ExpensesView({ onExpenseChange, expenses = [], limits = {}, financialProfileId = 1 }) {
  const [amount, setAmount] = useState('');
  const [category, setCategory] = useState('Food');
  const [description, setDescription] = useState('');

  const totalsByCategory = expenses.reduce((acc, item) => {
    const cat = (item.category || 'Uncategorized').trim().toLowerCase();
    acc[cat] = (acc[cat] || 0) + (Number(item.amount) || 0);
    return acc;
  }, {});

  const totalExpenses = expenses.reduce((sum, item) => sum + (Number(item.amount) || 0), 0);

  const chartData = Object.keys(limits).map(rawCat => {
    const cat = rawCat.trim().toLowerCase();
    return {
      category: rawCat.charAt(0).toUpperCase() + rawCat.slice(1),
      Limit: Number(limits[rawCat]) || 0,
      Spent: totalsByCategory[cat] || 0
    };
  });

  const handleAddExpense = async (e) => {
    e.preventDefault();
    if (!amount) return;

    try {
      // Corrected to include financialProfileId path variable matching backend controller
      await axios.post(`http://localhost:8080/api/expenses/add/${financialProfileId}`, {
        amount: parseFloat(amount),
        category: category,
        description: description || 'No description'
      });
      setAmount('');
      setDescription('');
      if (onExpenseChange) onExpenseChange();
    } catch (error) {
      console.error('Failed to add expense:', error);
    }
  };

  const handleDeleteExpense = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/expenses/${id}`);
      if (onExpenseChange) onExpenseChange();
    } catch (error) {
      console.error('Failed to delete expense:', error);
    }
  };

  return (
    <div style={{ padding: '24px', maxWidth: '1100px', margin: '0 auto' }}>
      <h2>Expense Tracker</h2>
      <p style={{ color: '#94a3b8' }}>Log and manage your monthly transactions synced with your backend.</p>

      <div style={{ backgroundColor: '#1e293b', padding: '20px', borderRadius: '12px', margin: '20px 0', border: '1px solid rgba(255,255,255,0.07)' }}>
        <h3 style={{ margin: '0 0 12px 0', fontSize: '1rem', color: '#94a3b8' }}>TOTAL LOGGED EXPENSES</h3>
        <div style={{ fontSize: '2rem', fontWeight: 'bold', color: '#ef4444' }}>
          ${totalExpenses.toLocaleString()}
        </div>
      </div>

      <div style={{ backgroundColor: '#1e293b', padding: '20px', borderRadius: '12px', marginBottom: '24px', border: '1px solid rgba(255,255,255,0.07)' }}>
        <h3 style={{ marginTop: 0, fontSize: '1.1rem' }}>Category Spending vs Limits</h3>
        <div style={{ width: '100%', height: '280px', marginTop: '15px' }}>
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
              <XAxis dataKey="category" stroke="#94a3b8" />
              <YAxis stroke="#94a3b8" />
              <Tooltip contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', borderRadius: '8px', color: '#f8fafc' }} formatter={(val) => `$${val.toLocaleString()}`} />
              <Legend />
              <Bar dataKey="Limit" fill="#334155" radius={[4, 4, 0, 0]} />
              <Bar dataKey="Spent" fill="#3b82f6" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      <div style={{ backgroundColor: '#1e293b', padding: '20px', borderRadius: '12px', marginBottom: '24px', border: '1px solid rgba(255,255,255,0.07)' }}>
        <h3 style={{ marginTop: 0, fontSize: '1.1rem' }}>Add New Expense</h3>
        <form onSubmit={handleAddExpense} style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 2fr auto', gap: '12px', alignItems: 'end' }}>
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
              <option value="Food">Food</option>
              <option value="Rent">Rent</option>
              <option value="Entertainment">Entertainment</option>
              <option value="Utilities">Utilities</option>
              <option value="Other">Other</option>
            </select>
          </div>
          <div>
            <label style={{ display: 'block', fontSize: '0.8rem', color: '#94a3b8', marginBottom: '6px' }}>Description</label>
            <input 
              type="text" 
              placeholder="e.g. Grocery run" 
              value={description} 
              onChange={(e) => setDescription(e.target.value)}
              style={{ width: '100%', padding: '10px', backgroundColor: '#0f172a', border: '1px solid #334155', borderRadius: '8px', color: '#fff', boxSizing: 'border-box' }}
            />
          </div>
          <button type="submit" style={{ padding: '10px 20px', backgroundColor: '#3b82f6', color: '#fff', border: 'none', borderRadius: '8px', fontWeight: '600', cursor: 'pointer', height: '41px' }}>
            Add Expense
          </button>
        </form>
      </div>

      <div style={{ backgroundColor: '#1e293b', padding: '20px', borderRadius: '12px', border: '1px solid rgba(255,255,255,0.07)' }}>
        <h3 style={{ marginTop: 0, fontSize: '1.1rem' }}>Transaction History</h3>
        {expenses.length === 0 ? (
          <p style={{ color: '#94a3b8' }}>No expenses logged yet.</p>
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
              {expenses.map((item, idx) => (
                <tr key={item.id || idx} style={{ borderBottom: '1px solid rgba(51, 65, 85, 0.4)' }}>
                  <td style={{ padding: '12px 10px', color: '#f8fafc', fontWeight: '500' }}>{item.category}</td>
                  <td style={{ padding: '12px 10px', color: '#94a3b8' }}>{item.description || '-'}</td>
                  <td style={{ padding: '12px 10px', color: '#ef4444', fontWeight: 'bold' }}>${Number(item.amount).toLocaleString()}</td>
                  <td style={{ padding: '12px 10px' }}>
                    <button 
                      onClick={() => handleDeleteExpense(item.id)}
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