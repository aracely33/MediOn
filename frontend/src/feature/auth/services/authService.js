import { api } from "../../../services/api";

// Endpoint para registrar un paciente
export const registerPatient = async (patient) => {
  return await api.post("/auth/patient/register", patient);
};

// Endpoint para iniciar sesión un paciente
export const loginUser = async (user) => {
  return await api.post("/auth/login", user);
};

export const logoutUser = async () => {
  return await api.post("/auth/logout");
};
