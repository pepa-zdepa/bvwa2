    import React, { useEffect, useState } from 'react';
    import UserDetails from './UserDetails';
    import apiUrl from "./Url"; // Import the UserDetails component

    function AdminPanel() {
      const [users, setUsers] = useState([]);
      const [selectedUser, setSelectedUser] = useState(null);

        useEffect(() => {
            // Fetch user data from the API on component mount
            fetchUserData();
        }, []);
        const fetchUserData = () => {
            // Fetch user data from the API
            fetch(`${apiUrl}/admin/users`, {
                credentials: 'include',
            })
                .then((response) => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then((data) => {
                    setUsers(data); // Update the users state with the refreshed data
                })
                .catch((error) => {
                    console.error('Error fetching user data:', error);
                });
        };


      const handleUserClick = (user) => {
        setSelectedUser(user);
      };

      return (
          <div className="max-w-md mx-auto mt-10 p-8 bg-white rounded-lg shadow-lg">
            <h1 className="text-3xl font-semibold mb-4">Panel Adminu</h1>

            {/* Display active users */}
            <div className="mb-6">
              <h2 className="text-xl font-semibold mb-2">Uživatele</h2>
              <ul>
                {users.map((user) => (
                    <li
                        key={user.id}
                        onClick={() => handleUserClick(user)} // Handle user click
                        className="flex items-center justify-between border-b py-2 cursor-pointer"
                    >
                      <div>
                        {user.first_name} {user.last_name}
                        {user.role === 'admin' && (
                            <span className="ml-2 text-sm text-green-500">Admin</span>
                        )}
                      </div>
                    </li>
                ))}
              </ul>
            </div>

            {/* UserDetails component */}
              {selectedUser && (
                  <UserDetails user={selectedUser} fetchUserData={fetchUserData} />
              )}
          </div>
      );
    }

    export default AdminPanel;
