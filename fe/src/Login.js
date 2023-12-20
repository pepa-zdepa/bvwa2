import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import apiUrl from "./Url";

function Login({loggedInUser}) {
    const navigate = useNavigate();

    const [loginData, setLoginData] = useState({
        nickname: '',
        password: '',
    });

    const [loginError, setLoginError] = useState(null);

    // useEffect(async () => {
    //     try {
    //         await fetch(`${apiUrl}/auth/check-username-available?username=`)
    //     } catch (e) {
    //         console.log(e)
    //     }
    // }, []);

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setLoginData({...loginData, [name]: value});
    };

    const handleLoginSubmit = async (e) => {
        e.preventDefault();
        // Successful login
        setLoginError(null);
        // Build formData object.
        let formData = new FormData();
        formData.append('user', loginData.nickname);
        formData.append('password', loginData.password);

        const loginGetData = await fetch(`${apiUrl}/auth/login`,
            {
                credentials: "include", // include, same-origin, omit
                mode: "cors", // no-cors,cors, same-origin
                body: formData,
                method: "post"
            });
        if (!loginGetData.ok) {
            setLoginError('Nevalidní login či heslo');
            return;
        }

        const loginGotData = await loginGetData.json();
        localStorage.setItem('loggedInUser', JSON.stringify(loginGotData))
        console.log(localStorage.getItem('loggedInUser'))

        navigate('/profile');
        window.location.reload();
    }

    return (
        <form onSubmit={handleLoginSubmit}>
            {loginError && <div className="text-red-500 mb-4">{loginError}</div>}
            <div className="mb-4">
                <label htmlFor="nickname" className="block text-sm font-medium text-gray-700">
                    Login
                </label>
                <input
                    type="nickname"
                    id="nickname"
                    name="nickname"
                    value={loginData.nickname}
                    onChange={handleInputChange}
                    className="w-full px-4 py-2 border rounded-lg"
                    required
                />
            </div>
            <div className="mb-4">
                <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                    Heslo
                </label>
                <input
                    type="password"
                    id="password"
                    name="password"
                    value={loginData.password}
                    onChange={handleInputChange}
                    className="w-full px-4 py-2 border rounded-lg"
                    required
                />
            </div>
            <button
                type="submit"
                className="bg-purple-500 hover:bg-purple-600 text-white px-4 py-2 rounded-full w-full"
            >
                Přihlásit
            </button>
        </form>
    );
}

export default Login;
