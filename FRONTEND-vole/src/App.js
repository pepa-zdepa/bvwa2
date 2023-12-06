import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './index.css';
import Home from './Home';
import Register from './Register';
import Profile from './Profile';
import Header from './Header';
import Footer from './Footer';
import MailPage from './MailPage';
import AdminPanel from './AdminPanel';

function App() {
   // Check if the admin user is already present in registeredUsers
   const storedUsers = JSON.parse(localStorage.getItem('registeredUsers')) || [];
   const isAdminPresent = storedUsers.some(user => user.isAdmin===true);
 
   // If the admin user is not present, add them to registeredUsers
   if (!isAdminPresent) {
     const adminUser = {
       email: 'admin@bvwa.net',
       password: 'adm1ni$tratoR',
       firstName: 'Admin',
       lastName: 'User',
       isAdmin: true
       // Add other properties as needed
     };
 
     storedUsers.push(adminUser);
     localStorage.setItem('registeredUsers', JSON.stringify(storedUsers));     
   }
 
   const loggedInUser = JSON.parse(localStorage.getItem('loggedInUser')) || null;

  return (
    <Router>
      <div>
        <Header />
        <Routes>
          <Route path="/register" element={<Register />} />
          <Route path="/profile" element={<Profile loggedInUser={loggedInUser} />} />
          <Route path="/admin" element={<AdminPanel />} />
          <Route path="/mail" element={<MailPage />} />
          <Route path="/" element={<Home loggedInUser={loggedInUser} />} />
        </Routes>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
