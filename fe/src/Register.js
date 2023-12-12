import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import InputValidator from './InputValidator'; // Import the utility

function Register() {
    const navigate = useNavigate();
    const [userProfile, setUserProfile] = useState({
        first_name: '',
        last_name: '',
        email: '',
        nickname: '',
        gender: '',
        phone: '',
        confirmPassword: '',
        password: ''
    });

    const [passwordError, setPasswordError] = useState(null);
    const [phoneNumberError, setPhoneNumberError] = useState(null);
    const [emailError, setEmailError] = useState(null);

    const handleFormSubmit = async (e) => {
        e.preventDefault();

        // Validate password
        if (!InputValidator.isPasswordValid(userProfile.password)) {
            setPasswordError(
                'Password must be at least 10 characters long and include at least one capital letter, one number, and one special character.'
            );
            return;
        }

        // Validate phone number
        if (!InputValidator.isPhoneNumberValid(userProfile.phone)) {
            setPhoneNumberError('Invalid phone number. Please enter a valid phone number without spaces.');
            return;
        }

        // Validate email
        if (!InputValidator.isEmailValid(userProfile.email)) {
            setEmailError('Invalid email. Please enter a valid email address.');
            return;
        }

        // Registration logic here
        const storedUsers = JSON.parse(localStorage.getItem('registeredUsers')) || [];
        const isUserExists = await fetch(`https://127.0.0.1:8443/auth/check-username-available?username=${userProfile.nickname}`)
        const isUserExists_data = await isUserExists.text();

        console.log(isUserExists_data);

        if (isUserExists_data == '1') {
            localStorage.setItem('registeredUsers', JSON.stringify(storedUsers));
            const registerUser = await fetch(`https://127.0.0.1:8443/user`, {
                method: 'POST',
                credentials: "include",
                mode: 'cors',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json', //fa@425gWT45w5!3
                },
                body: JSON.stringify({
                    first_name: userProfile.first_name,
                    last_name: userProfile.last_name,
                    email: userProfile.email,
                    user: userProfile.nickname,
                    gender: userProfile.gender,
                    phone: userProfile.phone,
                    password: userProfile.password
                })
            })
            const registerUser_data = await registerUser.text()
            if (registerUser_data == '') {
                navigate('/'); // Redirect to home/login page after registration
                window.location.reload();
            } else {
                // Display error if user already exists
                alert('Uživatel s tímto loginem jíž existuje.');
            }
        }
    };

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setUserProfile({...userProfile, [name]: value});
    };

    return (
        <div className="max-w-md mx-auto mt-10 p-8 bg-white rounded-lg shadow-lg">
            <h1 className="text-3xl font-semibold text-center mb-4">Registrace</h1>
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
                        Heslo
                    </label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={userProfile.password}
                        onChange={handleInputChange} //f0rgott3N!
                        className="w-full px-4 py-2 border rounded-lg"
                        required
                    />
                    {/* Display password error if any */}
                    {passwordError && <p className="text-red-500 mt-1">{passwordError}</p>}
                </div>
                {/* Login */}
                <div className="mb-4">
                    <label htmlFor="nickname" className="block text-sm font-medium text-gray-700">
                        Login
                    </label>
                    <input
                        type="text"
                        id="nickname"
                        name="nickname"
                        value={userProfile.nickname}
                        onChange={handleInputChange}
                        className="w-full px-4 py-2 border rounded-lg"
                        required
                    />
                </div>
                {/* First Name */}
                <div className="mb-4">
                    <label htmlFor="firstName" className="block text-sm font-medium text-gray-700">
                        Jmeno
                    </label>
                    <input
                        type="text"
                        id="first_name"
                        name="first_name"
                        value={userProfile.first_name}
                        onChange={handleInputChange}
                        className="w-full px-4 py-2 border rounded-lg"
                        required
                    />
                </div>
                {/* Last Name */}
                <div className="mb-4">
                    <label htmlFor="lastName" className="block text-sm font-medium text-gray-700">
                        Přijmeni
                    </label>
                    <input
                        type="text"
                        id="last_name"
                        name="last_name"
                        value={userProfile.last_name}
                        onChange={handleInputChange}
                        className="w-full px-4 py-2 border rounded-lg"
                        required
                    />
                </div>
                {/* Gender */}
                <div className="mb-4">
                    <label htmlFor="nickname" className="block text-sm font-medium text-gray-700">
                        Pohlaví
                    </label>
                    <input
                        type="text"
                        id="gender"
                        name="gender"
                        value={userProfile.gender}
                        onChange={handleInputChange}
                        className="w-full px-4 py-2 border rounded-lg"
                        required
                    />
                </div>
                {/* Phone Number */}
                <div className="mb-4">
                    <label htmlFor="phone" className="block text-sm font-medium text-gray-700">
                        Telefonní číslo
                    </label>
                    <input
                        type="tel"
                        id="phone"
                        name="phone"
                        value={userProfile.phone}
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
                        Potvrdit heslo
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
                    Zaregistrovat
                </button>
            </form>
        </div>
    );
}

export default Register;
