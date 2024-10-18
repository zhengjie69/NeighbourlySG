import React from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import SGLogo from "../assets/SGLogo.avif";
import "bootstrap/dist/css/bootstrap.min.css";

const Navbar = () => {
  const navigate = useNavigate();
  const location = useLocation(); // Get the current route

  // Fetch user roles from sessionStorage
  const userRoles = sessionStorage.getItem("roles") || "";
  const isOrganiser = userRoles.includes("2"); // Assuming 2 is Organiser role ID
  const isResident = userRoles.includes("1"); // Assuming 1 is Resident role ID
  const isAdmin = userRoles.includes("3"); // Assuming 3 is Admin role ID

  const handleLogout = () => {
    sessionStorage.clear();
    navigate("/ResidentLogin");
  };

  // Render links based on the roles
  const renderNavLinks = () => {
    if (isResident) {
      return (
        <>
          <li className="nav-item">
            <Link className="nav-link" to="/surveys">
              Surveys
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/events">
              Events
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/posts">
              Community Posts
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/ProfileSettings">
              Profile
            </Link>
          </li>
        </>
      );
    } else if (isOrganiser) {
      return (
        <>
          <li className="nav-item">
            <Link className="nav-link" to="/surveys">
              Surveys
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/events">
              Events
            </Link>
          </li>
        </>
      );
    } else if (isAdmin) {
      return (
        <>
          <li className="nav-item">
            <Link className="nav-link" to="/surveys">
              Surveys
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/events">
              Events
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/posts">
              Community Posts
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/ProfileSettings">
              Profile
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/ManageUsers">
              Manage Users
            </Link>
          </li>
        </>
      );
    }
    return null; // If no role is matched, don't render any links
  };

  // Paths where the logo is not clickable and Logout button is hidden
  const hideLogoutAndLogoLink =
    location.pathname === "/register" || location.pathname === "/ResidentLogin";

  return (
    <nav
      className="navbar navbar-expand-lg navbar-light bg-light"
      style={{ zIndex: 2, padding: "10px 20px", width: "100%" }}
    >
      <div className="container-fluid">
        {/* Conditionally render the logo and link */}
        {!hideLogoutAndLogoLink ? (
          <Link className="navbar-brand" to="/ResidentMainPage">
            <img
              src={SGLogo}
              alt="SG Logo"
              style={{ marginRight: "10px", width: "50px", height: "35px" }}
            />
            NeighbourlySG
          </Link>
        ) : (
          <div className="navbar-brand">
            <img
              src={SGLogo}
              alt="SG Logo"
              style={{ marginRight: "10px", width: "50px", height: "35px" }}
            />
            NeighbourlySG
          </div>
        )}

        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNavDropdown"
          aria-controls="navbarNavDropdown"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="navbarNavDropdown">
          <ul className="navbar-nav me-auto mb-2 mb-lg-0">
            {renderNavLinks()}
          </ul>

          {/* Conditionally render the Logout button */}
          {!hideLogoutAndLogoLink && (
            <button
              className="btn btn-outline-danger ms-2"
              onClick={handleLogout}
            >
              Logout
            </button>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
