import { api } from "../../services/api";

// Obtener usuario autenticado
export const getMe = async () => {
  try {
    const response = await api.get("/auth/me", { withCredentials: true });
    return response.data;
  } catch (error) {
    console.error("Error en getMe:", error);
    throw error;
  }
};

// Obtener datos completos de paciente por ID
export const getPatientById = async (id) => {
  try {
    const response = await api.get(`/patients/${id}`, {
      withCredentials: true,
    });
    return response.data;
  } catch (error) {
    console.error("Error en getPatientById:", error);
    throw error;
  }
};

// Traer citas de un paciente
export const getAppointmentsByPatientId = async (patientId) => {
  return await api.get(`/api/appointments/patient/${patientId}`);
};

// Cancelar cita
export const cancelAppointment = async (appointmentId) => {
  try {
    const response = await api.patch(
      `/api/appointments/${appointmentId}/cancel`
    );
    return response.data;
  } catch (error) {
    console.error("Error cancelando la cita:", error);
    throw error;
  }
};
