// Aquí se agregan los endpoints de agendar citas
import { api } from "../../../../services/api";

// Endpoint para crear una nueva cita
export const createAppointmentBook = async (appointmentData) => {
  return await api.post("/api/appointments", appointmentData);
};
