import { api } from "../../../services/api";

// Endpoint para registrar un paciente
export const registerPatient = async (patient) => {
  const response = await api.post("/auth/register", patient);
  return response.data;
};

// Endpoint para iniciar sesión un paciente
export const loginPatient = async (patient) => {
  const response = await api.post("/auth/login", patient);
  return response.data;
};

export const logoutPatient = async () => {
  const response = await api.post("/auth/logout");
  return response.data;
};
