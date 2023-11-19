import React, { useState, useEffect } from 'react';
import InputValidator from './InputValidator'; // Import the utility

function Profile({ loggedInUser }) {
  const [editMode, setEditMode] = useState(false);
  const [userProfile, setUserProfile] = useState({ ...loggedInUser });
  const [passwordData, setPasswordData] = useState({
    currentPassword: '',
    newPassword: '',
    confirmPassword: '',
  });
  const [passwordError, setPasswordError] = useState(null);
  const [imageError, setImageError] = useState(null);

  useEffect(() => {
    setUserProfile({ ...loggedInUser });
  }, [loggedInUser]);

  const handleEditToggle = () => {
    setEditMode(!editMode);
  };

  const handleSaveProfile = () => {
    // Save profile logic (e.g., update user data in localStorage or send to server)
    const storedUsers = JSON.parse(localStorage.getItem('registeredUsers')) || [];
    const updatedUsers = storedUsers.map((u) => (u.email === loggedInUser.email ? userProfile : u));
    localStorage.setItem('registeredUsers', JSON.stringify(updatedUsers));
    
    
    window.location.reload();
    setEditMode(false);
  };

  const handlePasswordChange = () => {
    // Password change logic (e.g., update password in localStorage or send to server)
    const storedUsers = JSON.parse(localStorage.getItem('registeredUsers')) || [];
    const user = storedUsers.find((u) => u.email === loggedInUser.email);

    if (user.password === passwordData.currentPassword) {
      if (passwordData.newPassword === passwordData.confirmPassword) {
        user.password = passwordData.newPassword;
        const updatedUsers = storedUsers.map((u) => (u.email === loggedInUser.email ? user : u));
        localStorage.setItem('registeredUsers', JSON.stringify(updatedUsers));
        setPasswordError(null);
      } else {
        setPasswordError('New password and confirm password do not match');
      }
    } else {
      setPasswordError('Current password is incorrect');
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

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    // Basic validation for image type (you can enhance this as needed)
    if (file && file.type.startsWith('image/')) {
      const reader = new FileReader();
      reader.onload = (event) => {
        setUserProfile({ ...userProfile, profilePicture: event.target.result });
        setImageError(null);
      };
      reader.readAsDataURL(file);
    } else {
      setImageError('Invalid image file. Please choose a valid image.');
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-8 bg-white rounded-lg shadow-lg">
      <h1 className="text-3xl font-semibold mb-4">Your Profile</h1>
      <div className="grid grid-cols-2 gap-4">
        <div className="mb-4 col-span-2">
          <label htmlFor="profilePicture" className="block text-sm font-medium text-gray-700">
            Profile Picture
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
              First Name
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
              Last Name
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
              Phone Number
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
              Date of Birth
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
            Save Profile
          </button>
          {/* Password Change Section */}
          <div className="mb-4">
            <label htmlFor="currentPassword" className="block text-sm font-medium text-gray-700">
              Current Password
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
                New Password
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
                Confirm Password
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
            Change Password
          </button>
        </div>
      )}
    </div>
  );
}

export default Profile;
