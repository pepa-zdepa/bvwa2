import React, { useEffect, useState } from 'react';
import ProfileForm from './ProfileForm';
import API_URLS from './config';

function AdminPanel() {
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);

  
  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await fetch(API_URLS.GET_ALL_ADMIN_USERS, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${loggedInUser.token}`,
          },
        });

        if (response.ok) {
          const userData = await response.json();
          setUsers(userData);
        } else {
          // Handle error, e.g., invalid token
          console.error('Error fetching users:', response.statusText);
        }
      } catch (error) {
        console.error('Error fetching users:', error);
      }
    };
    fetchUsers();
  }, [loggedInUser.token]);

  const handleBanUser = (id) => {
    const shouldDelete = window.confirm(`Opravdu chcete zrušit účet uživatele s loginem: ${nickname}?`);

    if (shouldDelete) {
      // Make a request to delete the user
      fetch(`${API_URLS.DELETE_ADMIN_USER}/${updatedUser.id}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${loggedInUser.token}`,
        },
      })
        .then((response) => {
          if (response.ok) {
            // Update the local state after successful deletion
            setUsers((prevUsers) => prevUsers.filter((user) => user.nickname !== nickname));

            // Display a success message or perform other actions
            alert(`Uživatel s loginem ${nickname} deleted successfully.`);
          } else {
            // Handle error response
            console.error('Error deleting user:', response.statusText);
            alert(`Error deleting user: ${response.statusText}`);
          }
        })
        .catch((error) => {
          // Handle network or other errors
          console.error('Error deleting user:', error);
          alert('Error deleting user. Please try again.');
        });
    }
  };

  const handleEmulateUser = (nickname) => {
    const userToEmulate = users.find((user) => user.nickname === nickname);
    setSelectedUser(userToEmulate);
  };

  const handleSaveEmulatedProfile = async (updatedUser) => {
    try {
      const response = await fetch(`${API_URLS.UPDATE_ADMIN_USER}/${updatedUser.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${loggedInUser.token}`,
        },
        body: JSON.stringify(updatedUser),
      });

      if (response.ok) {
        const updatedUsers = users.map((u) => (u.id === updatedUser.id ? updatedUser : u));
        setUsers(updatedUsers);
        setSelectedUser(null);
      } else {
        // Handle error, e.g., invalid token
        console.error('Chyba obnovění profilu uživatele:', response.statusText);
      }
    } catch (error) {
      console.error('Error updating user profile:', error);
    }
  };

  const handleToggleAdmin = async (id) => {
    try {
      const response = await fetch(`${API_URLS.UPDATE_ADMIN_USER_ROLE}/${id}`, {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${loggedInUser.token}`,
        },
      });

      if (response.ok) {
        const updatedUsers = users.map((user) => {
          if (user.id === id) {
            return { ...user, role: user.role === 'user' ? 'admin' : 'user' };
          }
          return user;
        });

        setUsers(updatedUsers);
      } else {
        // Handle error, e.g., invalid token
        console.error('Error toggling admin:', response.statusText);
      }
    } catch (error) {
      console.error('Error toggling admin:', error);
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-8 bg-white rounded-lg shadow-lg">
      <h1 className="text-3xl font-semibold mb-4">Panel administratoru</h1>

      {/* Display active users */}
      <div className="mb-6">
        <h2 className="text-xl font-semibold mb-2">Uživately</h2>
        <ul>
          {users.map((user) => (
            <li key={user.email} className="flex items-center justify-between border-b py-2">
              <div>
                {user.email}
                {user.isAdmin && (
                  <span className="ml-2 text-sm text-green-500">Admin</span>
                )}
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
                  Emulace uživatele
                </button>
                <button
                  onClick={() => handleToggleAdmin(user.email)}
                  className={`bg-purple-500 hover:bg-purple-600 text-white px-2 py-1 rounded-full mr-2 ${
                    user.isAdmin ? 'opacity-50 cursor-not-allowed' : ''
                  }`}
                  disabled={user.isAdmin}
                >
                  {user.isAdmin ? 'Admin' : 'Změna na Admina'}
                </button>
              </div>
            </li>
          ))}
        </ul>
      </div>      
      {/* Emulated Profile Form */}
      {selectedUser && (
        <div className="mt-6">
          <h2 className="text-xl font-semibold mb-2">Emulováný profil uživatelu</h2>
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
