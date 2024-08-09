import React, { useState } from 'react';

const ResidentLogin = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Add your authentication logic here
        console.log('Login submitted', { email, password });
    };

    const containerStyle = {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        backgroundColor: '#f5f5f5'
    };

    const formStyle = {
        width: '100%',
        maxWidth: '500px',
        padding: '20px',
        backgroundColor: '#ffffff',
        boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
        borderRadius: '8px'
    };

    const inputStyle = {
        width: '98%',
        padding: '10px',
        margin: '10px 0',
        border: '1px solid #ccc',
        borderRadius: '4px'
    };

    const buttonStyle = {
        width: '100%',
        padding: '10px',
        backgroundColor: '#007bff',
        color: '#fff',
        border: 'none',
        borderRadius: '4px',
        cursor: 'pointer'
    };

    return (
        <div style={containerStyle}>
            <form style={formStyle} onSubmit={handleSubmit}>
                <h2 className="text-center">Resident Login</h2>
                <div>
                    <label>Email</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        style={inputStyle}
                        required
                    />
                </div>
                <div>
                    <label>Password</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        style={inputStyle}
                        required
                    />
                </div>
                <button type="submit" style={buttonStyle}>Login</button>
            </form>
        </div>
    );
};

export default ResidentLogin;
