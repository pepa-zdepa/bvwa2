import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import API_URLS from './config';

function Header() {
  const navigate = useNavigate();
  const loggedInUser = JSON.parse(localStorage.getItem('loggedInUser')) || null;

  const [user, setUser] = useState(loggedInUser);

  useEffect(() => {
    // Fetch user data when the component mounts
    const fetchUserData = async () => {
      try {
        const response = await fetch(API_URLS.GET_USER, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${loggedInUser.token}`,
          },
        });

        if (response.ok) {
          const userData = await response.json();
          setUser(userData);
        } else {
          // Handle error, e.g., invalid token
          console.error('Chyba cteni user data:', response.statusText);
        }
      } catch (error) {
        console.error('Chyba cteni user data:', error);
      }
    };

    if (loggedInUser) {
      fetchUserData();
    }
  }, [loggedInUser]);

  const handleLogout = async () => {
    try {
      // Make a POST request to the backend to handle logout
      const response = await fetch(API_URLS.LOGOUT, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${loggedInUser.token}`,
        },
      });

      if (response.ok) {
        localStorage.removeItem('loggedInUser');
        setUser(null);
        navigate('/');
        window.location.reload();
      } else {
        // Handle error, e.g., invalid token
        console.error('Error during logout:', response.statusText);
      }
    } catch (error) {
      console.error('Error during logout:', error);
    }
  };

  return (
    <header className="bg-purple-500 py-4">
      <nav className="container mx-auto flex items-center justify-between">
        <div className="text-white text-2xl font-bold">Your Logo</div>
        <ul className="space-x-4 flex items-center">
          {/* Home link always visible */}
          <li>
            <Link to="/" className="text-white hover:text-purple-300">
              Index
            </Link>
          </li>
          {/* Display profile and log-out button if the user is logged in */}
          {user && (
            <>
              <li>
                <Link to="/profile" className="text-white hover:text-purple-300">
                  <img
                    src={user.profilePicture || 'default-profile-pic.jpg'}
                    alt="Profil"
                    className="w-8 h-8 rounded-full object-cover"
                  />
                  Ahoj, {user.nickname}
                </Link>
              </li>
              {loggedInUser.isAdmin && (
                <li>
                  <Link to="/admin" className="text-white hover:text-purple-300">
                    Adminová panel
                  </Link>
                </li>
              )}
              <li>
                <button
                  onClick={handleLogout}
                  className="text-white hover:text-purple-300 focus:outline-none"
                >
                  Odhlásít
                </button>
              </li>
            </>
          )}
        </ul>
      </nav>
    </header>
  );
}

export default Header;