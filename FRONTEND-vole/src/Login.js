import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Login() {
  const navigate = useNavigate();
  
  const [loginData, setLoginData] = useState({
    email: '',
    password: '',
  });

  const [loginError, setLoginError] = useState(null);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setLoginData({ ...loginData, [name]: value });
  };

  const handleLoginSubmit = (e) => {
    e.preventDefault();

    // Get stored users data
    const storedUsers = JSON.parse(localStorage.getItem('registeredUsers')) || [];

    // Check login credentials
    const foundUser = storedUsers.find(
      (user) => user.email === loginData.email && user.password === loginData.password
    );

    if (foundUser) {
      // Successful login
      setLoginError(null);
      localStorage.setItem('loggedInUser', JSON.stringify(foundUser));
      
      console.log('Login successful for user:', loginData.email);
      
      navigate('/profile');
      window.location.reload();
      
    } else {
      // Invalid credentials
      setLoginError('Invalid email or password');
    }
  };

  return (
    <form onSubmit={handleLoginSubmit}>
      {loginError && <div className="text-red-500 mb-4">{loginError}</div>}
      <div className="mb-4">
        <label htmlFor="email" className="block text-sm font-medium text-gray-700">
          Email
        </label>
        <input
          type="email"
          id="email"
          name="email"
          value={loginData.email}
          onChange={handleInputChange}
          className="w-full px-4 py-2 border rounded-lg"
          required
        />
      </div>
      <div className="mb-4">
        <label htmlFor="password" className="block text-sm font-medium text-gray-700">
          Password
        </label>
        <input
          type="password"
          id="password"
          name="password"
          value={loginData.password}
          onChange={handleInputChange}
          className="w-full px-4 py-2 border rounded-lg"
          required
        />
      </div>
      <button
        type="submit"
        className="bg-purple-500 hover:bg-purple-600 text-white px-4 py-2 rounded-full w-full"
      >
        Login
      </button>
    </form>
  );
}

export default Login;
