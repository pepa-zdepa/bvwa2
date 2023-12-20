import React, {useEffect, useState} from 'react';
import ProfileForm from './ProfileForm';
import apiUrl from "./Url";

function Profile() {
    const [editMode, setEditMode] = useState(false);
    const [userProfile, setUserProfile] = useState({});
    const [originalUserProfile, setOriginalUserProfile] = useState({});

    const fetchData = () => {
        fetch("${apiUrl}/user",
            {
                credentials: "include",
                method: "get"
            }).then(response => {
            return response.json()
        }).then(data => {
            setUserProfile(data);
            setOriginalUserProfile(data);
        });
    }

    useEffect(() => {
        fetchData();
    }, []);

    const handleEditToggle = () => {
        setEditMode(!editMode);
    };
    const handleSaveProfile = async (updatedUser) => {
        const resultProfile = await fetch(`${apiUrl}/user`, {
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

        if (!resultProfile.ok) {
            alert(await resultProfile.text())
            return
        }

        const formData = new FormData();
        formData.append("fileupload", updatedUser.profilePicture);

        const resultPhoto = await fetch(`${apiUrl}/user/upload-image`, {
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
