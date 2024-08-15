// eslint-disable-next-line no-unused-vars
import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Tab, Tabs, Form, Button, Alert, Modal } from 'react-bootstrap';
import { Link } from 'react-router-dom'; // Import Link from react-router-dom
import SGLogo from '../../assets/SGLogo.avif'; // Import the Singapore logo
import neighbourlySGbackground from '../../assets/neighbourlySGbackground.jpg';

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
  const [disableAccount, setDisableAccount] = useState(false);
  const [errors, setErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [disablePassword, setDisablePassword] = useState('');

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

  const handleProfileUpdate = (e) => {
    e.preventDefault();
    const validationErrors = validateForm();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      setSuccessMessage('');
    } else {
      setErrors({});
      setSuccessMessage('Profile updated successfully!');
      // Handle profile update logic here
      console.log('Profile updated:', { name, email, oldPassword, newPassword, constituency });
    }
  };

  const handleDisableAccount = () => {
    // Implement account disable logic here after validation
    if (disablePassword === "user's stored password") { // Example comparison
      setDisableAccount(true);
      console.log('Account disabled');
      alert('Your account has been disabled.');
      setShowModal(false);
    } else {
      alert('Password is incorrect.');
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

      <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', marginTop: '150px' }}>
        <div className="card p-4 mb-4" style={{ width: '450px', boxShadow: '0 12px 24px rgba(0, 0, 0, 0.2)', borderRadius: '16px', backgroundColor: 'rgba(255, 255, 255, 0.9)' }}>
          <div className="text-center mb-3">
            <h3 style={{ fontWeight: '700', fontSize: '1.6rem', color: '#333' }}>Profile Settings</h3>
          </div>
          <Tabs
            id="profile-settings-tabs"
            activeKey={key}
            onSelect={(k) => setKey(k)}
            className="mb-3"
          >
            <Tab eventKey="profile" title="Profile">
              <Form onSubmit={handleProfileUpdate}>
                <Form.Group controlId="formName" className="mb-2">
                  <Form.Label>Name</Form.Label>
                  <Form.Control 
                    type="text" 
                    placeholder="Enter your name" 
                    value={name} 
                    onChange={(e) => setName(e.target.value)} 
                    isInvalid={errors.name}
                  />
                  <Form.Control.Feedback type="invalid">{errors.name}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group controlId="formEmail" className="mb-2">
                  <Form.Label>Email address</Form.Label>
                  <Form.Control 
                    type="email" 
                    placeholder="Enter your email" 
                    value={email} 
                    onChange={(e) => setEmail(e.target.value)} 
                    isInvalid={errors.email}
                  />
                  <Form.Control.Feedback type="invalid">{errors.email}</Form.Control.Feedback>
                </Form.Group>

                <Button variant="primary" type="submit" className="w-100 mt-2">
                  Update Profile
                </Button>
              </Form>
            </Tab>

            <Tab eventKey="password" title="Change Password">
              <Form onSubmit={handleProfileUpdate}>
                <Form.Group controlId="formOldPassword" className="mb-2">
                  <Form.Label>Old Password</Form.Label>
                  <Form.Control 
                    type="password" 
                    placeholder="Enter old password" 
                    value={oldPassword} 
                    onChange={(e) => setOldPassword(e.target.value)} 
                    isInvalid={errors.oldPassword}
                  />
                  <Form.Control.Feedback type="invalid">{errors.oldPassword}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group controlId="formNewPassword" className="mb-2">
                  <Form.Label>New Password</Form.Label>
                  <Form.Control 
                    type="password" 
                    placeholder="Enter new password" 
                    value={newPassword} 
                    onChange={(e) => setNewPassword(e.target.value)} 
                    isInvalid={errors.newPassword}
                  />
                  <Form.Control.Feedback type="invalid">{errors.newPassword}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group controlId="formConfirmPassword" className="mb-2">
                  <Form.Label>Confirm Password</Form.Label>
                  <Form.Control 
                    type="password" 
                    placeholder="Confirm new password" 
                    value={confirmPassword} 
                    onChange={(e) => setConfirmPassword(e.target.value)} 
                    isInvalid={errors.confirmPassword}
                  />
                  <Form.Control.Feedback type="invalid">{errors.confirmPassword}</Form.Control.Feedback>
                </Form.Group>

                <Button variant="primary" type="submit" className="w-100 mt-2">
                  Update Password
                </Button>
              </Form>
            </Tab>

            <Tab eventKey="constituency" title="Constituency">
              <Form onSubmit={handleProfileUpdate}>
                <Form.Group controlId="formConstituency" className="mb-2">
                  <Form.Label>Constituency</Form.Label>
                  <Form.Control 
                    as="select" 
                    value={constituency} 
                    onChange={(e) => setConstituency(e.target.value)} 
                    isInvalid={errors.constituency}
                  >
                    <option value="">Select your constituency</option>
                    {grcSmcOptions.map((option, index) => (
                      <option key={index} value={option}>{option}</option>
                    ))}
                  </Form.Control>
                  <Form.Control.Feedback type="invalid">{errors.constituency}</Form.Control.Feedback>
                </Form.Group>

                <Button variant="primary" type="submit" className="w-100 mt-2">
                  Update Constituency
                </Button>
              </Form>
            </Tab>
          </Tabs>

          {successMessage && <Alert variant="success" className="mt-3">{successMessage}</Alert>}
        </div>

        <div className="card p-4" style={{ width: '450px', boxShadow: '0 12px 24px rgba(0, 0, 0, 0.2)', borderRadius: '16px', backgroundColor: 'rgba(255, 255, 255, 0.9)' }}>
          <h4 style={{ fontSize: '1.2rem', color: '#333' }}>Disable Account</h4>
          <p style={{ fontSize: '0.9rem', color: '#555' }}>If you disable your account, you won’t be able to access it again.</p>
          <Button 
            variant="danger" 
            className="w-100 mt-2" 
            onClick={() => setShowModal(true)}
            disabled={disableAccount}
          >
            {disableAccount ? 'Account Disabled' : 'Disable Account'}
          </Button>
        </div>
      </div>

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Confirm Disable Account</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <p style={{ color: '#d9534f', fontSize: '0.9rem' }}>Are you sure you want to disable your account? This action cannot be undone.</p>
            <Form.Group controlId="formDisablePassword" className="mb-3">
              <Form.Label>Enter your password to disable account</Form.Label>
              <Form.Control 
                type="password" 
                placeholder="Enter password" 
                value={disablePassword} 
                onChange={(e) => setDisablePassword(e.target.value)} 
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Cancel
          </Button>
          <Button variant="danger" onClick={handleDisableAccount}>
            Confirm Disable Account
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}

export default ProfileSettingsPage;
