import axios from 'axios';

// Base URL of the user-service (Spring Boot microservice, auth/identity)
const API = axios.create({
  baseURL: 'http://localhost:8081/api'
});

export default API;
