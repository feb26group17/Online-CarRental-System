import axios from 'axios';
import { store } from '../redux/store';

// Base URL of user-service (Spring Boot auth/identity service on port 8081)
export const authApi = axios.create({
  baseURL: 'http://localhost:8081/api'
});

// Base URL of ocrs-crud-service (Spring Boot CRUD operations service on port 8082)
export const crudApi = axios.create({
  baseURL: 'http://localhost:8082/api'
});

const attachAuthToken = (config) => {
  try {
    const state = store.getState();
    const token = state?.auth?.token;
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
  } catch (err) {
    console.error('Error attaching auth token to request:', err);
  }
  return config;
};

authApi.interceptors.request.use(attachAuthToken, (error) => Promise.reject(error));
crudApi.interceptors.request.use(attachAuthToken, (error) => Promise.reject(error));

// Export authApi as default for backward compatibility with login/register pages
export default authApi;
