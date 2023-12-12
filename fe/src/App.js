import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './index.css';
import Home from './Home';
import Register from './Register';
import Profile from './Profile';
import Header from './Header';
import Footer from './Footer';
import MessageDetail from './MessageDetail';
import AdminPanel from './AdminPanel';
import MailPage from "./MailPage";

let loggedInUser;
function App() {
  return (
    <Router>
      <div>
        <Header />
        <Routes>
          <Route path="/register" element={<Register />} />
          <Route path="/profile" element={<Profile loggedInUser={loggedInUser} />} />
          <Route path="/messages" element={<MailPage loggedInUser={loggedInUser}/>}/>
          <Route path="/admin" element={<AdminPanel />} />
          <Route path="/" element={<Home loggedInUser={loggedInUser} />} />
        </Routes>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
