import React, { useEffect, useState } from 'react';
import ProfileForm from './ProfileForm';

function AdminPanel() {
  const [users, setUsers] = useState(JSON.parse(sessionStorage.getItem('registeredUsers')) || []);
  const [bannedUsers, setBannedUsers] = useState(JSON.parse(sessionStorage.getItem('bannedUsers')) || []);
  const [selectedUser, setSelectedUser] = useState(null);
  const loggedInUser = JSON.parse(localStorage.getItem('loggedInUser')) || null;  

  useEffect(() => {
    const storedUsers = JSON.parse(localStorage.getItem('registeredUsers')) || [];
    const activeUsers = storedUsers.filter(user => !bannedUsers.find(bannedUser => bannedUser.email === user.email));
    setUsers(activeUsers);
  }, [bannedUsers]);

  const handleBanUser = (email) => {
    setUsers((prevUsers) => {
      const userToBan = prevUsers.find((user) => user.email === email);
      const updatedUsers = prevUsers.filter((user) => user.email !== email);
      const updatedBannedUsers = [...bannedUsers, userToBan];
      sessionStorage.setItem('bannedUsers', JSON.stringify(updatedBannedUsers));
      
      // Update localStorage as well
      localStorage.setItem('registeredUsers', JSON.stringify(updatedUsers));
  
      return updatedUsers;
    });
  };

  // Check if the logged-in user is an admin
  if (!loggedInUser || loggedInUser.role !== 'admin') {
    // Redirect or show a message for non-admin users
    return (
      <div className="max-w-md mx-auto mt-10 p-8 bg-white rounded-lg shadow-lg">
        <h1 className="text-3xl font-semibold mb-4">Panel Adminu</h1>
        <p className="text-red-500">Přístup odepřen. Pouze pro adminy.</p>
      </div>
    );
  }
  
  const handleUnbanUser = (email) => {
    setBannedUsers((prevBannedUsers) => {
      const userToUnban = prevBannedUsers.find((user) => user.email === email);
      const updatedBannedUsers = prevBannedUsers.filter((user) => user.email !== email);
      const updatedUsers = [...users, userToUnban];
      sessionStorage.setItem('bannedUsers', JSON.stringify(updatedBannedUsers));
      
      // Update localStorage as well
      localStorage.setItem('registeredUsers', JSON.stringify(updatedUsers));
  
      return updatedBannedUsers;
    });
  };

  const handleEmulateUser = (email) => {
    const userToEmulate = users.find(user => user.email === email);
    setSelectedUser(userToEmulate);
  };

  const handleSaveEmulatedProfile = (updatedUser) => {
    const updatedUsers = users.map(u => (u.email === updatedUser.email ? updatedUser : u));
    setUsers(updatedUsers);
  
    setSelectedUser(null);
  
    localStorage.setItem('registeredUsers', JSON.stringify(updatedUsers));
  };

  const handleToggleAdmin = (email) => {
    const updatedUsers = users.map(user => {
      if (user.email === email) {
        return { ...user, isAdmin: !user.isAdmin };
      }
      return user;
    });
  
    setUsers(updatedUsers);
    localStorage.setItem('registeredUsers', JSON.stringify(updatedUsers));
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-8 bg-white rounded-lg shadow-lg">
      <h1 className="text-3xl font-semibold mb-4">Panel Adminu</h1>

      {/* Display active users */}
      <div className="mb-6">
        <h2 className="text-xl font-semibold mb-2">Uživatele</h2>
        <ul>
          {users.map(user => (
            <li key={user.email} className="flex items-center justify-between border-b py-2">
              <div>{user.email}
              {user.role === 'admin' && <span className="ml-2 text-sm text-green-500">Admin</span>}
              </div>
              <div>
                <button
                  onClick={() => handleBanUser(user.email)}
                  className="bg-red-500 hover:bg-red-600 text-white px-2 py-1 rounded-full mr-2"
                >
                  Ban
                </button>
                <button
                  onClick={() => handleEmulateUser(user.email)}
                  className="bg-blue-500 hover:bg-blue-600 text-white px-2 py-1 rounded-full"
                >
                  Zemulovat
                </button>
                <button
        onClick={() => handleToggleAdmin(user.email)}
        className={`bg-purple-500 hover:bg-purple-600 text-white px-2 py-1 rounded-full mr-2 ${user.isAdmin ? 'opacity-50 cursor-not-allowed' : ''}`}
        disabled={user.isAdmin}
      >
        {user.isAdmin ? 'Admin' : 'Make Admin'}
      </button>
              </div>
            </li>
          ))}
        </ul>
      </div>      
      {/* Emulated Profile Form */}
      {selectedUser && (
        <div className="mt-6">
          <h2 className="text-xl font-semibold mb-2">Zemulováný profil uživatele</h2>
          <ProfileForm
            user={selectedUser}
            onSave={handleSaveEmulatedProfile}
            onCancel={() => setSelectedUser(null)}
            editMode={true}
          />
        </div>
      )}
    </div>
  );
}

export default AdminPanel;
