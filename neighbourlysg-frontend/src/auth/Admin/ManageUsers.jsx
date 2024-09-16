import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import neighbourlySGbackground from '../../assets/neighbourlySGbackground.jpg';
import SGLogo from '../../assets/SGLogo.avif';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const ManageUsers = () => {
  const [profiles, setProfiles] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedProfileId, setSelectedProfileId] = useState(null);
  const [selectedRoles, setSelectedRoles] = useState({
    admin: false,
    org: false,
    user: false
  });

  useEffect(() => {
    const fetchProfiles = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/ProfileService/profiles');
        if (response.status === 200) {
          setProfiles(response.data);
        }
      } catch (error) {
        console.error(error);
      }
    };

    fetchProfiles();
  }, []);

  const editRole = async (profileId) => {
    setSelectedProfileId(profileId);
    try {
      // get profile, precheck user role 
      const response = await axios.get(`http://localhost:8080/api/ProfileService/profile/${profileId}`);
      if (response.status === 200) {
        const roles = response.data.roles || [];
        setSelectedRoles({
          admin: roles.includes(3),
          org: roles.includes(2),
          user: roles.includes(1),
        });
        setIsModalOpen(true);
      }
    } catch (error) {
      console.error("Error fetching profile:", error);
    }
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const handleCheckboxChange = (e) => {
    const { name, checked } = e.target;
    setSelectedRoles((prevRoles) => ({
      ...prevRoles,
      [name]: checked
    }));
  };

  const saveChanges = async () => {
    const updatedRoles = [];
    if (selectedRoles.admin) updatedRoles.push(3); // Admin role
    if (selectedRoles.org) updatedRoles.push(2); // Organiser role
    if (selectedRoles.user) updatedRoles.push(1); // User role

    try {
      const response = await axios.put(`http://localhost:8080/api/ProfileService/assign-role`, {
          "userId": selectedProfileId,
          "roleIds": updatedRoles
      });

      if (response.status === 200) {
        toast.success('Role updated successfully!');
        closeModal();
      }
    } catch (error) {
      toast.error('Failed to update roles. Please try again.');
    }
  };

  return (
    <div
      className="d-flex flex-column align-items-center vh-100"
      style={{
        backgroundImage: `url(${neighbourlySGbackground})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        backdropFilter: 'blur(5px)',
      }}
    >
      <ToastContainer />
      {/* Navigation Bar */}
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

      <div className="container mt-5 flex-grow-1">
        <div className="mb-4" style={{ width: "100%" }}>
          <h2
            className="text-dark bg-white bg-opacity-75 p-2 rounded text-center"
            style={{ display: "inline-block", width: "100%" }}
          >
            Manage Users
          </h2>
        </div>

        <div className="table-responsive bg-white bg-opacity-80 p-2 rounded shadow">
          <table className="table table-hover">
            <thead className="table-dark">
              <tr>
                <th scope="col">ID</th>
                <th scope="col">Name</th>
                <th scope="col">Email</th>
                <th scope="col">Constituency</th>
                <th scope="col"></th>
              </tr>
            </thead>
            <tbody>
              {profiles.map((profile) => (
                <tr key={profile.id}>
                  <th scope="row">{profile.id}</th>
                  <td>{profile.name}</td>
                  <td>{profile.email}</td>
                  <td>{profile.constituency}</td>
                  <td style={{ display: 'flex', justifyContent: 'flex-end' }}>
                    <button
                      type="button"
                      className="btn btn-primary"
                      onClick={() => editRole(profile.id)}
                    >
                      Edit Role
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {isModalOpen && (
        <div
          className="modal fade show"
          style={{
            display: 'block',
            position: 'fixed',
            top: '0',
            left: '0',
            width: '100%',
            height: '100%',
            backgroundColor: 'rgba(0, 0, 0, 0.5)',
            zIndex: '1050',
          }}
          tabIndex="-1"
          aria-labelledby="editRoleModalLabel"
          aria-hidden="true"
        >
          <div
            className="modal-dialog"
            style={{
              margin: '10% auto',
              width: '80%',
            }}
          >
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title" id="editRoleModalLabel">Edit Role</h5>
                <button
                  type="button"
                  className="btn-close"
                  onClick={closeModal}
                  aria-label="Close"
                ></button>
              </div>
              <div className="modal-body">
                <div>
                  <div className="form-check">
                      <input
                        className="form-check-input"
                        type="checkbox"
                        id="admin"
                        name="admin"
                        checked={selectedRoles.admin}
                        onChange={handleCheckboxChange}
                      />
                      <label className="form-check-label" htmlFor="admin">
                        Admin
                      </label>
                    </div>

                    <div className="form-check">
                      <input
                        className="form-check-input"
                        type="checkbox"
                        id="org"
                        name="org"
                        checked={selectedRoles.org}
                        onChange={handleCheckboxChange}
                      />
                      <label className="form-check-label" htmlFor="org">
                        Organizer
                      </label>
                    </div>

                    <div className="form-check">
                      <input
                        className="form-check-input"
                        type="checkbox"
                        id="user"
                        name="user"
                        checked={selectedRoles.user}
                        onChange={handleCheckboxChange}
                      />
                      <label className="form-check-label" htmlFor="user">
                        User
                      </label>
                    </div>
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={closeModal}>Close</button>
                <button type="button" className="btn btn-primary" onClick={saveChanges}>Save changes</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ManageUsers;
