import React, { useState, useEffect } from 'react';

function UserDetails({ user, fetchUserData }) {
    const [editedUser, setEditedUser] = useState({
        first_name: user.first_name,
        last_name: user.last_name,
        email: user.email,
        phone: user.phone,
        gender: user.gender,
        role: user.role,
    });
    const [isEditingDetails, setIsEditingDetails] = useState(false);
    const [isChangingPassword, setIsChangingPassword] = useState(false);
    const [userDetails, setUserDetails] = useState(user);
    const [successNotification, setSuccessNotification] = useState(false); // Declare successNotification state
    const [errorNotification, setErrorNotification] = useState(false); // Declare errorNotification state

    useEffect(() => {
        setEditedUser({
            first_name: user.first_name,
            last_name: user.last_name,
            email: user.email,
            phone: user.phone,
            gender: user.gender,
            role: user.role,
        });
    }, [user]);

    const handleEditDetailsClick = () => {
        setIsEditingDetails(true);
    };

    const handleChangePasswordClick = () => {
        setIsChangingPassword(true);
    };

    const handleCancelEdit = () => {
        setIsEditingDetails(false);
    };

    const handleCancelPasswordChange = () => {
        setIsChangingPassword(false);
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setEditedUser({ ...editedUser, [name]: value });
    };

    const handleEditUserDetails = () => {
        if (
            Object.values(editedUser).some((value) => value === null || value.trim() === '')
        ) {
            setErrorNotification(true);
            setTimeout(() => {
                setErrorNotification(false);
            }, 3000);
            return;
        }

        const userId = user.id;
        fetch(`https://127.0.0.1:8443/admin/user/${userId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(editedUser),
            credentials: 'include',
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error('Failed to update user details');
                }
                console.log('User details updated successfully:', editedUser);
                setIsEditingDetails(false);
                setSuccessNotification(true);
                setTimeout(() => {
                    setSuccessNotification(false);
                    fetch(`https://127.0.0.1:8443/admin/user/${userId}`, {
                        credentials: 'include',
                    })
                        .then((response) => {
                            if (!response.ok) {
                                throw new Error('Failed to fetch updated user details');
                            }
                            return response.json();
                        })
                        .then((data) => {
                            setUserDetails(data);
                        })
                        .catch((error) => {
                            console.error('Error fetching updated user details:', error);
                        });
                }, 800);
            })
            .catch((error) => {
                console.error('Error updating user details:', error);
                setErrorNotification(true);
                setTimeout(() => {
                    setErrorNotification(false);
                }, 3000);
            });
    };

    return (
        <div className="bg-white p-8 rounded-lg shadow-lg">
            <div className="mb-4">
                <div className="mb-2">
                    <strong>Email:</strong> {userDetails.email}
                </div>
                <div className="mb-2">
                    <strong>Phone:</strong> {userDetails.phone}
                </div>
                <div className="mb-2">
                    <strong>Gender:</strong> {userDetails.gender}
                </div>
                <div className="mb-2">
                    <strong>Role:</strong> {userDetails.role}
                </div>
            </div>
            <div className="mt-4">
                <button
                    className="bg-blue-500 text-white px-4 py-2 rounded-lg mr-2"
                    onClick={handleEditDetailsClick}
                >
                    Upravit Detaily
                </button>
                <button
                    className="bg-red-500 text-white px-4 py-2 rounded-lg"
                    onClick={handleChangePasswordClick}
                >
                    Změnit Heslo
                </button>
            </div>
            {isEditingDetails && (
                <div className="mt-4">
                    <form>
                        <div className="mb-2">
                            <label htmlFor="first_name" className="block font-medium">
                                Křestní Jméno:
                            </label>
                            <input
                                type="text"
                                id="first_name"
                                name="first_name"
                                value={editedUser.first_name}
                                onChange={handleInputChange}
                                className="border border-gray-300 rounded-md px-3 py-2 w-full"
                            />
                        </div>
                        <div className="mb-2">
                            <label htmlFor="last_name" className="block font-medium">
                                Příjmení:
                            </label>
                            <input
                                type="text"
                                id="last_name"
                                name="last_name"
                                value={editedUser.last_name}
                                onChange={handleInputChange}
                                className="border border-gray-300 rounded-md px-3 py-2 w-full"
                            />
                        </div>
                        <div className="mb-2">
                            <label htmlFor="email" className="block font-medium">
                                Email:
                            </label>
                            <input
                                type="email"
                                id="email"
                                name="email"
                                value={editedUser.email}
                                onChange={handleInputChange}
                                className="border border-gray-300 rounded-md px-3 py-2 w-full"
                            />
                        </div>
                        <div className="mb-2">
                            <label htmlFor="phone" className="block font-medium">
                                Telefon:
                            </label>
                            <input
                                type="text"
                                id="phone"
                                name="phone"
                                value={editedUser.phone}
                                onChange={handleInputChange}
                                className="border border-gray-300 rounded-md px-3 py-2 w-full"
                            />
                        </div>
                        <div className="mb-2">
                            <label htmlFor="gender" className="block font-medium">
                                Pohlaví:
                            </label>
                            <input
                                type="text"
                                id="gender"
                                name="gender"
                                value={editedUser.gender}
                                onChange={handleInputChange}
                                className="border border-gray-300 rounded-md px-3 py-2 w-full"
                            />
                        </div>
                        <div className="mb-2">
                            <label htmlFor="role" className="block font-medium">
                                Role:
                            </label>
                            <input
                                type="text"
                                id="role"
                                name="role"
                                value={editedUser.role}
                                onChange={handleInputChange}
                                className="border border-gray-300 rounded-md px-3 py-2 w-full"
                            />
                        </div>
                        <div className="mt-4">
                            <button
                                type="button"
                                onClick={handleEditUserDetails}
                                className="bg-blue-500 text-white px-4 py-2 rounded-md"
                            >
                                Uložit
                            </button>
                            <button
                                type="button"
                                onClick={handleCancelEdit}
                                className="bg-gray-400 text-white px-4 py-2 rounded-md ml-2"
                            >
                                Zrušit
                            </button>
                        </div>
                    </form>
                </div>
            )}
            {successNotification && (
                <div className="mt-4 bg-green-200 text-green-700 px-4 py-2 rounded-md">
                    Aktualizace proběhla úspěšně!
                </div>
            )}
            {errorNotification && (
                <div className="mt-4 bg-red-200 text-red-700 px-4 py-2 rounded-md">
                    Aktualizace se nezdařila. Zkuste to prosím znovu.
                </div>
            )}
        </div>
    );
}

export default UserDetails;
