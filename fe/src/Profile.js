import React, {useEffect, useState} from 'react';
import ProfileForm from './ProfileForm';

function Profile() {
    const [editMode, setEditMode] = useState(false);
    const [userProfile, setUserProfile] = useState({});
    const [originalUserProfile, setOriginalUserProfile] = useState({});

    useEffect(() => {
        const fetchData = async() => {
            const profileGetData = await fetch("https://127.0.0.1:8443/user",
                {
                    credentials: "include",
                    method: "get"
                });

            const profileGotData = await profileGetData.json();
            console.log(profileGotData)

            setUserProfile(profileGotData);
            setOriginalUserProfile(profileGotData);
        }

        fetchData();
    }, []);

    const handleEditToggle = () => {
        setEditMode(!editMode);
    };


    const handleSaveProfile = async (updatedUser) => {
        console.log(updatedUser)

        const resultProfile = await fetch(`https://127.0.0.1:8443/user`, {
            method: 'PUT',
            credentials: "include",
            mode: 'cors',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                first_name: updatedUser.first_name,
                last_name: updatedUser.last_name,
                email: updatedUser.email,
                gender: updatedUser.gender,
                phone: updatedUser.phone
            })
        })

        console.log(updatedUser.profilePicture)

        if (!resultProfile.ok) {
            alert(await resultProfile.text())
            return
        }

        const formData = new FormData();
        formData.append("fileupload", updatedUser.profilePicture);

        const resultPhoto = await fetch(`https://127.0.0.1:8443/user/upload-image`, {
            method: 'POST',
            credentials: "include",
            headers: {
                'Content-Type': 'image/*'
            },
            mode: 'cors',
            body: updatedUser.profilePicture
        })

        if (!resultPhoto.ok) {
            alert(await resultPhoto.text())
            return
        }

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
