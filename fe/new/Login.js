import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API_URLS from './config';

function Login() {
  const navigate = useNavigate();
  
  const [loginData, setLoginData] = useState({
    username: '',
    password: '',
  });

  const [loginError, setLoginError] = useState(null);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setLoginData({ ...loginData, [name]: value });
  };

  const handleLoginSubmit = async (e) => {
    e.preventDefault();
  
    try {
      const response = await fetch(API_URLS.LOGIN, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginData),
      });
  
      if (!response.ok) {
        // Handle login failure
        const errorData = await response.json();
        setLoginError(`Login selhal: ${errorData.message}`);
        return;
      }
  
      // Login successful
      setLoginError(null);
      const userData = await response.json();
      localStorage.setItem('loggedInUser', JSON.stringify(userData));
      console.log('Login povedl pro:', loginData.username);
      navigate('/');
      window.location.reload();
    } catch (error) {
      console.error('Chyba loginu:', error.message);
      setLoginError('Chyba loginu. Prosím zopakujte to.');
    }
  };

  return (
    <form onSubmit={handleLoginSubmit}>
      {loginError && <div className="text-red-500 mb-4">{loginError}</div>}
      <div className="mb-4">
        <label htmlFor="username" className="block text-sm font-medium text-gray-700">
        Username
        </label>
        <input
          type="username"
          id="username"
          name="username"
          value={loginData.username}
          onChange={handleInputChange}
          className="w-full px-4 py-2 border rounded-lg"
          required
        />
      </div>
      <div className="mb-4">
        <label htmlFor="password" className="block text-sm font-medium text-gray-700">
          Heslo
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
        Přihlásit
      </button>
    </form>
  );
}

export default Login;
