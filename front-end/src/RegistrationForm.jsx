import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function RegistrationForm() {
  const [formData, setFormData] = useState({
    firstname: '',
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
    throw new Error("REGISTRATION FORM MARKER");
    e.preventDefault();
    try {
      const payload = {
        firstname: "FORCE_FIRSTNAME_TEST",
        lastname: formData.lastname ?? "",
        email: formData.email ?? "",
        password: formData.password ?? ""
      };

      console.log("REGISTER PAYLOAD SENT:", payload);
      console.log("REG_FORM_MARKER_V2", { formData });
      debugger;
      
      const response = await axios.post('http://localhost:8080/register', payload);
      console.log('Registration Successful:', response.data);
      alert('Account created successfully! Please log in.');
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
        name="firstname" 
        placeholder="First Name" 
        value={formData.firstname}
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
