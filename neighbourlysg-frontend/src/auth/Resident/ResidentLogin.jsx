import 'bootstrap/dist/css/bootstrap.min.css';
import neighbourlySGbackground from '../../assets/neighbourlySGbackground.jpg';

function ResidentLogin() {
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
        <form>
          <div className="mb-3">
            <label htmlFor="email" className="form-label" style={{ fontSize: '1rem', color: '#495057' }}>Email Address</label>
            <input 
              type="email" 
              className="form-control" 
              id="email" 
              placeholder="Enter your email" 
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
  );
}

export default ResidentLogin;
