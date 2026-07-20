import React from 'react';
import { useLocation } from 'react-router-dom'; // 1. Import useLocation
import './Dashboard.css';

function Dashboard() {
  const location = useLocation();
  
  // 2. Grab the firstName from the redirect state. 
  // If someone goes directly to /dashboard without registering, it defaults to "Guest"
  const userName = location.state?.firstName || "Guest"; 

  return (
    <div className="dashboard-layout">
      <aside className="sidebar">
        <h2>Next Step</h2>
        <nav>
          <ul>
            <li>Overview</li>
            <li>Forecasts</li>
            <li>Expenses</li>
            <li>Settings</li>
          </ul>
        </nav>
      </aside>

      <main className="main-content">
        <header>
          {/* This now displays the actual database name! */}
          <h1>Welcome back, {userName}</h1> 
        </header>
        
        <div className="dashboard-grid">
          <div className="card">
            <h3>Current Balance</h3>
            <p>$0.00</p>
          </div>
          <div className="card">
            <h3>Monthly Forecast</h3>
            <p>Pending data...</p>
          </div>
          <div className="card">
            <h3>Recent Activity</h3>
            <p>No recent transactions.</p>
          </div>
        </div>
      </main>
    </div>
  );
}

export default Dashboard;