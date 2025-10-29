import { createContext, useContext, useState } from "react";
import { createAppointmentBook } from "../feature/Patient/appointmentBook/services/appointmentBooksService";

export const AppointmentContext = createContext();

export const useAppointment = () => {
  const context = useContext(AppointmentContext);
  if (!context) {
    throw new Error(
      "useAppointment must be used within an AppointmentProvider"
    );
  }
  return context;
};

export const AppointmentProvider = ({ children }) => {
  const [appointments, setAppointments] = useState([]);
  const [doctorsAppointment, setDoctorsAppointment] = useState([]);

  const addAppointment = async (appointmentData) => {
    try {
      const response = await createAppointmentBook(appointmentData);
      console.log("Appointment created:", response.data);
      setAppointments([...appointments, response.data]);
      return response.data;
    } catch (error) {
      console.error("Error creating appointment:", error);
      console.error(
        "Error details:",
        error.response?.data.details || error.message
      );
      throw error;
    }
  };

  return (
    <AppointmentContext.Provider
      value={{
        appointments,
        addAppointment,
        doctorsAppointment,
      }}
    >
      {children}
    </AppointmentContext.Provider>
  );
};
