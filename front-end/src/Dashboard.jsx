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
        <div style={{ padding: '20px', marginTop: 'auto' }}>
          <button 
            onClick={() => navigate('/login')}
            style={{ 
              width: '100%', 
              padding: '10px', 
              background: '#e74c3c', 
              color: '#fff', 
              border: 'none', 
              borderRadius: '6px', 
              cursor: 'pointer', 
              fontWeight: 'bold' 
            }}
          >
            Log Out
          </button>
        </div>
      </aside>

      <main className="main-content">
        <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
          <h1>Welcome back, {userName} 👋</h1>
        </header>

        {/* Top Metric Cards */}
        <div className="dashboard-grid" style={{ marginBottom: '25px' }}>
          <div className="card">
            <h3>Current Balance</h3>
            <p style={{ fontSize: '1.8rem', fontWeight: 'bold', margin: '10px 0 0' }}>
              ${inputs.currentSavings.toLocaleString()}
            </p>
          </div>
          <div className="card">
            <h3>Monthly Net Flow</h3>
            <p style={{ fontSize: '1.8rem', fontWeight: 'bold', margin: '10px 0 0', color: monthlyNet >= 0 ? '#27ae60' : '#c0392b' }}>
              ${monthlyNet.toLocaleString()} / mo
            </p>
          </div>
          <div className="card">
            <h3>Savings Goal Target</h3>
            <p style={{ fontSize: '1.8rem', fontWeight: 'bold', margin: '10px 0 0' }}>
              ${inputs.targetGoalAmount.toLocaleString()}
            </p>
          </div>
        </div>

        {/* What-If Controls & Interactive Chart */}
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: '20px' }}>
          
          {/* Controls Panel */}
          <div className="card">
            <h3>What-If Controls</h3>
            
            <div style={{ marginTop: '15px' }}>
              <label style={{ display: 'block', fontSize: '0.9rem', marginBottom: '5px' }}>
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
                style={{ width: '100%' }} 
              />
            </div>

            <div style={{ marginTop: '15px' }}>
              <label style={{ display: 'block', fontSize: '0.9rem', marginBottom: '5px' }}>
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
                style={{ width: '100%' }} 
              />
            </div>

            <div style={{ marginTop: '15px' }}>
              <label style={{ display: 'block', fontSize: '0.9rem', marginBottom: '5px' }}>Current Savings ($)</label>
              <input 
                type="number" 
                name="currentSavings" 
                value={inputs.currentSavings} 
                onChange={handleInputChange} 
                style={{ width: '100%', padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }} 
              />
            </div>

            <div style={{ marginTop: '15px' }}>
              <label style={{ display: 'block', fontSize: '0.9rem', marginBottom: '5px' }}>Savings Goal ($)</label>
              <input 
                type="number" 
                name="targetGoalAmount" 
                value={inputs.targetGoalAmount} 
                onChange={handleInputChange} 
                style={{ width: '100%', padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }} 
              />
            </div>
          </div>

          {/* Interactive Chart Panel */}
          <div className="card">
            <h3>Savings Trajectory ({inputs.projectionMonths} Months)</h3>
            <div style={{ width: '100%', height: '320px', marginTop: '15px' }}>
              <ResponsiveContainer width="100%" height="100%">
                <LineChart data={chartData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="monthLabel" />
                  <YAxis />
                  <Tooltip formatter={(value) => `$${value}`} />
                  <Line type="monotone" dataKey="projectedSavings" stroke="#2b6cb0" strokeWidth={3} name="Projected Savings" />
                  <Line type="dash" dataKey="goalTarget" stroke="#e53e3e" strokeDasharray="5 5" name="Savings Goal" />
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
