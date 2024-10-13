/* eslint-disable no-unused-vars */
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, useLocation } from 'react-router-dom';
import Navbar from './component/Navbar';
import ResidentLogin from './auth/Resident/ResidentLogin';
import RegisterPage from './auth/Resident/RegisterPage';
import ResidentMainPage from './auth/Resident/ResidentMainPage';
import ProfileSettingsPage from './auth/Resident/ProfileSettingsPage';
import SurveyShowcasePage from './auth/Resident/SurveyShowcasePage';
import CreateSurveyForm from './auth/Resident/CreateSurveyForm';
import SurveyResponsesPage from './auth/Resident/SurveyResponsesPage';
import SurveyDetailPage from './auth/Resident/SurveyDetailPage';
import EventShowcasePage from './auth/Resident/EventShowcasePage';
import CommunityPost from './auth/Resident/CommunityPost';
import ManageUsers from './auth/Admin/ManageUsers';


const Layout = ({ children }) => {
  const location = useLocation();
  const isLoginPage = location.pathname === '/ResidentLogin'; // Check if the current route is login page
  
  return (
    <div>
      {!isLoginPage && <Navbar />} {/* Render Navbar unless on the login page */}
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
            <Route path="/Register" element={<RegisterPage />} />
            <Route path="/ResidentMainPage" element={<ResidentMainPage />} />
            <Route path="/ProfileSettings" element={<ProfileSettingsPage />} />
            <Route path="/surveys" element={<SurveyShowcasePage />} /> {/* Route for SurveyShowcasePage */}
            <Route path="/CreateSurveyForm" element={<CreateSurveyForm />} /> {/* Route for CreateSurveyPage */}
            <Route path="/survey-responses" element={<SurveyResponsesPage />} />
            <Route path="/survey-detail" element={<SurveyDetailPage />} />
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