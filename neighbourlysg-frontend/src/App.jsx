/* eslint-disable no-unused-vars */
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import ResidentLogin from './auth/Resident/ResidentLogin';
import RegisterPage from './auth/Resident/RegisterPage';
import ResidentMainPage from './auth/Resident/ResidentMainPage';
import ProfileSettingsPage from './auth/Resident/ProfileSettingsPage';

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/ResidentLogin" />} /> {/* Redirect root to ResidentLogin */}
                <Route path="/ResidentLogin" element={<ResidentLogin />} />
                <Route path="/Register" element={<RegisterPage />} />
                <Route path="/ResidentMainPage" element={<ResidentMainPage />} />
                <Route path="/ProfileSettings" element={<ProfileSettingsPage />} />
                <Route path="*" element={<Navigate to="/" />} /> {/* Fallback for undefined routes */}
            </Routes>
        </Router>
    );
};

export default App;
