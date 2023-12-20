import React, {useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import apiUrl from "./Url";

function Header() {
    const navigate = useNavigate();
    const loggedInUser = JSON.parse(localStorage.getItem('loggedInUser')) || null;

    const [user, setUser] = useState(loggedInUser);

    const handleLogout = async () => {
        await fetch(`${apiUrl}/auth/logout`,
            {
                credentials: "include", // include, same-origin, omit
                mode: "cors", // no-cors,cors, same-origin
                method: "post"
            });
        localStorage.removeItem('loggedInUser');
        setUser(null);

        navigate('/');
        window.location.reload();
    };

    return (
        <header className="bg-purple-500 py-4">
            <nav className="container mx-auto flex items-center justify-between">
                <div className="text-white text-2xl font-bold">Webovka!</div>
                <ul className="space-x-4 flex items-center">
                    { !user &&
                        <li>
                            <Link to="/" className="text-white hover:text-purple-300">
                                Login
                            </Link>
                        </li>
                    }
                    {/* Display profile and log-out button if the user is logged in */}
                    {user && (
                        <>
                            <li>
                                <Link to="/profile" className="text-white hover:text-purple-300">
                                    Profil
                                </Link>
                            </li>
                            {loggedInUser.role === "admin" && (
                                <li>
                                    <Link to="/admin" className="text-white hover:text-purple-300">
                                        Panel adminu
                                    </Link>
                                </li>
                            )}
                            <li>
                                <button
                                    onClick={handleLogout}
                                    className="text-white hover:text-purple-300 focus:outline-none"
                                >
                                    Odhlásit
                                </button>
                            </li>
                            <li>
                                <Link to="/messages" className="text-white hover:text-purple-300">
                                    Zpravy
                                </Link>
                            </li>
                        </>
                    )}
                </ul>
            </nav>
        </header>
    );
}

export default Header;