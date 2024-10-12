// eslint-disable-next-line no-unused-vars
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios'; // Import axios
import { Modal, Button } from 'react-bootstrap';
import neighbourlySGbackground from '../../assets/neighbourlySGbackground.jpg';
import { Link } from 'react-router-dom';
import { rsaEncrypt } from '../Utils/RSAUtil';

function RegisterPage() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [selectedConstituency, setSelectedConstituency] = useState('');
  const [errors, setErrors] = useState({});
  const [message, setMessage] = useState(''); // State for success or error message
  const [isError, setIsError] = useState(false); // New state to track error vs success
  const [showSuccessModal, setShowSuccessModal] = useState(false);

  const navigate = useNavigate(); // Initialize useNavigate hook

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

  const validateForm = () => {
    const errors = {};
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;

    if (!name) errors.name = 'Name is required';
    if (!email) {
      errors.email = 'Email is required';
    } else if (!emailRegex.test(email)) {
      errors.email = 'Please enter a valid email address';
    }
    if (!password) {
      errors.password = 'Password is required';
    } else if (!passwordRegex.test(password)) {
      errors.password = 'Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character';
    }
    if (password !== confirmPassword) {
      errors.confirmPassword = 'Passwords do not match';
    }
    if (!selectedConstituency) errors.selectedConstituency = 'Please select your constituency';

    return errors;
  };


  const handleRegister = async (e) => {
    e.preventDefault();
    const validationErrors = validateForm();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    try {
      // Encrypt the password using the RSA utility
      // const encryptedPassword = rsaEncrypt(password);
      const response = await axios.post('http://localhost:5000/api/auth/register', {
        name: name,
        email: email,
        password: password,  // Send the encrypted password
        constituency: selectedConstituency,
      });

      // Handle success response
      // const { message, data } = response.data;
      //console.log("Registered Profile:", data); // Optionally use the profile data

      setMessage("Register successfully!");
      setIsError(false); // Clear error state
      setErrors({});

    } catch (error) {
      console.error('Registration error:', error);
      console.error('Registration error msg:', error.response.data.errorDetails);

      if (error.response && error.response.data && error.response.data.errorDetails) {
        setMessage(error.response.data.errorDetails); // Display the backend error message
      } else {
        setMessage('Registration failed, please try again.'); // Fallback message
      }
      setIsError(true); // Set error state
    }
  };

  useEffect(() => {
    if (showSuccessModal) {
      const timer = setTimeout(() => {
        setShowSuccessModal(false);
        navigate('/ResidentMainPage'); // Redirect to ResidentMainPage after 3 seconds
      }, 3000); // 3 seconds

      return () => clearTimeout(timer); // Cleanup timer if component unmounts
    }
  }, [showSuccessModal, navigate]);

  return (
    <div
      className="d-flex justify-content-center align-items-center vh-100"
      style={{
        backgroundImage: `url(${neighbourlySGbackground})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        backdropFilter: 'blur(5px)',
      }}
    >
      <div className="card p-5" style={{ width: '400px', boxShadow: '0 12px 24px rgba(0, 0, 0, 0.2)', borderRadius: '16px', backgroundColor: 'rgba(255, 255, 255, 0.9)' }}>
        <div className="text-center">
          <h3 className="mb-4" style={{ fontWeight: '700', fontSize: '1.8rem', color: '#333' }}>Create an Account</h3>
          <p style={{ fontSize: '1rem', color: '#6c757d', marginBottom: '30px' }}>
            Join our community and get started!
          </p>
        </div>
        <form onSubmit={handleRegister}>
          <div className="mb-3">
            <label htmlFor="name" className="form-label" style={{ fontSize: '1rem', color: '#495057' }}>Name</label>
            <input
              type="text"
              className={`form-control ${errors.name && 'is-invalid'}`}
              id="name"
              placeholder="Enter your name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              style={{ height: '45px', fontSize: '1rem', borderRadius: '10px' }}
            />
            {errors.name && <div className="invalid-feedback">{errors.name}</div>}
          </div>
          <div className="mb-3">
            <label htmlFor="email" className="form-label" style={{ fontSize: '1rem', color: '#495057' }}>Email Address</label>
            <input
              type="email"
              className={`form-control ${errors.email && 'is-invalid'}`}
              id="email"
              placeholder="Enter your email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              style={{ height: '45px', fontSize: '1rem', borderRadius: '10px' }}
            />
            {errors.email && <div className="invalid-feedback">{errors.email}</div>}
          </div>
          <div className="mb-3">
            <label htmlFor="password" className="form-label" style={{ fontSize: '1rem', color: '#495057' }}>Password</label>
            <input
              type="password"
              className={`form-control ${errors.password && 'is-invalid'}`}
              id="password"
              placeholder="Enter your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              style={{ height: '45px', fontSize: '1rem', borderRadius: '10px' }}
            />
            {errors.password && <div className="invalid-feedback">{errors.password}</div>}
          </div>
          <div className="mb-4">
            <label htmlFor="confirmPassword" className="form-label" style={{ fontSize: '1rem', color: '#495057' }}>Confirm Password</label>
            <input
              type="password"
              className={`form-control ${errors.confirmPassword && 'is-invalid'}`}
              id="confirmPassword"
              placeholder="Confirm your password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              style={{ height: '45px', fontSize: '1rem', borderRadius: '10px' }}
            />
            {errors.confirmPassword && <div className="invalid-feedback">{errors.confirmPassword}</div>}
          </div>
          <div className="mb-4">
            <label htmlFor="constituency" className="form-label" style={{ fontSize: '1rem', color: '#495057' }}>Select Your Constituency</label>
            <select
              id="constituency"
              className={`form-select ${errors.selectedConstituency && 'is-invalid'}`}
              value={selectedConstituency}
              onChange={(e) => setSelectedConstituency(e.target.value)}
              style={{ height: '45px', fontSize: '1rem', borderRadius: '10px' }}
            >
              <option value="">Select Constituency</option>
              {grcSmcOptions.map((constituency, index) => (
                <option key={index} value={constituency}>{constituency}</option>
              ))}
            </select>
            {errors.selectedConstituency && <div className="invalid-feedback">{errors.selectedConstituency}</div>}
          </div>
          <button type="submit" className="btn btn-primary w-100" style={{ height: '50px', fontSize: '1rem', borderRadius: '10px', transition: 'background-color 0.3s ease' }}>
            Register
          </button>
        </form>
        {message && (
          <div className={`alert ${isError ? 'alert-danger' : 'alert-success'} mt-4`} role="alert">
            {message}
          </div>
        )}
        <div className="mt-4 text-center">
          <Link to="/ResidentLogin" className="text-primary" style={{ fontSize: '0.9rem', textDecoration: 'none', transition: 'color 0.3s ease' }}>
            Already have an account? <span style={{ fontWeight: 'bold' }}>Login here</span>
          </Link>
        </div>
      </div>
      {/* Success Modal */}
      <Modal show={showSuccessModal} onHide={() => setShowSuccessModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Success</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Your account has been created successfully! Redirecting to the main page...
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={() => navigate('/ResidentMainPage')}>
            Go to Main Page Now
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}

export default RegisterPage;
