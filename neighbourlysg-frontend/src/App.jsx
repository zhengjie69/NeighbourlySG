/* eslint-disable no-unused-vars */
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, useLocation } from 'react-router-dom';
import Navbar from './component/Navbar';
import ResidentLogin from './auth/Resident/ResidentLogin';
import OrganiserLogin from './auth/Organiser/OrganiserLogin'; // Corrected import statement
import RegisterPage from './auth/Resident/RegisterPage';
import ResidentMainPage from './auth/Resident/ResidentMainPage';
import ProfileSettingsPage from './auth/Resident/ProfileSettingsPage';
import SurveyShowcasePage from './auth/Resident/SurveyShowcasePage';
import CreateSurveyForm from './auth/Resident/CreateSurveyForm';
import EventShowcasePage from './auth/Resident/EventShowcasePage';
import CommunityPost from './auth/Resident/CommunityPost';
import ManageUsers from './auth/Admin/ManageUsers';

// eslint-disable-next-line react/prop-types
const Layout = ({ children }) => {
    const location = useLocation();
    const isLoginPage = location.pathname === '/ResidentLogin' || location.pathname === '/OrganiserLogin'; // Corrected to use "OrganiserLogin"
    
    return (
      <div>
        {!isLoginPage && <Navbar />} {/* Render Navbar unless on the login pages */}
        {children}
      </div>
    );
};

const App = () => {
    // const roles = JSON.parse(sessionStorage.getItem('roles')) || [];
    
    return (
        <Router>
            <Layout>
                <Routes>
                    <Route path="/" element={<Navigate to="/ResidentLogin" />} />
                    <Route path="/ResidentLogin" element={<ResidentLogin />} />
                    <Route path="/OrganiserLogin" element={<OrganiserLogin />} /> {/* Corrected route for OrganiserLogin */}
                    <Route path="/Register" element={<RegisterPage />} />
                    <Route path="/ResidentMainPage" element={<ResidentMainPage />} />
                    <Route path="/ProfileSettings" element={<ProfileSettingsPage />} />
                    <Route path="/surveys" element={<SurveyShowcasePage />} /> {/* Route for SurveyShowcasePage */}
                    <Route path="/CreateSurveyForm" element={<CreateSurveyForm />} /> {/* Route for CreateSurveyForm */}
                    <Route path="/events" element={<EventShowcasePage />} /> {/* Route for ResidentEventPage */}
                    <Route path="/posts" element={<CommunityPost />} /> {/* Route for CommunityPost */}
                    <Route path='/manageusers' element={<ManageUsers />} />
                    {/* <Route path="/ManageUsers" element={roles.includes(3) ? <ManageUsers /> : <Navigate to="/" />} /> */}
                </Routes>
            </Layout>
        </Router>
    );
};

export default App;
