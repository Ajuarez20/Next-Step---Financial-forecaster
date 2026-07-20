// RegistrationForm.jsx
import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom'; // 1. Added this import

function RegistrationForm() {
  const [formData, setFormData] = useState({
    firstname: '',
    lastname: '',
    email: '',
    password: ''
  });

  const navigate = useNavigate(); // 2. Initialized the navigation hook

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
      
      // 3. Replaced the alert with the redirect to dashboard
      navigate('/dashboard', { 
        state: { firstName: response.data.firstname } 
      });
      
    } catch (error) {
      console.error('Registration Error:', error);
      alert('Registration Failed.');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Register</h2>
      <input name="firstname" placeholder="First Name" onChange={handleChange} required />
      <input name="lastname" placeholder="Last Name" onChange={handleChange} required />
      <input name="email" type="email" placeholder="Email" onChange={handleChange} required />
      <input name="password" type="password" placeholder="Password" onChange={handleChange} required />
      <button type="submit">Submit</button>
    </form>
  );
}

export default RegistrationForm;