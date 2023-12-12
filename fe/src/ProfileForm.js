import React, {useEffect, useState} from 'react';

function ProfileForm({user, onSave, onCancel}) {
    const [editMode, setEditMode] = useState(false);
    const [userProfile, setUserProfile] = useState({...user});
    const [passwordData, setPasswordData] = useState({
        currentPassword: '',
        newPassword: '',
        confirmPassword: '',
    });
    const [passwordError, setPasswordError] = useState(null);
    const [imageError, setImageError] = useState(null);

    const image = "https://127.0.0.1:8443/user/" + JSON.parse(localStorage.getItem('loggedInUser')).id + "/image"

    useEffect(() => {
        setUserProfile({...user});
    }, [user]);

    const handleEditToggle = () => {
        setEditMode(!editMode);
    };

    const handleSaveProfile = () => {
        // Save profile logic (e.g., update user data in parent component)
        onSave(userProfile);
        setEditMode(false);
    };

    const handlePasswordChange = async () => {
        if (passwordData.newPassword === passwordData.confirmPassword) {
            setPasswordError(null);
        } else {
            setPasswordError('Hesla nejsou shodné.');
            return;
        }

        const result = await fetch(`https://127.0.0.1:8443/user/update-password`, {
            method: 'PUT',
            credentials: "include",
            mode: 'cors',
            headers: {
                'Accept': 'application/json',
            },
            body: passwordData.newPassword
        })

        if (!result.ok) {
            alert(await result.text())
        } else {
            console.log(await result.text())
            setPasswordError('Uloženo');
            passwordData.newPassword = ""
            passwordData.confirmPassword = ""
        }
    };

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setUserProfile({...userProfile, [name]: value});
    };

    const handlePasswordInputChange = (e) => {
        const {name, value} = e.target;
        setPasswordData({...passwordData, [name]: value});
    };

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        // Basic validation for image type (you can enhance this as needed)
        if (file && file.type.startsWith('image/')) {
            const reader = new FileReader();
            reader.onload = (event) => {
                setUserProfile({...userProfile, profilePicture: e.target.files[0]});
                setImageError(null);
            };
            reader.readAsDataURL(file);

            console.log(userProfile.profilePicture)
        } else {
            setImageError('Nevalidní obrázek. Prosím vyběrté obrázek.');
        }
    };

    return (
        <div className="max-w-md mx-auto mt-10 p-8 bg-white rounded-lg shadow-lg">
            <h1 className="text-3xl font-semibold mb-4">Vaš profil</h1>
            <div className="grid grid-cols-2 gap-4">
                <div className="mb-4 col-span-2">
                    <label htmlFor="profilePicture" className="block text-sm font-medium text-gray-700">
                        Obrázek profilu
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
                            src={image}
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
                            id="first_name"
                            name="first_name"
                            value={userProfile.first_name}
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
                            id="last_name"
                            name="last_name"
                            value={userProfile.last_name}
                            onChange={handleInputChange}
                            className="w-full px-4 py-2 border rounded-lg"
                            disabled={!editMode}
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="phoneNumber" className="block text-sm font-medium text-gray-700">
                            Telefonní číslo
                        </label>
                        <input
                            type="tel"
                            id="phone"
                            name="phone"
                            value={userProfile.phone}
                            onChange={handleInputChange}
                            className="w-full px-4 py-2 border rounded-lg"
                            disabled={!editMode}
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="phoneNumber" className="block text-sm font-medium text-gray-700">
                            Email
                        </label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={userProfile.email}
                            onChange={handleInputChange}
                            className="w-full px-4 py-2 border rounded-lg"
                            disabled={!editMode}
                        />
                    </div>
                </div>
            </div>
            {/* Display user profile data */}
            <button onClick={handleEditToggle}
                    className="bg-purple-500 hover:bg-purple-600 text-white px-4 py-2 rounded-full">
                {editMode ? 'Storno změn' : 'Změnit'}
            </button>
            {editMode && (
                <div className="mt-4">
                    <button onClick={handleSaveProfile}
                            className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-full mr-4">
                        Uložit profil
                    </button>
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
                                Potvrdit heslo
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
                    <button onClick={handlePasswordChange}
                            className="bg-purple-500 hover:bg-purple-600 text-white px-4 py-2 rounded-full">
                        Změnit heslo
                    </button>
                </div>
            )}
            {/*{onCancel && editMode && (*/}
            {/*    <button onClick={onCancel}*/}
            {/*            className="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-full mt-4">*/}
            {/*        Storno*/}
            {/*    </button>*/}
            {/*)}*/}
        </div>
    );
}

export default ProfileForm;
