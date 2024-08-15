// eslint-disable-next-line no-unused-vars
import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useNavigate, Link } from 'react-router-dom';
import neighbourlySGbackground from '../../assets/neighbourlySGbackground.jpg';
import axios from 'axios'; // Import axios for making HTTP requests
import SGLogo from '../../assets/SGLogo.avif'; // Replace with your actual logo path

function ResidentLogin() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false); 

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post('http://localhost:8080/login', { email, password });
      // Handle successful login
      setMessage('Login successful!');
      setIsError(false); // Clear error state
      setErrors({});
      // Redirect to home or dashboard
      //window.location.href = '/home';
    } catch (error) {
      console.error('Registration error:', error); 
      console.error('Registration error msg:', error.response.data.errorDetails); 
      
      if (error.response && error.response.data && error.response.data.errorDetails) {
          setMessage(error.response.data.errorDetails); // Display the backend error message
      } else {
        setMessage('Login failed. Please try again.'); // Fallback message
      }
      setIsError(true); // Set error state
  }
  
  const [warning, setWarning] = useState('');
  const navigate = useNavigate();

  return (
    <div 
      className="d-flex justify-content-center align-items-center flex-column vh-100" 
      style={{ 
        backgroundImage: `url(${neighbourlySGbackground})`, 
        backgroundSize: 'cover', 
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat'
      }}
    >
      <div className="card p-5" style={{ width: '400px', boxShadow: '0 12px 24px rgba(0, 0, 0, 0.15)', borderRadius: '12px', backgroundColor: 'rgba(255, 255, 255, 0.9)' }}>
        <div className="text-center">
          <h3 className="mb-4" style={{ fontWeight: '600', fontSize: '1.8rem', color: '#343a40' }}>Resident Login</h3>
          <p style={{ fontSize: '1rem', color: '#6c757d', marginBottom: '30px' }}>
            Access community surveys, events, and more by logging in.
          </p>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label htmlFor="email" className="form-label" style={{ fontSize: '1rem', color: '#495057' }}>Email Address</label>
            <input 
              type="email" 
              className="form-control" 
              id="email" 
              placeholder="Enter your email" 
              style={{ height: '45px', fontSize: '1rem', borderRadius: '8px' }} 
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
          <div className="mb-4">
            <label htmlFor="password" className="form-label" style={{ fontSize: '1rem', color: '#495057' }}>Password</label>
            <input 
              type="password" 
              className="form-control" 
              id="password" 
              placeholder="Enter your password" 
              style={{ height: '45px', fontSize: '1rem', borderRadius: '8px' }} 
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
          <button type="submit" className="btn btn-primary w-100" style={{ height: '50px', fontSize: '1rem', borderRadius: '8px' }}>
            Login
          </button>
          {message && (
                    <div className={`alert ${isError ? 'alert-danger' : 'alert-success'} mt-4`} role="alert">
                        {message}
                    </div>
                )}
        </form>
        <div className="mt-4 text-center">
          <a href="/forgot-password" className="text-primary" style={{ fontSize: '0.9rem', textDecoration: 'none' }}>Forgot Password?</a>
        </div>
        <div className="mt-3 text-center">
          <a href="/register" className="text-primary" style={{ fontSize: '0.9rem', textDecoration: 'none' }}>
            Don&apos;t have an account? <span style={{ fontWeight: 'bold' }}>Register here</span>
          </a>
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
              <li className="nav-item dropdown">
                <a className="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                Resident Login
                </a>
                <ul className="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                  <li><Link className="dropdown-item" to="#">Resident Login</Link></li>
                  <li><Link className="dropdown-item" to="#">Organizer Login</Link></li>
                </ul>
              </li>
            </ul>
          </div>
        </div>
      </nav>

      <div className="d-flex justify-content-center align-items-center flex-grow-1">
        <div className="card p-5" style={{ width: '400px', boxShadow: '0 12px 24px rgba(0, 0, 0, 0.15)', borderRadius: '12px', backgroundColor: 'rgba(255, 255, 255, 0.9)' }}>
          <div className="text-center">
            <h3 className="mb-4" style={{ fontWeight: '600', fontSize: '1.8rem', color: '#343a40' }}>Resident Login</h3>
            <p style={{ fontSize: '1rem', color: '#6c757d', marginBottom: '30px' }}>
              Access community surveys, events, and more by logging in.
            </p>
          </div>
          {warning && <div className="alert alert-danger" role="alert">{warning}</div>}
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label htmlFor="email" className="form-label" style={{ fontSize: '1rem', color: '#495057' }}>Email Address</label>
              <input 
                type="email" 
                className="form-control" 
                id="email" 
                placeholder="Enter your email" 
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                style={{ height: '45px', fontSize: '1rem', borderRadius: '8px' }} 
              />
            </div>
            <div className="mb-4">
              <label htmlFor="password" className="form-label" style={{ fontSize: '1rem', color: '#495057' }}>Password</label>
              <input 
                type="password" 
                className="form-control" 
                id="password" 
                placeholder="Enter your password" 
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                style={{ height: '45px', fontSize: '1rem', borderRadius: '8px' }} 
              />
            </div>
            <button type="submit" className="btn btn-primary w-100" style={{ height: '50px', fontSize: '1rem', borderRadius: '8px' }}>
              Login
            </button>
          </form>
          <div className="mt-4 text-center">
            <a href="/forgot-password" className="text-primary" style={{ fontSize: '0.9rem', textDecoration: 'none' }}>Forgot Password?</a>
          </div>
          <div className="mt-3 text-center">
            <a href="/register" className="text-primary" style={{ fontSize: '0.9rem', textDecoration: 'none' }}>
              Don&apos;t have an account? <span style={{ fontWeight: 'bold' }}>Register here</span>
            </a>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ResidentLogin;