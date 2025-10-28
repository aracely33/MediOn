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

// Obtener horarios ocupados del médico
export const getDoctorAvailability = async (doctorId) => {
  try {
    const response = await api.get(
      `/api/appointments/doctor/${doctorId}/availability`
    );
    // Devuelve un array de strings ISO de fechas ocupadas
    return response.data;
  } catch (error) {
    console.error("Error al obtener disponibilidad del doctor:", error);
    throw error;
  }
};
