import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import InputValidator from './InputValidator'; // Import the utility

function Register() {
  const navigate = useNavigate();
  const [userProfile, setUserProfile] = useState({
    email: '',
    password: '',
    firstName: '',
    lastName: '',
    phoneNumber: '',    
    confirmPassword: '',
    dateOfBirth: null,
    profilePicture: null,
    isAdmin: false,
  });

  const [passwordError, setPasswordError] = useState(null);
  const [phoneNumberError, setPhoneNumberError] = useState(null);
  const [emailError, setEmailError] = useState(null);

  const handleFormSubmit = (e) => {
    e.preventDefault();

    // Validate password
    if (!InputValidator.isPasswordValid(userProfile.password)) {
      setPasswordError(
        'Password must be at least 10 characters long and include at least one capital letter, one number, and one special character.'
      );
      return;
    }

    // Validate phone number
    if (!InputValidator.isPhoneNumberValid(userProfile.phoneNumber)) {
      setPhoneNumberError('Invalid phone number. Please enter a valid phone number without spaces.');
      return;
    }

    // Validate email
    if (!InputValidator.isEmailValid(userProfile.email)) {
      setEmailError('Invalid email. Please enter a valid email address.');
      return;
    }

    // Registration logic here (e.g., send data to the server)
    const storedUsers = JSON.parse(localStorage.getItem('registeredUsers')) || [];
    const isUserExists = storedUsers.some((u) => u.email === userProfile.email);

    if (!isUserExists) {
      storedUsers.push(userProfile);
      localStorage.setItem('registeredUsers', JSON.stringify(storedUsers));    
      navigate('/'); // Redirect to home/login page after registration
      window.location.reload();
    } else {
      // Display error if user already exists
      alert('User with this email already exists. Please log in.');
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserProfile({ ...userProfile, [name]: value });
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-8 bg-white rounded-lg shadow-lg">
      <h1 className="text-3xl font-semibold text-center mb-4">Register</h1>
      <form onSubmit={handleFormSubmit}>
        {/* Email */}
        <div className="mb-4">
          <label htmlFor="email" className="block text-sm font-medium text-gray-700">
            Email
          </label>
          <input
            type="email"
            id="email"
            name="email"
            value={userProfile.email}
            onChange={handleInputChange}
            className="w-full px-4 py-2 border rounded-lg"
            required
          />
          {/* Display email error if any */}
          {emailError && <p className="text-red-500 mt-1">{emailError}</p>}
        </div>
        {/* Password */}
        <div className="mb-4">
          <label htmlFor="password" className="block text-sm font-medium text-gray-700">
            Password
          </label>
          <input
            type="password"
            id="password"
            name="password"
            value={userProfile.password}
            onChange={handleInputChange}
            className="w-full px-4 py-2 border rounded-lg"
            required
          />
          {/* Display password error if any */}
          {passwordError && <p className="text-red-500 mt-1">{passwordError}</p>}
        </div>
        {/* First Name */}
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
            required
          />
        </div>
        {/* Last Name */}
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
            required
          />
        </div>        
        {/* Phone Number */}
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
            required
          />
          {/* Display phone number error if any */}
          {phoneNumberError && <p className="text-red-500 mt-1">{phoneNumberError}</p>}
        </div>        
        {/* Confirm Password */}
        <div className="mb-4">
          <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-700">
            Confirm Password
          </label>
          <input
            type="password"
            id="confirmPassword"
            name="confirmPassword"
            value={userProfile.confirmPassword}
            onChange={handleInputChange}
            className="w-full px-4 py-2 border rounded-lg"
            required
          />
        </div>
        {/* Submit Button */}
        <button
          type="submit"
          className="bg-purple-500 hover:bg-purple-600 text-white px-4 py-2 rounded-full w-full"
        >
          Register
        </button>
      </form>
    </div>
  );
}

export default Register;
