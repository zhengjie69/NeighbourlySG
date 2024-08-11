// ResidentMainPage.jsx
// eslint-disable-next-line no-unused-vars
import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';
import neighbourlySGbackground from '../../assets/neighbourlySGbackground.jpg';
import SGLogo from '../../assets/SGLogo.avif'; // Import the Singapore logo

const ResidentMainPage = () => {
  const sampleSurveys = [
    { title: "Community Garden Feedback", description: "Share your thoughts on our new community garden initiative.", link: "/surveys" },
    { title: "Safety in the Neighborhood", description: "Provide input on how we can improve safety in our area.", link: "/surveys" }
  ];

  const sampleEvents = [
    { title: "Neighborhood Cleanup", date: "August 15, 2024", description: "Join us for a community cleanup event.", link: "/events" },
    { title: "Fall Festival", date: "September 10, 2024", description: "Celebrate the season with food, games, and fun!", link: "/events" }
  ];

  const samplePosts = [
    { 
      author: "Jane Doe", 
      content: "Had a great time at the community park today. It's looking beautiful!", 
      image: "https://via.placeholder.com/150",
      link: "/posts"
    },
    { 
      author: "John Smith", 
      content: "Excited for the upcoming Fall Festival. Who else is going?", 
      image: "",
      link: "/posts"
    },
  ];

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

      {/* Navigation Bar */}
      <nav className="navbar navbar-expand-lg navbar-light bg-light" style={{ zIndex: 2, padding: '10px 20px' }}>
        <div className="container-fluid">
          <Link className="navbar-brand" to="/">
            <img src={SGLogo} alt="SG Logo" style={{ marginRight: '10px', width: '50px', height: '35px' }} />
            NeighbourlySG
          </Link>
          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNavDropdown">
            <ul className="navbar-nav me-auto mb-2 mb-lg-0">
              <li className="nav-item">
                <Link className="nav-link active" aria-current="page" to="/surveys">Surveys</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/events">Events</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/posts">Community Posts</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/ProfileSettings">Profile</Link> {/* Link updated to point to /ProfileSettings */}
              </li>
              <li className="nav-item dropdown">
                <ul className="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                  <li><Link className="dropdown-item" to="/settings">Settings</Link></li>
                  <li><Link className="dropdown-item" to="/help">Help</Link></li>
                </ul>
              </li>
            </ul>
            <span className="navbar-text">
              Welcome, [User]!
            </span>
          </div>
        </div>
      </nav>

      {/* Main Content Area */}
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

            <div className="card mb-4" style={{ backgroundColor: '#f8f9fa', padding: '20px', borderRadius: '10px' }}>
              <h4>Active Surveys</h4>
              <p>Participate in ongoing community surveys to share your thoughts.</p>
              {sampleSurveys.map((survey, index) => (
                <Link to={survey.link} key={index} className="text-decoration-none text-dark">
                  <div className="card mb-3" style={{ borderRadius: '10px', cursor: 'pointer' }}>
                    <div className="card-body">
                      <h5 className="card-title">{survey.title}</h5>
                      <p className="card-text">{survey.description}</p>
                    </div>
                  </div>
                </Link>
              ))}
              <div className="d-flex justify-content-end">
                <Link to="/surveys" className="btn btn-primary">View More</Link>
              </div>
            </div>

            <div className="card mb-4" style={{ backgroundColor: '#f8f9fa', padding: '20px', borderRadius: '10px' }}>
              <h4>Upcoming Events</h4>
              {sampleEvents.map((event, index) => (
                <Link to={event.link} key={index} className="text-decoration-none text-dark">
                  <div className="card mb-3" style={{ borderRadius: '10px', cursor: 'pointer' }}>
                    <div className="card-body">
                      <h5 className="card-title">{event.title}</h5>
                      <h6 className="card-subtitle mb-2 text-muted">{event.date}</h6>
                      <p className="card-text">{event.description}</p>
                    </div>
                  </div>
                </Link>
              ))}
              <div className="d-flex justify-content-end">
                <Link to="/events" className="btn btn-primary">View More</Link>
              </div>
            </div>

            {/* Community News Feed with Clickable Containers */}
            <div className="card mb-4" style={{ backgroundColor: '#f8f9fa', padding: '20px', borderRadius: '10px' }}>
              <h4>Community News Feed</h4>
              {samplePosts.map((post, index) => (
                <Link to={post.link} key={index} className="text-decoration-none text-dark">
                  <div className="card mb-3" style={{ borderRadius: '10px', padding: '15px', cursor: 'pointer' }}>
                    <div className="card-body">
                      <h5 className="card-title">{post.author}</h5>
                      <p className="card-text">{post.content}</p>
                      {post.image && <img src={post.image} alt="Post" className="img-fluid rounded mb-2" />}
                    </div>
                  </div>
                </Link>
              ))}
              <div className="d-flex justify-content-end">
                <Link to="/posts" className="btn btn-primary">View More</Link>
              </div>
            </div>
          </div>

          <div className="col-md-4">
            <div className="card" style={{ backgroundColor: '#f8f9fa', padding: '20px', borderRadius: '10px' }}>
              <h4>Your Profile</h4>
              <p>Update your profile details and manage your account settings.</p>
              <div className="d-flex justify-content-center">
                <Link to="/ProfileSettings" className="btn btn-secondary" style={{ maxWidth: '200px', width: '100%' }}>Edit Profile</Link> {/* Link updated to point to /ProfileSettings */}
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Footer */}
      <footer className="bg-dark text-white text-center py-3 mt-5" style={{ zIndex: 2, position: 'relative', bottom: 0, width: '100%' }}>
        <p>NeighbourlySG &copy; 2024. All rights reserved.</p>
        <p><Link to="/contact" className="text-white">Contact Support</Link></p>
      </footer>
    </div>
  );
};

export default ResidentMainPage;
