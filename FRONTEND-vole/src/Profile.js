import React, { useState, useEffect } from 'react';
import ProfileForm from './ProfileForm';

function Profile({ loggedInUser }) {
  const [editMode, setEditMode] = useState(false);
  const [userProfile, setUserProfile] = useState({ ...loggedInUser });
  const [originalUserProfile, setOriginalUserProfile] = useState({ ...loggedInUser });

  useEffect(() => {
    setUserProfile({ ...loggedInUser });
    setOriginalUserProfile({ ...loggedInUser });
  }, [loggedInUser]);

  const handleEditToggle = () => {
    setEditMode(!editMode);
  };

  const handleSaveProfile = (updatedUser) => {
    const storedUsers = JSON.parse(localStorage.getItem('registeredUsers')) || [];
    const updatedUsers = storedUsers.map((u) => (u.email === loggedInUser.email ? updatedUser : u));
    localStorage.setItem('registeredUsers', JSON.stringify(updatedUsers));

    setEditMode(false);
    setUserProfile(updatedUser);
  };

  const handleCancelEdit = () => {
    setUserProfile(originalUserProfile);
    setEditMode(false);
  };

  return (
    <div>
      <ProfileForm
        user={userProfile}
        onEditToggle={handleEditToggle}
        onSave={handleSaveProfile}
        onCancel={handleCancelEdit}
        editMode={editMode}
      />
    </div>
  );
}

export default Profile;
