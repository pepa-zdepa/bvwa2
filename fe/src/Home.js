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
      return showRegisterForm ? <Register /> : <Login loggedInUser={loggedInUser}/>;
    }
    return null;
  };

  return (
    <div className="bg-gradient-to-r from-purple-500 to-orange-500 min-h-screen flex justify-center items-center">
      <div className="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
        <h1 className="text-3xl font-bold text-center mb-4">
          {loggedInUser ? "Jste již přihlašený." : showRegisterForm ? 'Zaregistrovat se na webovce' : 'Přihlásit do webovky'}
        </h1>
        {renderForms()}
        {!loggedInUser && (
          <button
            onClick={toggleForm}
            className="text-purple-500 hover:underline mt-4 block text-center cursor-pointer"
          >
            {showRegisterForm ? 'Již máte účet. Tak jasně přihláste.' : 'Nemate účet? Tak ho vytvořte zde.'}
          </button>
        )}
      </div>
    </div>
  );
}

export default Home;
