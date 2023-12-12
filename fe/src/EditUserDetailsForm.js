import React, { useState } from 'react';

function EditUserDetailsForm({ user, onSave, onCancel }) {
    const [editedUser, setEditedUser] = useState(user);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setEditedUser({ ...editedUser, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSave(editedUser);
    };

    return (
        <form onSubmit={handleSubmit}>
            <div className="mb-4">
                <label htmlFor="first_name" className="block font-semibold">
                    First Name:
                </label>
                <input
                    type="text"
                    id="first_name"
                    name="first_name"
                    value={editedUser.first_name}
                    onChange={handleInputChange}
                />
            </div>
            <div className="mb-4">
                <label htmlFor="last_name" className="block font-semibold">
                    Last Name:
                </label>
                <input
                    type="text"
                    id="last_name"
                    name="last_name"
                    value={editedUser.last_name}
                    onChange={handleInputChange}
                />
            </div>
            <div className="mb-4">
                <label htmlFor="email" className="block font-semibold">
                    Email:
                </label>
                <input
                    type="email"
                    id="email"
                    name="email"
                    value={editedUser.email}
                    onChange={handleInputChange}
                />
            </div>
            <div className="mb-4">
                <button className="bg-blue-500 text-white px-2 py-1 rounded-md mr-2">
                    Save
                </button>
                <button className="bg-gray-400 text-white px-2 py-1 rounded-md" onClick={onCancel}>
                    Cancel
                </button>
            </div>
        </form>
    );
}

export default EditUserDetailsForm;
