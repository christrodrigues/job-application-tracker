
import axios from "axios";

//base url
const API_BASE_URL = 'http://localhost:8080/api';

// Create an instance of axios with the base URL
const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
    });

 //request interceptor to add JWT token to requests   
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

//response interceptor to handle errors globally
api.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (error.response) {
            // Handle specific status codes
            if (error.response.status === 401) {
                // Unauthorized, redirect to login
               
                localStorage.removeItem('token');
                localStorage.removeItem('user'); 
                window.location.href = '/login';
            } else if (error.response.status === 403) {
                // Forbidden, show an error message
                alert('You do not have permission to perform this action.');
            }
        }
        return Promise.reject(error);
    }
);

export default api;