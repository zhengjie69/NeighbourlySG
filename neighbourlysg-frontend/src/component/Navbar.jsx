import React from 'react';
import { Link } from 'react-router-dom';
import SGLogo from '../assets/SGLogo.avif';
import 'bootstrap/dist/css/bootstrap.min.css';

const Navbar = () => {
    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light" style={{ zIndex: 2, padding: '10px 20px', width: '100%' }}>
        <div className="container-fluid">
            <Link className="navbar-brand" to="/ResidentMainPage">
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
                <Link className="nav-link" to="/ProfileSettings">Profile</Link>
                </li>
                <li className="nav-item">
                <Link className="nav-link" to="/ManageUsers">Manage Users</Link>
                </li>
            </ul>
            <span className="navbar-text">
                Welcome, [User]!
            </span>
            </div>
        </div>
        </nav>
    );
};

export default Navbar;
