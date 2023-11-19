import React, { useState } from 'react';
import Login from './Login';
import Register from './Register';

function Home() {
  const [showRegisterForm, setShowRegisterForm] = useState(false);

  const toggleForm = () => {
    setShowRegisterForm(!showRegisterForm);
  };

  return (
    <div className="bg-gradient-to-r from-purple-500 to-orange-500 min-h-screen flex justify-center items-center">
      <div className="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
        <h1 className="text-3xl font-bold text-center mb-4">
          {showRegisterForm ? 'Register on Your Website' : 'Login to Your Account'}
        </h1>
        {showRegisterForm ? (
          <Register />
        ) : (
          <Login />
        )}
        <button
          onClick={toggleForm}
          className="text-purple-500 hover:underline mt-4 block text-center cursor-pointer"
        >
          {showRegisterForm ? 'Already registered? Login here.' : 'Not registered? Register here.'}
        </button>
      </div>
    </div>
  );
}

export default Home;
