import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Form, Button, Alert, Modal } from 'react-bootstrap';
import neighbourlySGbackground from '../../assets/neighbourlySGbackground.jpg';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../Utils/axiosConfig'

const grcSmcOptions = [
  'Aljunied GRC', 'Ang Mo Kio GRC', 'Bishan-Toa Payoh GRC', 'Chua Chu Kang GRC',
  'East Coast GRC', 'Holland-Bukit Timah GRC', 'Jalan Besar GRC', 'Jurong GRC',
  'Marine Parade GRC', 'Marsiling-Yew Tee GRC', 'Nee Soon GRC', 'Pasir Ris-Punggol GRC',
  'Sembawang GRC', 'Tampines GRC', 'Tanjong Pagar GRC', 'West Coast GRC',
  'Bukit Batok SMC', 'Bukit Panjang SMC', 'Hong Kah North SMC', 'Hougang SMC',
  'Kebun Baru SMC', 'MacPherson SMC', 'Marymount SMC', 'Mountbatten SMC',
  'Pioneer SMC', 'Potong Pasir SMC', 'Punggol West SMC', 'Radin Mas SMC',
  'Yio Chu Kang SMC', 'Yuhua SMC'
];

function ProfileSettingsPage() {
  const [key, setKey] = useState('profile');
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [constituency, setConstituency] = useState('');
  const [errors, setErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [deleteConfirm, setDeleteConfirm] = useState('');  // Changed to deleteConfirm
  const userId = sessionStorage.getItem('userId');
  const navigate = useNavigate();

  const validateForm = () => {
    const errors = {};
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;

    if (!name) errors.name = 'Name is required';
    if (!email) {
      errors.email = 'Email is required';
    } else if (!emailRegex.test(email)) {
      errors.email = 'Please enter a valid email address';
    }
    if (key === 'password') {
      if (!oldPassword) {
        errors.oldPassword = 'Old password is required';
      } else if (oldPassword !== "user's stored old password") { // Example comparison
        errors.oldPassword = 'Old password is incorrect';
      }
      if (!newPassword) {
        errors.newPassword = 'New password is required';
      } else if (!passwordRegex.test(newPassword)) {
        errors.newPassword = 'Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character';
      }
      if (newPassword !== confirmPassword) {
        errors.confirmPassword = 'Passwords do not match';
      }
    }
    if (key === 'constituency' && !constituency) errors.constituency = 'Constituency is required';

    return errors;
  };

  const handleProfileUpdate = async (e) => {
    e.preventDefault();
    const validationErrors = validateForm();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      setSuccessMessage('');
    } else {
      setErrors({});
      try {
        const response = await axiosInstance.put(`/ProfileService/updateProfile/${userId}`, {
          name,
          email,
          password: newPassword,
          constituency,
        });
        setSuccessMessage('Profile updated successfully!');
      } catch (error) {
        setErrors({ api: 'Failed to update profile. Please try again later.' });
      }
    }
  };

  const handleDeleteAccount = async () => {
    if (deleteConfirm === 'delete account') {
      try {
        const response = await axiosInstance.delete(`/ProfileService/profile/${userId}`);
        if (response.status === 200) {
          alert('Your account has been deleted.');
          setShowModal(false);
          navigate('/ResidentLogin');
        }
      } catch (error) {
        alert('Failed to delete account. Please try again.');
      }
    } else {
      alert('You need to type "delete account" to confirm.');
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
      <div className="container mt-5 flex-grow-1">
        <div className="mb-4" style={{ width: "100%" }}>
          <h2
            className="text-dark bg-white bg-opacity-75 p-2 rounded text-center"
            style={{ display: "inline-block", width: "100%" }}
          >
            Profile Settings
          </h2>
        </div>

        <div
          className="card mb-4"
          style={{ width: "100%", padding: "20px" }}
        >
          <Form onSubmit={handleProfileUpdate}>
            <Form.Group controlId="formName" className="mb-3">
              <div className="row align-items-center">
                <div className="col-sm-4">
                  <Form.Label className="form-label">Name</Form.Label>
                </div>
                <div className="col-sm-8">
                  <Form.Control
                    type="text"
                    placeholder="Enter your name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    isInvalid={errors.name}
                  />
                  <Form.Control.Feedback type="invalid">{errors.name}</Form.Control.Feedback>
                </div>
              </div>
            </Form.Group>

            <Form.Group controlId="formemail" className="mb-3">
              <div className="row align-items-center">
                <div className="col-sm-4">
                  <Form.Label className="form-label">Email Address</Form.Label>
                </div>
                <div className="col-sm-8">
                  <Form.Control
                    type="email"
                    placeholder="Enter your email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    isInvalid={errors.email}
                  />
                  <Form.Control.Feedback type="invalid">{errors.email}</Form.Control.Feedback>
                </div>
              </div>
            </Form.Group>

            <Form.Group controlId="formOldPassword" className="mb-3">
              <div className="row align-items-center">
                <div className="col-sm-4">
                  <Form.Label className="form-label">Old Password</Form.Label>
                </div>
                <div className="col-sm-8">
                  <Form.Control
                    type="password"
                    placeholder="Enter old password"
                    value={oldPassword}
                    onChange={(e) => setOldPassword(e.target.value)}
                    isInvalid={errors.oldPassword}
                  />
                  <Form.Control.Feedback type="invalid">{errors.oldPassword}</Form.Control.Feedback>
                </div>
              </div>
            </Form.Group>

            <Form.Group controlId="formNewPassword" className="mb-3">
              <div className="row align-items-center">
                <div className="col-sm-4">
                  <Form.Label className="form-label">New Password</Form.Label>
                </div>
                <div className="col-sm-8">
                  <Form.Control
                    type="password"
                    placeholder="Enter new password"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    isInvalid={errors.newPassword}
                  />
                  <Form.Control.Feedback type="invalid">{errors.newPassword}</Form.Control.Feedback>
                </div>
              </div>
            </Form.Group>

            <Form.Group controlId="formConfirmPassword" className="mb-3">
              <div className="row align-items-center">
                <div className="col-sm-4">
                  <Form.Label className="form-label">Confirm Password</Form.Label>
                </div>
                <div className="col-sm-8">
                  <Form.Control
                    type="password"
                    placeholder="Confirm new password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    isInvalid={errors.confirmPassword}
                  />
                  <Form.Control.Feedback type="invalid">{errors.confirmPassword}</Form.Control.Feedback>
                </div>
              </div>
            </Form.Group>


            <Form.Group controlId="formConstituency" className="mb-3">
              <div className="row align-items-center">
                <div className="col-sm-4">
                  <Form.Label className="form-label">Constituency</Form.Label>
                </div>
                <div className="col-sm-8">
                  <Form.Select
                    value={constituency}
                    onChange={(e) => setConstituency(e.target.value)}
                    isInvalid={errors.constituency}
                  >
                    <option value="">Select your constituency</option>
                    {grcSmcOptions.map((option, index) => (
                      <option key={index} value={option}>{option}</option>
                    ))}
                  </Form.Select>
                  <Form.Control.Feedback type="invalid">{errors.constituency}</Form.Control.Feedback>
                </div>
              </div>
            </Form.Group>

            <div className="row">
              <div className="col d-flex justify-content-start">
                <Button
                  variant="danger"
                  type="button"
                  className="mt-2"
                  onClick={() => setShowModal(true)}
                >
                  Delete Account
                </Button>
              </div>
              <div className="col d-flex justify-content-end">
                <Button
                  variant="primary"
                  type="submit"
                  className="mt-2"
                >
                  Update
                </Button>
              </div>
            </div>
          </Form>

          {successMessage && <Alert variant="success" className="mt-3">{successMessage}</Alert>}
        </div>
      </div>

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Confirm Delete Account</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <p style={{ color: '#d9534f' }}>Are you sure you want to delete your account? This action cannot be undone.</p>
            <Form.Group controlId="formDeleteAccount" className="mb-3">
              <Form.Label>Type 'delete account' to delete your account</Form.Label>
              <Form.Control
                placeholder="delete account"
                value={deleteConfirm}
                onChange={(e) => setDeleteConfirm(e.target.value)}
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Cancel
          </Button>
          <Button variant="danger" onClick={handleDeleteAccount}>
            Confirm Delete Account
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}

export default ProfileSettingsPage;
