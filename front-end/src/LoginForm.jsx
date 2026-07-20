import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function LoginForm() {
  const [credentials, setCredentials] = useState({
    email: '',
    password: ''
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    setCredentials({
      ...credentials,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/login', credentials);
      console.log('Login Successful:', response.data);
      
      // Safely gets the name whether your backend returns firstName or firstname
      const nameToDisplay = response.data.firstName || response.data.firstname || 'User';

      navigate('/dashboard', { 
        state: { firstName: nameToDisplay } 
      });
      
    } catch (error) {
      console.error('Login Error:', error);
      alert('Invalid email or password.');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Log In to Next Step</h2>
      <input 
        name="email" 
        type="email" 
        placeholder="Email" 
        value={credentials.email} 
        onChange={handleChange} 
        required 
      />
      <input 
        name="password" 
        type="password" 
        placeholder="Password" 
        value={credentials.password} 
        onChange={handleChange} 
        required 
      />
      <button type="submit">Log In</button>
      
      <p style={{ marginTop: '10px', fontSize: '14px' }}>
        Don't have an account? <a href="/register">Register here</a>
      </p>
    </form>
  );
}

export default LoginForm;