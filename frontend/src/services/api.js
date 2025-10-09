import axios from "axios";

export const api = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true,
});

// Lo comentado es en caso de usar localStorage para almacenar los tokens
// api.interceptors.request.use(
//     (config) => {
//         const patientToken = localStorage.getItem("patient_token");
//         const professionalToken = localStorage.getItem("professional_token");

//         const token = patientToken | professionalToken;

//         if (token) {
//             config.headers.Authorization = `Bearer ${token}`;
//         }

//         return config;
//     },
//     (error) => {
//         return Promise.reject(error);
//     }
// );

// api.interceptors.response.use(
//     (response) => {
//         return response;
//     },
//     (error) => {
//         if (error.response?.status === 401) {
//             // Token expirado o inválido - limpiar localStorage
//             localStorage.removeItem("patient_token");
//             localStorage.removeItem("professional_token");

//             // Redirigir al login
//             window.location.href = '/login';
//         }
//         return Promise.reject(error);
//     }
// );
