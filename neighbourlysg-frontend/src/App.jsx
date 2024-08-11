/* eslint-disable no-unused-vars */
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import ResidentLogin from './auth/Resident/ResidentLogin';
import RegisterPage from './auth/Resident/RegisterPage';
import ResidentMainPage from './auth/Resident/ResidentMainPage'; // Updated path to ResidentMainPage

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<ResidentLogin />} />
                <Route path="/ResidentLogin" element={<ResidentLogin />} />
                <Route path="/Register" element={<RegisterPage />} />
                <Route path="/ResidentMainPage" element={<ResidentMainPage />} /> {/* Updated route for ResidentMainPage */}
            </Routes>
        </Router>
    );
};

export default App;
