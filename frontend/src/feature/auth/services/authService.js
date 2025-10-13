import { api } from "../../../services/api";

// Endpoint para registrar un paciente
export const registerPatient = async (patient) => {
  return await api.post("/auth/register", patient);
};

// Endpoint para iniciar sesión un paciente
export const loginPatient = async (patient) => {
  return await api.post("/auth/login", patient);
};

export const logoutPatient = async () => {
  return await api.post("/auth/logout");
};
