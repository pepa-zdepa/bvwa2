import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Login from './Login';
import Register from './Register';
import MessageDetail from './MessageDetail'; // Import the MessageDetail component

function Home({ loggedInUser }) {
  const [showRegisterForm, setShowRegisterForm] = useState(false);
  const navigate = useNavigate();

  const toggleForm = () => {
    setShowRegisterForm(!showRegisterForm);
  };

  // Redirect to the messaging page if the user is logged in
  if (loggedInUser) {
    return <MessageDetail />;
  }

  return (
    <div className="bg-gradient-to-r from-purple-500 to-orange-500 min-h-screen flex justify-center items-center">
      <div className="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
        <h1 className="text-3xl font-bold text-center mb-4">
          {showRegisterForm ? 'Registrovat účet.' : 'Přihlásit do účtu.'}
        </h1>
        {!loggedInUser ? (
          showRegisterForm ? <Register /> : <Login />
        ) : null}
        {!loggedInUser && (
          <button
            onClick={toggleForm}
            className="text-purple-500 hover:underline mt-4 block text-center cursor-pointer"
          >
            {showRegisterForm ? 'Již máte účet? Tak přihlášte.' : 'Nemáte účet? Tak ho vytvořte.'}
          </button>
        )}
      </div>
    </div>
  );
}

export default Home;
