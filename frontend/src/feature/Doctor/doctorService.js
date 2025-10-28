import { api } from "../../services/api";

// Obtener todos los profesionales
export const getAllProfessionals = async () => {
  const response = await api.get("/professional");
  return response.data;
};

// Obtener un profesional por ID
export const getProfessionalById = async (id) => {
  const response = await api.get(`/professional/${id}`);
  return response.data;
};
