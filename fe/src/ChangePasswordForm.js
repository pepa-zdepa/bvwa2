import React, { useState } from 'react';

function ChangePasswordForm({ userId, onSave, onCancel }) {
    const [newPassword, setNewPassword] = useState('');

    const handleInputChange = (e) => {
        setNewPassword(e.target.value);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSave(userId, newPassword);
    };

    return (
        <form onSubmit={handleSubmit}>
            <div className="mb-4">
                <label htmlFor="new_password" className="block font-semibold">
                    New Password:
                </label>
                <input
                    type="password"
                    id="new_password"
                    name="new_password"
                    value={newPassword}
                    onChange={handleInputChange}
                />
            </div>
            <div className="mb-4">
                <button className="bg-blue-500 text-white px-2 py-1 rounded-md mr-2">
                    Save Password
                </button>
                <button className="bg-gray-400 text-white px-2 py-1 rounded-md" onClick={onCancel}>
                    Cancel
                </button>
            </div>
        </form>
    );
}

export default ChangePasswordForm;
