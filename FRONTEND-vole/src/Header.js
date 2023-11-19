import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';


function Header({ loggedInUser }) {
  const navigate = useNavigate();

  const [user, setUser] = useState(loggedInUser);

  useEffect(() => {
    setUser(loggedInUser);
  }, [loggedInUser]);

  const handleLogout = () => {
    // Perform logout logic (e.g., clear user data from localStorage)
    localStorage.removeItem('loggedInUser');
    setUser(null);
    
    navigate('/');
    window.location.reload();
  };
  return (
    <header className="bg-purple-500 py-4">
      <nav className="container mx-auto flex items-center justify-between">
        <div className="text-white text-2xl font-bold">Your Logo</div>
        <ul className="space-x-4 flex items-center">
          {/* Home link always visible */}
          <li><Link to="/" className="text-white hover:text-purple-300">Home</Link></li>
          {/* Display profile and log-out button if the user is logged in */}
          {user && (
            <>
              <li>
                <Link to="/profile" className="text-white hover:text-purple-300">
                  <img
                    src={user.profilePicture || 'default-profile-pic.jpg'}
                    alt="Profile"
                    className="w-8 h-8 rounded-full object-cover"
                  />
                  Hi, {user.firstName}
                </Link>
              </li>
              <li>
                <button
                  onClick={handleLogout}
                  className="text-white hover:text-purple-300 focus:outline-none"
                >
                  Log Out
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