import React, { useState, useEffect } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';
import neighbourlySGbackground from '../../assets/neighbourlySGbackground.jpg';
import SGLogo from '../../assets/SGLogo.avif'; // Import the Singapore logo
import { useLocation } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';

const ResidentMainPage = () => {
  const location = useLocation();
  const [userRoles, setUserRoles] = useState("");
  const [isAdmin, setIsAdmin] = useState(false);
  const [isOrganiser, setIsOrganiser] = useState(false);
  const [isResident, setIsResident] = useState(false);

  // Use useEffect to fetch roles after component mounts
  useEffect(() => {
    const roles = sessionStorage.getItem("roles") || "";
    setUserRoles(roles);
    setIsAdmin(roles.includes("3"));       // Assuming "3" is the Admin role ID
    setIsOrganiser(roles.includes("2"));   // Assuming "2" is the Organiser role ID
    setIsResident(roles.includes("1"));    // Assuming "1" is the Resident role ID
  }, []);

  // Show toast message if available in the location state
  useEffect(() => {
    if (location.state?.message) {
      toast.success(location.state.message);
    }
  }, [location.state]);

  return (
    <div 
      style={{ 
        backgroundImage: `url(${neighbourlySGbackground})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        minHeight: '100vh',
        color: '#fff',
        display: 'flex',
        flexDirection: 'column'
      }}
    >
      <ToastContainer/>
      {/* Overlay for better text visibility */}
      <div 
        style={{
          position: 'absolute',
          top: 0,
          left: 0,
          width: '100%',
          height: '100%',
          backgroundColor: 'rgba(0, 0, 0, 0.5)',
          zIndex: 1
        }}
      ></div>

      <div className="container mt-5 text-dark" style={{ 
        backgroundColor: 'rgba(255, 255, 255, 0.9)', 
        borderRadius: '10px', 
        padding: '30px', 
        zIndex: 2,
        marginBottom: 'auto',
        maxWidth: '1200px'
      }}>
        <div className="row">
          <div className="col-md-8">
            <h2>Community Dashboard</h2>
            <p>Welcome to NeighbourlySG! Here’s what’s happening in your community:</p>

            {/* Admin sees Manage User */}
            {isAdmin && (
              <div className="card mb-4" style={{ backgroundColor: '#f8f9fa', padding: '20px', borderRadius: '10px' }}>
                <h4>Manage User</h4>
                <p>Oversee and manage user profiles and account settings within the community platform.</p>
                <div className="d-flex justify-content-end">
                  <Link to="/manageusers" className="btn btn-primary">View More</Link>
                </div>
              </div>
            )}

            {/* Resident sees Surveys, Events, Community Posts, Profile */}
            {isResident && (
              <>
                <div className="card mb-4" style={{ backgroundColor: '#f8f9fa', padding: '20px', borderRadius: '10px' }}>
                  <h4>Active Surveys</h4>
                  <p>Participate in ongoing community surveys to share your thoughts.</p>
                  <div className="d-flex justify-content-end">
                    <Link to="/surveys" className="btn btn-primary">View More</Link>
                  </div>
                </div>

                <div className="card mb-4" style={{ backgroundColor: '#f8f9fa', padding: '20px', borderRadius: '10px' }}>
                  <h4>Upcoming Events</h4>
                  <p>Stay updated on future community gatherings and activities. RSVP to join and participate in your local events.</p>
                  <div className="d-flex justify-content-end">
                    <Link to="/events" className="btn btn-primary">View More</Link>
                  </div>
                </div>

                <div className="card mb-4" style={{ backgroundColor: '#f8f9fa', padding: '20px', borderRadius: '10px' }}>
                  <h4>Community Posts</h4>
                  <p>Discover the latest updates, posts, and discussions happening in your community. Share your thoughts, comment, and engage with your neighbors.</p>
                  <div className="d-flex justify-content-end">
                    <Link to="/posts" className="btn btn-primary">View More</Link>
                  </div>
                </div>

                <div className="card mb-4" style={{ backgroundColor: '#f8f9fa', padding: '20px', borderRadius: '10px' }}>
                  <h4>Profile</h4>
                  <p>Manage your profile and account settings. Keep your details up to date.</p>
                  <div className="d-flex justify-content-end">
                    <Link to="/profile" className="btn btn-primary">View More</Link>
                  </div>
                </div>
              </>
            )}

            {/* Organiser sees Surveys and Events */}
            {isOrganiser && (
              <>
                <div className="card mb-4" style={{ backgroundColor: '#f8f9fa', padding: '20px', borderRadius: '10px' }}>
                  <h4>Active Surveys</h4>
                  <p>Manage ongoing community surveys and view responses.</p>
                  <div className="d-flex justify-content-end">
                    <Link to="/surveys" className="btn btn-primary">View More</Link>
                  </div>
                </div>

                <div className="card mb-4" style={{ backgroundColor: '#f8f9fa', padding: '20px', borderRadius: '10px' }}>
                  <h4>Upcoming Events</h4>
                  <p>Plan and manage community events. Keep your community updated.</p>
                  <div className="d-flex justify-content-end">
                    <Link to="/events" className="btn btn-primary">View More</Link>
                  </div>
                </div>
              </>
            )}
          </div>
        </div>
      </div>

      {/* Footer */}
      <footer className="bg-dark text-white text-center py-3 mt-5" style={{ zIndex: 2, position: 'relative', bottom: 0, width: '100%' }}>
        <p>NeighbourlySG &copy; 2024. All rights reserved.</p>
      </footer>
    </div>
  );
};

export default ResidentMainPage;
