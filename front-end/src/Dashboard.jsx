import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import './Dashboard.css';

function Dashboard() {
  const location = useLocation();
  const navigate = useNavigate();
  
  // Grab the firstName from redirect state, default to "Guest" if visited directly
  const userName = location.state?.firstName || "Guest"; 

  // What-If Forecast State
  const [inputs, setInputs] = useState({
    monthlyIncome: 4500,
    monthlyExpenses: 3000,
    currentSavings: 5000,
    targetGoalAmount: 20000,
    projectionMonths: 24
  });

  const [chartData, setChartData] = useState([]);

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
            <li className="active">Overview</li>
            <li>Forecasts</li>
            <li>Expenses</li>
            <li>Settings</li>
          </ul>
        </nav>
        <div className="sidebar-footer">
          <button 
            className="logout-btn"
            onClick={() => navigate('/login')}
          >
            Log Out
          </button>
        </div>
      </aside>

      {/* Main Workspace */}
      <main className="main-content">
        <header className="main-header">
          <h1>Welcome back, {userName} 👋</h1>
        </header>

        {/* Top Metric Cards */}
        <div className="dashboard-grid">
          <div className="card metric-card">
            <h3>Current Balance</h3>
            <p className="metric-value">
              ${inputs.currentSavings.toLocaleString()}
            </p>
          </div>
          <div className="card metric-card">
            <h3>Monthly Net Flow</h3>
            <p className={`metric-value ${monthlyNet >= 0 ? 'positive' : 'negative'}`}>
              ${monthlyNet.toLocaleString()} / mo
            </p>
          </div>
          <div className="card metric-card">
            <h3>Savings Goal Target</h3>
            <p className="metric-value">
              ${inputs.targetGoalAmount.toLocaleString()}
            </p>
          </div>
        </div>

        {/* What-If Controls & Interactive Chart */}
        <div className="content-grid">
          
          {/* Controls Panel */}
          <div className="card controls-card">
            <h3>What-If Controls</h3>
            
            <div className="control-group">
              <label>
                Monthly Income: <strong>${inputs.monthlyIncome.toLocaleString()}</strong>
              </label>
              <input 
                type="range" 
                name="monthlyIncome" 
                min="1000" 
                max="15000" 
                step="100" 
                value={inputs.monthlyIncome} 
                onChange={handleInputChange} 
              />
            </div>

            <div className="control-group">
              <label>
                Monthly Expenses: <strong>${inputs.monthlyExpenses.toLocaleString()}</strong>
              </label>
              <input 
                type="range" 
                name="monthlyExpenses" 
                min="500" 
                max="12000" 
                step="100" 
                value={inputs.monthlyExpenses} 
                onChange={handleInputChange} 
              />
            </div>

            <div className="control-group">
              <label>Current Savings ($)</label>
              <input 
                type="number" 
                name="currentSavings" 
                value={inputs.currentSavings} 
                onChange={handleInputChange} 
              />
            </div>

            <div className="control-group">
              <label>Savings Goal ($)</label>
              <input 
                type="number" 
                name="targetGoalAmount" 
                value={inputs.targetGoalAmount} 
                onChange={handleInputChange} 
              />
            </div>
          </div>

          {/* Interactive Chart Panel */}
          <div className="card chart-card">
            <h3>Savings Trajectory ({inputs.projectionMonths} Months)</h3>
            <div className="chart-wrapper">
              <ResponsiveContainer width="100%" height="100%">
                <LineChart data={chartData}>
                  <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
                  <XAxis dataKey="monthLabel" stroke="#94a3b8" />
                  <YAxis stroke="#94a3b8" />
                  <Tooltip 
                    formatter={(value) => `$${value.toLocaleString()}`}
                    contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', borderRadius: '8px', color: '#f8fafc' }}
                  />
                  <Line type="monotone" dataKey="projectedSavings" stroke="#3b82f6" strokeWidth={3} name="Projected Savings" />
                  <Line type="dash" dataKey="goalTarget" stroke="#ef4444" strokeDasharray="5 5" name="Savings Goal" />
                </LineChart>
              </ResponsiveContainer>
            </div>
          </div>

        </div>
      </main>
    </div>
  );
}

export default Dashboard;
