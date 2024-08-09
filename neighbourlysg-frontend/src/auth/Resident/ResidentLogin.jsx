import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

function ResidentLogin() {
  return (
    <div className="d-flex justify-content-center align-items-center vh-100" style={{ backgroundColor: '#f1f1f1' }}>
      <div className="card p-4" style={{ width: '500px', boxShadow: '0 8px 16px rgba(0, 0, 0, 0.2)', borderRadius: '10px' }}>
        <h3 className="text-center mb-4" style={{ fontWeight: 'bold', fontSize: '1.5rem' }}>Resident Login</h3>
        <form>
          <div className="mb-3">
            <label htmlFor="email" className="form-label">Email</label>
            <input type="email" className="form-control" id="email" placeholder="Enter your email" />
          </div>
          <div className="mb-4">
            <label htmlFor="password" className="form-label">Password</label>
            <input type="password" className="form-control" id="password" placeholder="Enter your password" />
          </div>
          <button type="submit" className="btn btn-primary w-100">Login</button>
        </form>
      </div>
    </div>
  );
}

export default ResidentLogin;
