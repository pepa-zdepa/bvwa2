import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './index.css';
import Home from './Home';
import Register from './Register';
import Profile from './Profile';
import Header from './Header';
import Footer from './Footer';
import MailPage from './MailPage';

function App() {
 // Retrieve logged-in user information from local storage
 const [loggedInUser, setLoggedInUser] = useState(
  JSON.parse(localStorage.getItem('loggedInUser')) || null
);
useEffect(() => {
  const handleStorageChange = () => {
    setLoggedInUser(JSON.parse(localStorage.getItem('loggedInUser')) || null);
  };

  // Обновление состояния loggedInUser при изменении в localStorage
  setLoggedInUser(JSON.parse(localStorage.getItem('loggedInUser')) || null);

  // Добавление обработчика события для отслеживания изменений в localStorage
  window.addEventListener('storage', handleStorageChange);

  // Удаление обработчика события при размонтировании компонента
  return () => {
    window.removeEventListener('storage', handleStorageChange);
  };
}, []);
  return (
    <Router>
      <div>
        <Header loggedInUser={loggedInUser} />
        <Routes>
          <Route path="/register" element={<Register />} />
          <Route path="/profile" element={<Profile loggedInUser={loggedInUser} />} />
          <Route path="/mail" element={<MailPage />} />
          <Route path="/" element={<Home />} />
        </Routes>
        <Footer />
        
      </div>
    </Router>
  );
}

export default App;
