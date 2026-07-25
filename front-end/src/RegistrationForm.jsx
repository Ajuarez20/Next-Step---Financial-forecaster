import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function RegistrationForm() {
  const [formData, setFormData] = useState({
    firstName: '',
    lastname: '',
    email: '',
    password: ''
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/register', formData);
      console.log('Registration Successful:', response.data);
      alert('Account created successfully! Please log in.');
      
      // Redirect straight to the login page after successful registration
      navigate('/login');
    } catch (error) {
      console.error('Registration Error:', error);
      alert('Error registering user. Please try again.');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Create Next Step Account</h2>
      
      <input 
        name="firstName" 
        placeholder="First Name" 
        value={formData.firstName}
        onChange={handleChange} 
        required 
      />
      
      <input 
        name="lastname" 
        placeholder="Last Name" 
        value={formData.lastname}
        onChange={handleChange} 
        required 
      />
      
      <input 
        name="email" 
        type="email" 
        placeholder="Email" 
        value={formData.email}
        onChange={handleChange} 
        required 
      />
      
      <input 
        name="password" 
        type="password" 
        placeholder="Password" 
        value={formData.password}
        onChange={handleChange} 
        required 
      />
      
      <button type="submit">Register</button>

      <p style={{ marginTop: '10px', fontSize: '14px' }}>
        Already have an account? <a href="/login">Log in here</a>
      </p>
    </form>
  );
}

export default RegistrationForm;
