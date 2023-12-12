import React, { useState, useEffect } from 'react';

function ProfileForm({ user, onSave, onCancel }) {
  const [editMode, setEditMode] = useState(false);
  const [userProfile, setUserProfile] = useState({ ...user });
  const [passwordData, setPasswordData] = useState({
    currentPassword: '',
    newPassword: '',
    confirmPassword: '',
  });
  const [passwordError, setPasswordError] = useState(null);
  const [imageError, setImageError] = useState(null);

  useEffect(() => {
    setUserProfile({ ...user });
  }, [user]);

  const handleEditToggle = () => {
    setEditMode(!editMode);
  };

  const handleSaveProfile = async () => {
    try {
      const response = await fetch(API_URLS.UPDATE_USER, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${user.token}`,
        },
        body: JSON.stringify(userProfile),
      });

      if (response.ok) {
        onSave(userProfile);
        setEditMode(false);
      } else {
        console.error('Error updating user profile:', response.statusText);
        // Handle error response if needed
      }
    } catch (error) {
      console.error('Error updating user profile:', error);
      // Handle network or other errors if needed
    }
  };

  const handlePasswordChange = async () => {
    try {
      const response = await fetch(API_URLS.CHANGE_PASSWORD, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${user.token}`,
        },
        body: JSON.stringify({
          currentPassword: passwordData.currentPassword,
          newPassword: passwordData.newPassword,
        }),
      });

      if (response.ok) {
        setPasswordError(null);
        onSave({ ...userProfile, password: passwordData.newPassword });
      } else {
        const errorData = await response.json();
        setPasswordError(errorData.message);
      }
    } catch (error) {
      console.error('Error changing password:', error);
      // Handle network or other errors if needed
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserProfile({ ...userProfile, [name]: value });
  };

  const handlePasswordInputChange = (e) => {
    const { name, value } = e.target;
    setPasswordData({ ...passwordData, [name]: value });
  };

  const handleImageChange = async (e) => {
    const file = e.target.files[0];

    if (file && file.type.startsWith('image/')) {
      try {
        const formData = new FormData();
        formData.append('image', file);

        const response = await fetch(API_URLS.UPLOAD_IMAGE, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${user.token}`,
          },
          body: formData,
        });

        if (response.ok) {
          const imageData = await response.json();
          setUserProfile({ ...userProfile, profilePicture: imageData.imagePath });
          setImageError(null);
        } else {
          const errorData = await response.json();
          setImageError(errorData.message);
        }
      } catch (error) {
        console.error('Error uploading image:', error);
        // Handle network or other errors if needed
      }
    } else {
      setImageError('Nespravný format obrázovky.');
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-8 bg-white rounded-lg shadow-lg">
      <h1 className="text-3xl font-semibold mb-4">Your Profile</h1>
      <div className="grid grid-cols-2 gap-4">
        <div className="mb-4 col-span-2">
          <label htmlFor="profilePicture" className="block text-sm font-medium text-gray-700">
            Obrázovka uživatele
          </label>
          {editMode ? (
            <input
              type="file"
              id="profilePicture"
              name="profilePicture"
              accept="image/*"
              onChange={handleImageChange}
              className="w-full px-4 py-2 border rounded-lg"
            />
          ) : (
            <img
              src={userProfile.profilePicture}
              alt="Profile"
              className="w-16 h-16 rounded-full object-cover"
            />
          )}
          {imageError && <p className="text-red-500 mt-2">{imageError}</p>}
        </div>
        {/* Other form fields for editing profile */}
        <div className="col-span-2">
          <div className="mb-4">
            <label htmlFor="firstName" className="block text-sm font-medium text-gray-700">
              Jmeno
            </label>
            <input
              type="text"
              id="firstName"
              name="firstName"
              value={userProfile.firstName}
              onChange={handleInputChange}
              className="w-full px-4 py-2 border rounded-lg"
              disabled={!editMode}
            />
          </div>
          <div className="mb-4">
            <label htmlFor="lastName" className="block text-sm font-medium text-gray-700">
              Přijmeni
            </label>
            <input
              type="text"
              id="lastName"
              name="lastName"
              value={userProfile.lastName}
              onChange={handleInputChange}
              className="w-full px-4 py-2 border rounded-lg"
              disabled={!editMode}
            />
          </div>
          <div className="mb-4">
            <label htmlFor="phoneNumber" className="block text-sm font-medium text-gray-700">
              Telefonni Číslo
            </label>
            <input
              type="tel"
              id="phoneNumber"
              name="phoneNumber"
              value={userProfile.phoneNumber}
              onChange={handleInputChange}
              className="w-full px-4 py-2 border rounded-lg"
              disabled={!editMode}
            />
          </div>
          <div className="mb-4">
            <label htmlFor="dateOfBirth" className="block text-sm font-medium text-gray-700">
              Datum Narození
            </label>
            <input
              type="date"
              id="dateOfBirth"
              name="dateOfBirth"
              value={userProfile.dateOfBirth || ''}
              onChange={handleInputChange}
              className="w-full px-4 py-2 border rounded-lg"
              disabled={!editMode}
            />
          </div>
        </div>
      </div>
      {/* Display user profile data */}
      <button onClick={handleEditToggle} className="bg-purple-500 hover:bg-purple-600 text-white px-4 py-2 rounded-full">
        {editMode ? 'Cancel Edit' : 'Edit Profile'}
      </button>
      {editMode && (
        <div className="mt-4">
          <button onClick={handleSaveProfile} className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-full mr-4">
            Uložít profil
          </button>
          {/* Password Change Section */}
          <div className="mb-4">
            <label htmlFor="currentPassword" className="block text-sm font-medium text-gray-700">
              Aktuální heslo
            </label>
            <input
              type="password"
              id="currentPassword"
              name="currentPassword"
              value={passwordData.currentPassword}
              onChange={handlePasswordInputChange}
              className="w-full px-4 py-2 border rounded-lg"
              required
            />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div className="mb-4">
              <label htmlFor="newPassword" className="block text-sm font-medium text-gray-700">
                Nové heslo
              </label>
              <input
                type="password"
                id="newPassword"
                name="newPassword"
                value={passwordData.newPassword}
                onChange={handlePasswordInputChange}
                className="w-full px-4 py-2 border rounded-lg"
                required
              />
            </div>
            <div className="mb-4">
              <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-700">
                Potvrdit nové heslo
              </label>
              <input
                type="password"
                id="confirmPassword"
                name="confirmPassword"
                value={passwordData.confirmPassword}
                onChange={handlePasswordInputChange}
                className="w-full px-4 py-2 border rounded-lg"
                required
              />
            </div>
          </div>
          {passwordError && <p className="text-red-500 mb-4">{passwordError}</p>}
          <button onClick={handlePasswordChange} className="bg-purple-500 hover:bg-purple-600 text-white px-4 py-2 rounded-full">
            Změnit heslo
          </button>
        </div>
      )}
      {onCancel && editMode && (
        <button onClick={onCancel} className="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-full mt-4">
          Strono
        </button>
      )}
    </div>
  );
}

export default ProfileForm;
