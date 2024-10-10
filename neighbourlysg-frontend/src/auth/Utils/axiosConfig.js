// src/utils/axiosInstance.js
import axios from 'axios';

// Create an Axios instance
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api', // Adjust the base URL as needed
});

// Add a request interceptor
axiosInstance.interceptors.request.use(
  (config) => {
    const token = sessionStorage.getItem('accessToken'); // Retrieve token from local storage (or wherever you store it)

    // Check if the request URL matches the auth pattern
    if (token && !config.url.startsWith('/auth')) {
      config.headers.Authorization = `Bearer ${token}`; // Set the token in the Authorization header
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default axiosInstance;
