import { createContext, useContext, useEffect, useState } from "react";
import {
  loginPatient,
  logoutPatient,
  registerPatient,
} from "../feature/auth/services/authService";

// Las líneas de código comentadas son en caso de usar localStorage para almacenar los tokens

export const PatientContext = createContext();

export const usePatient = () => {
  const context = useContext(PatientContext);
  if (!context) {
    throw new Error("usePatient must be used within a PatientProvider");
  }
  return context;
};

export const PatientProvider = ({ children }) => {
  const [patient, setPatient] = useState(null);
  const [isAuthenticatedPatient, setIsAuthenticatedPatient] = useState(false);
  const [errors, setErrors] = useState([]);

  const signUp = async (patientData) => {
    try {
      const response = await registerPatient(patientData);
      setPatient(response.data);
      setIsAuthenticatedPatient(true);
    } catch (error) {
      setErrors(error);
      console.log("Error to register patient: ", error);
    }
  };

  const signIn = async (patientData) => {
    try {
      const response = await loginPatient(patientData);
      const { token, user } = response.data;
      //   localStorage.setItem("patient_token", token);
      setPatient(user);
      isAuthenticatedPatient(true);
    } catch (error) {
      setErrors(error);
      console.log("Error to register patient: ", error);
    }
  };

  const signOut = async () => {
    // await localStorage.removeItem("patient_token");
    await logoutPatient();
    setPatient(null);
    isAuthenticatedPatient(false);
  };

  useEffect(() => {
    function reviewLogin() {
      const token = localStorage.getItem("patient_token");
      if (!token) {
        setPatient(null);
        isAuthenticatedPatient(false);
        return;
      }
    }
    reviewLogin();
  }, []);

  return (
    <PatientContext.Provider
      value={{
        patient,
        signUp,
        signIn,
        signOut,
        errors,
        isAuthenticatedPatient,
      }}
    >
      {children}
    </PatientContext.Provider>
  );
};
