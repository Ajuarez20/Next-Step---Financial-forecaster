import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

import ExpensesView from './ExpensesView';
import IncomeView from './IncomeView';
import ForecastsView from './ForecastsView';
import SettingsView from './SettingsView';
import CopilotInsights from './CopilotInsights';
import './Dashboard.css';

function Dashboard() {
  const location = useLocation();
  const navigate = useNavigate();
  
  const userName = location.state?.firstName || "Guest"; 
  const [activeTab, setActiveTab] = useState('overview');

  const [inputs, setInputs] = useState({
    monthlyIncome: 4500,
    monthlyExpenses: 3000,
    currentSavings: 5000,
    targetGoalAmount: 20000,
    projectionMonths: 24
  });

  const [expensesList, setExpensesList] = useState([]);
  const [incomeList, setIncomeList] = useState([]);
  const [chartData, setChartData] = useState([]);

  const [categoryLimits, setCategoryLimits] = useState({
    food: 200,
    rent: 1200,
    entertainment: 200,
    utilities: 150
  });

  const fetchAllData = async () => {
    try {
      // Fetch Expenses
      const expResponse = await axios.get('http://localhost:8080/api/expenses/all');
      const expItems = expResponse.data || [];
      setExpensesList(expItems);
      const expenseTotal = expItems.reduce((sum, item) => sum + (Number(item.amount) || 0), 0);

      // Fetch Income
      const incResponse = await axios.get('http://localhost:8080/api/income/all');
      const incItems = incResponse.data || [];
      setIncomeList(incItems);
      const incomeTotal = incItems.reduce((sum, item) => sum + (Number(item.amount) || 0), 0);
      
      setInputs((prev) => ({
        ...prev,
        monthlyExpenses: expenseTotal,
        monthlyIncome: incomeTotal > 0 ? incomeTotal : prev.monthlyIncome
      }));
    } catch (error) {
      console.error('Failed to fetch financial data:', error);
    }
  };

  useEffect(() => {
    fetchAllData();
  }, []);

  const fetchForecast = async () => {
    try {
      const response = await axios.post('http://localhost:8080/api/forecast', inputs);
      setChartData(response.data);
    } catch (error) {
      console.error('Forecast calculation failed:', error);
    }
  };

  useEffect(() => {
    fetchForecast();
  }, [inputs]);

  const handleInputChange = (e) => {
    setInputs({
      ...inputs,
      [e.target.name]: parseFloat(e.target.value) || 0
    });
  };

  const monthlyNet = inputs.monthlyIncome - inputs.monthlyExpenses;

  return (
    <div className="dashboard-layout">
      {/* Sidebar Navigation */}
      <aside className="sidebar">
        <h2>Next Step</h2>
        <nav>
          <ul>
            <li className={activeTab === 'overview' ? 'active' : ''} onClick={() => setActiveTab('overview')} style={{ cursor: 'pointer' }}>
              <span style={{ marginRight: '12px' }}>📊</span> Overview
            </li>
            <li className={activeTab === 'forecasts' ? 'active' : ''} onClick={() => setActiveTab('forecasts')} style={{ cursor: 'pointer' }}>
              <span style={{ marginRight: '12px' }}>📈</span> Forecasts
            </li>
            <li className={activeTab === 'income' ? 'active' : ''} onClick={() => setActiveTab('income')} style={{ cursor: 'pointer' }}>
              <span style={{ marginRight: '12px' }}>💰</span> Income
            </li>
            <li className={activeTab === 'expenses' ? 'active' : ''} onClick={() => setActiveTab('expenses')} style={{ cursor: 'pointer' }}>
              <span style={{ marginRight: '12px' }}>💳</span> Expenses
            </li>
            <li className={activeTab === 'settings' ? 'active' : ''} onClick={() => setActiveTab('settings')} style={{ cursor: 'pointer' }}>
              <span style={{ marginRight: '12px' }}>⚙️</span> Settings
            </li>
          </ul>
        </nav>
        <div className="sidebar-footer">
          <button className="logout-btn" onClick={() => navigate('/login')}>
            Log Out
          </button>
        </div>
      </aside>

      {/* Main Workspace */}
      <main className="main-content">
        <header className="main-header" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '28px' }}>
          <div>
            <h1 style={{ margin: 0, fontSize: '1.6rem', color: '#f8fafc' }}>Welcome back, {userName} 👋</h1>
            <p style={{ margin: '4px 0 0 0', color: '#94a3b8', fontSize: '0.85rem' }}>Here is your financial overview for this month.</p>
          </div>
          
          <div style={{ display: 'flex', alignItems: 'center', gap: '10px', backgroundColor: 'rgba(30, 41, 59, 0.7)', padding: '6px 14px', borderRadius: '20px', border: '1px solid rgba(255,255,255,0.08)', backdropFilter: 'blur(10px)' }}>
            <div style={{ width: '28px', height: '28px', borderRadius: '50%', backgroundColor: '#3b82f6', display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 'bold', fontSize: '0.8rem', color: '#fff' }}>
              {userName.charAt(0).toUpperCase()}
            </div>
            <span style={{ fontSize: '0.9rem', fontWeight: '600', color: '#f8fafc' }}>{userName}</span>
            <span style={{ width: '8px', height: '8px', borderRadius: '50%', backgroundColor: '#22c55e', display: 'inline-block', marginLeft: '4px' }}></span>
          </div>
        </header>

        {activeTab === 'overview' && (
          <>
            <CopilotInsights inputs={inputs} expenses={expensesList} categoryLimits={categoryLimits} />

            <div className="dashboard-grid">
              <div className="card metric-card">
                <h3>Current Balance</h3>
                <p className="metric-value">${inputs.currentSavings.toLocaleString()}</p>
              </div>
              <div className="card metric-card">
                <h3>Monthly Net Flow</h3>
                <p className={`metric-value ${monthlyNet >= 0 ? 'positive' : 'negative'}`}>
                  ${monthlyNet.toLocaleString()} / mo
                </p>
              </div>
              <div className="card metric-card">
                <h3>Savings Goal Target</h3>
                <p className="metric-value">${inputs.targetGoalAmount.toLocaleString()}</p>
              </div>
            </div>

            <div className="content-grid">
              <div className="card controls-card">
                <h3>What-If Controls</h3>
                
                <div className="control-group">
                  <label>Monthly Income: <strong>${inputs.monthlyIncome.toLocaleString()}</strong></label>
                  <input type="range" name="monthlyIncome" min="1000" max="15000" step="100" value={inputs.monthlyIncome} onChange={handleInputChange} />
                </div>

                <div className="control-group">
                  <label>Monthly Expenses: <strong>${inputs.monthlyExpenses.toLocaleString()}</strong></label>
                  <input type="range" name="monthlyExpenses" min="0" max="12000" step="100" value={inputs.monthlyExpenses} onChange={handleInputChange} />
                </div>

                <div className="control-group">
                  <label>Current Savings ($)</label>
                  <input type="number" name="currentSavings" value={inputs.currentSavings} onChange={handleInputChange} />
                </div>

                <div className="control-group">
                  <label>Savings Goal ($)</label>
                  <input type="number" name="targetGoalAmount" value={inputs.targetGoalAmount} onChange={handleInputChange} />
                </div>
              </div>

              <div className="card chart-card">
                <h3>Savings Trajectory ({inputs.projectionMonths} Months)</h3>
                <div className="chart-wrapper">
                  <ResponsiveContainer width="100%" height="100%">
                    <LineChart data={chartData}>
                      <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
                      <XAxis dataKey="monthLabel" stroke="#94a3b8" />
                      <YAxis stroke="#94a3b8" />
                      <Tooltip formatter={(value) => `$${value.toLocaleString()}`} contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', borderRadius: '8px', color: '#f8fafc' }} />
                      <Line type="monotone" dataKey="projectedSavings" stroke="#3b82f6" strokeWidth={3} name="Projected Savings" />
                      <Line type="dash" dataKey="goalTarget" stroke="#ef4444" strokeDasharray="5 5" name="Savings Goal" />
                    </LineChart>
                  </ResponsiveContainer>
                </div>
              </div>
            </div>
          </>
        )}

        {activeTab === 'forecasts' && (
          <ForecastsView monthlyIncome={inputs.monthlyIncome} monthlyExpenses={inputs.monthlyExpenses} currentSavings={inputs.currentSavings} targetGoalAmount={inputs.targetGoalAmount} />
        )}

        {activeTab === 'income' && (
          <IncomeView onIncomeChange={fetchAllData} incomes={incomeList} />
        )}

        {activeTab === 'expenses' && (
          <ExpensesView onExpenseChange={fetchAllData} expenses={expensesList} />
        )}

        {activeTab === 'settings' && (
          <SettingsView limits={categoryLimits} setLimits={setCategoryLimits} userName={userName} />
        )}
      </main>
    </div>
  );
}

export default Dashboard;
