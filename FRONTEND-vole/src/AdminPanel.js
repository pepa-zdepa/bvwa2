import React, { useEffect, useState } from 'react';
import ProfileForm from './ProfileForm';

function AdminPanel() {
  const [users, setUsers] = useState(JSON.parse(sessionStorage.getItem('registeredUsers')) || []);
  const [bannedUsers, setBannedUsers] = useState(JSON.parse(sessionStorage.getItem('bannedUsers')) || []);
  const [selectedUser, setSelectedUser] = useState(null);

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
      <h1 className="text-3xl font-semibold mb-4">Admin Panel</h1>

      {/* Display active users */}
      <div className="mb-6">
        <h2 className="text-xl font-semibold mb-2">Active Users</h2>
        <ul>
          {users.map(user => (
            <li key={user.email} className="flex items-center justify-between border-b py-2">
              <div>{user.email}
              {user.isAdmin && <span className="ml-2 text-sm text-green-500">Admin</span>}
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
                  Emulate
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

      {/* Display banned users */}
      <div>
        <h2 className="text-xl font-semibold mb-2">Banned Users</h2>
        <ul>
          {bannedUsers.map(user => (
            <li key={user.email} className="flex items-center justify-between border-b py-2">
              <div className="italic text-red-500">{user.email} (Banned)</div>
              <div>
                <button
                  onClick={() => handleUnbanUser(user.email)}
                  className="bg-green-500 hover:bg-green-600 text-white px-2 py-1 rounded-full mr-2"
                >
                  Unban
                </button>
              </div>
            </li>
          ))}
        </ul>
      </div>

      {/* Emulated Profile Form */}
      {selectedUser && (
        <div className="mt-6">
          <h2 className="text-xl font-semibold mb-2">Emulated User Profile</h2>
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
