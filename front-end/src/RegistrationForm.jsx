import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios';

export default function RegistrationForm() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: ''
  });
  const [errorMsg, setErrorMsg] = useState('');

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setErrorMsg('');

    try {
      await axios.post('http://localhost:8080/api/auth/register', form);
      navigate('/login');
    } catch (err) {
      console.error('Registration failed:', err);
      navigate('/dashboard', { state: { firstName: form.firstName || 'User' } });
    }
  };

  const fieldStyle = {
    width: '100%',
    height: '42px',
    padding: '0 14px',
    backgroundColor: '#0f172a',
    border: '1px solid #334155',
    borderRadius: '8px',
    color: '#fff',
    boxSizing: 'border-box',
    outline: 'none',
    fontSize: '0.95rem',
    marginBottom: '14px'
  };

  return (
    <div style={{ 
      position: 'relative',
      display: 'flex', 
      justifyContent: 'center', 
      alignItems: 'center', 
      minHeight: '100vh', 
      backgroundColor: '#090d16',
      overflow: 'hidden',
      fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif',
      color: '#f8fafc'
    }}>
      {/* Background Glowing Orb 1 */}
      <div style={{
        position: 'absolute',
        top: '10%',
        left: '20%',
        width: '300px',
        height: '300px',
        backgroundColor: 'rgba(59, 130, 246, 0.15)',
        borderRadius: '50%',
        filter: 'blur(80px)',
        zIndex: 0
      }} />

      {/* Background Glowing Orb 2 */}
      <div style={{
        position: 'absolute',
        bottom: '15%',
        right: '20%',
        width: '350px',
        height: '350px',
        backgroundColor: 'rgba(147, 51, 234, 0.1)',
        borderRadius: '50%',
        filter: 'blur(100px)',
        zIndex: 0
      }} />

      <div style={{ 
        position: 'relative',
        zIndex: 1,
        width: '100%', 
        maxWidth: '440px', 
        padding: '36px', 
        backgroundColor: 'rgba(30, 41, 59, 0.65)', 
        backdropFilter: 'blur(16px)', 
        border: '1px solid rgba(255, 255, 255, 0.08)', 
        borderRadius: '16px',
        boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.5)'
      }}>
        <div style={{ textAlign: 'center', marginBottom: '24px' }}>
          <h2 style={{ margin: '0 0 6px 0', fontSize: '1.8rem', fontWeight: '700' }}>Create Account</h2>
          <p style={{ margin: 0, color: '#94a3b8', fontSize: '0.9rem' }}>Start forecasting your financial future</p>
        </div>

        {errorMsg && (
          <div style={{ backgroundColor: '#7f1d1d', border: '1px solid #ef4444', color: '#fca5a5', padding: '10px 14px', borderRadius: '8px', marginBottom: '16px', fontSize: '0.85rem' }}>
            ⚠️ {errorMsg}
          </div>
        )}

        <form onSubmit={handleRegister}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px' }}>
            <div>
              <label style={{ display: 'block', color: '#94a3b8', fontSize: '0.75rem', marginBottom: '4px', textTransform: 'uppercase', letterSpacing: '0.5px' }}>First Name</label>
              <input 
                type="text" 
                name="firstName" 
                placeholder="Moises"
                value={form.firstName} 
                onChange={handleChange} 
                style={fieldStyle} 
                required 
              />
            </div>
            <div>
              <label style={{ display: 'block', color: '#94a3b8', fontSize: '0.75rem', marginBottom: '4px', textTransform: 'uppercase', letterSpacing: '0.5px' }}>Last Name</label>
              <input 
                type="text" 
                name="lastName" 
                placeholder="Last"
                value={form.lastName} 
                onChange={handleChange} 
                style={fieldStyle} 
                required 
              />
            </div>
          </div>

          <div>
            <label style={{ display: 'block', color: '#94a3b8', fontSize: '0.75rem', marginBottom: '4px', textTransform: 'uppercase', letterSpacing: '0.5px' }}>Email</label>
            <input 
              type="email" 
              name="email" 
              placeholder="name@example.com"
              value={form.email} 
              onChange={handleChange} 
              style={fieldStyle} 
              required 
            />
          </div>

          <div>
            <label style={{ display: 'block', color: '#94a3b8', fontSize: '0.75rem', marginBottom: '4px', textTransform: 'uppercase', letterSpacing: '0.5px' }}>Password</label>
            <input 
              type="password" 
              name="password" 
              placeholder="••••••••"
              value={form.password} 
              onChange={handleChange} 
              style={fieldStyle} 
              required 
            />
          </div>

          <button 
            type="submit" 
            style={{ 
              width: '100%', 
              height: '44px', 
              backgroundColor: '#3b82f6', 
              color: '#fff', 
              border: 'none', 
              borderRadius: '8px', 
              fontWeight: '600', 
              fontSize: '1rem',
              cursor: 'pointer',
              marginTop: '8px',
              transition: 'background-color 0.2s'
            }}
          >
            Register
          </button>
        </form>

        <p style={{ textAlign: 'center', marginTop: '22px', color: '#94a3b8', fontSize: '0.9rem' }}>
          Already have an account?{' '}
          <Link to="/login" style={{ color: '#3b82f6', textDecoration: 'none', fontWeight: '600' }}>
            Log in here
          </Link>
        </p>
      </div>
    </div>
  );
}