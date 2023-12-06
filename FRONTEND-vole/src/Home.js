import React, { useState } from 'react';
import Login from './Login';
import Register from './Register';

function Home({ loggedInUser }) {
  const [showRegisterForm, setShowRegisterForm] = useState(false);

  const toggleForm = () => {
    setShowRegisterForm(!showRegisterForm);
  };

  // Conditionally render the forms based on whether the user is logged in
  const renderForms = () => {
    if (!loggedInUser) {
      return showRegisterForm ? <Register /> : <Login />;
    }
    return null;
  };

  return (
    <div className="bg-gradient-to-r from-purple-500 to-orange-500 min-h-screen flex justify-center items-center">
      <div className="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
        <h1 className="text-3xl font-bold text-center mb-4">
          {loggedInUser ? "You're logged in. Get to work." : showRegisterForm ? 'Register on Your Website' : 'Login to Your Account'}
        </h1>
        {renderForms()}
        {!loggedInUser && (
          <button
            onClick={toggleForm}
            className="text-purple-500 hover:underline mt-4 block text-center cursor-pointer"
          >
            {showRegisterForm ? 'Already registered? Login here.' : 'Not registered? Register here.'}
          </button>
        )}
      </div>
    </div>
  );
}

export default Home;
