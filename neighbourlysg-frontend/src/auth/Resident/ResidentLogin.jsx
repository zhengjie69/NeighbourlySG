import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import neighbourlySGbackground from '../../assets/neighbourlySGbackground.jpg';
import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../Utils/axiosConfig'

function ResidentLogin() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      // // Encrypt the password using the RSA utility
      // const encryptedPassword = rsaEncrypt(password);
      const response = await axiosInstance.post('/auth/login', {
        email: email,
        password: password,  // Send the encrypted password
      });

      if (response.status === 200) {
        sessionStorage.setItem('userId', response.data.id);
        sessionStorage.setItem('roles', response.data.roles);
        sessionStorage.setItem('constituency', response.data.constituency);
        sessionStorage.setItem('accessToken', response.data.accessToken);
        sessionStorage.setItem('auth-user', JSON.stringify(response.data));
        navigate('/ResidentMainPage', { state: { message: "welcome back!" } });
      }
    } catch (error) {
      console.error('Login error:', error);
      setIsError(true);
      setMessage('Login failed. Please try again.');
    }
  };

  return (
    <div
      className="d-flex justify-content-center align-items-center vh-100"
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
          <Link to="/register" className="text-primary" style={{ fontSize: '0.9rem', textDecoration: 'none' }}>
            Don&apos;t have an account? <span style={{ fontWeight: 'bold' }}>Register here</span>
          </Link>
        </div>
      </div>
    </div>
  );
}

export default ResidentLogin;
