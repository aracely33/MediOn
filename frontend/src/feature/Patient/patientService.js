import { api } from "../../services/api";

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

// Modificar datos personales de un paciente existente
export const updatePatient = async (id, updatedData) => {
  try {
    const response = await api.patch(`/patients/${id}`, updatedData, {
      withCredentials: true,
    });
    return response.data;
  } catch (error) {
    console.error("Error en updatePatient:", error);
    throw error;
  }
};
