import React, { useState, useEffect } from 'react';
import ProfileForm from './ProfileForm';
import API_URLS from './config';

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

  const handleSaveProfile = async (updatedUser) => {
    try {
      const response = await fetch(API_URLS.UPDATE_PROFILE, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${loggedInUser.token}`,
        },
        body: JSON.stringify(updatedUser),
      });

      if (response.ok) {
        setUserProfile(updatedUser);
        setOriginalUserProfile(updatedUser);
        setEditMode(false);
      } else {
        // Handle error, e.g., invalid token
        console.error('Error updating user profile:', response.statusText);
      }
    } catch (error) {
      console.error('Error updating user profile:', error);
    }
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
