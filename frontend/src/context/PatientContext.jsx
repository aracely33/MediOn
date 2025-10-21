import { createContext, useContext, useEffect, useState } from "react";
import {
  loginPatient,
  logoutPatient,
  registerPatient,
} from "../feature/auth/services/authService";
import { api } from "../services/api";

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
  const [loadingPatient, setLoadingPatient] = useState(true);

  console.log("Ver paciente: ", patient);

  const signUp = async (patientData) => {
    console.log("estas en en signup de PatientContext");
    try {
      const response = await registerPatient({
        name: patientData.name,
        lastName: patientData.lastName,
        email: patientData.email,
        password: patientData.password,
        confirmPassword: patientData.confirmPassword,
      });

      console.log("✅ Registro exitoso:", response.data);
      //const { token } = response.data;
      // Guardamos en localStorage para usarlos luego
      //localStorage.setItem("patient_token", token);
      localStorage.setItem("patient_email", patientData.email);

      // Guardamos email en el contexto temporalmente
      setPatient({ email: patientData.email });

      return response.data;
    } catch (error) {
      console.error("Error al registrar paciente:", error.response?.data);
      throw error; // para que SignupForm pueda mostrarlo
    }
  };

  const signIn = async (patientData) => {
    try {
      const response = await loginPatient(patientData);
      console.log("Respuesta en PatientContext: ", response);
      const { token } = response.data;
      console.log("Token obtenido: ", token);
      localStorage.setItem("patient_token", token);

      const meResponse = await api.get("/auth/me", {
        headers: { Authorization: `Bearer ${token}` },
      });

      setPatient(meResponse.data);
      setIsAuthenticatedPatient(true);
    } catch (error) {
      console.log("Error to login patient: ", error);
    }
  };

  const signOut = async () => {
    try {
      await logoutPatient(); // <-- llamada al backend
    } catch (error) {
      console.error("Error al cerrar sesión:", error);
      // incluso si falla, limpiamos localStorage para evitar quedar autenticado
    } finally {
      localStorage.removeItem("patient_token");
      setPatient(null);
      setIsAuthenticatedPatient(false);
    }
  };

  // ✅ Movemos reviewLogin FUERA del useEffect
  const reviewLogin = async () => {
    console.log("🟡 Ejecutando reviewLogin...");

    setLoadingPatient(true);

    const token = localStorage.getItem("patient_token");
    console.log("📦 Token encontrado en localStorage:", token);

    if (!token) {
      console.log("❌ No hay token guardado. Cerrando sesión...");
      setPatient(null);
      setIsAuthenticatedPatient(false);
      setLoadingPatient(false);
      return;
    }

    try {
      console.log("➡️ Enviando solicitud a /auth/me con token...");
      const response = await api.get("/auth/me", {
        headers: { Authorization: `Bearer ${token}` },
      });
      console.log("✅ Respuesta recibida en reviewLogin:", response);

      setPatient(response.data);
      setIsAuthenticatedPatient(true);
      console.log("🟢 Paciente autenticado correctamente:", response.data);
    } catch (error) {
      console.log("🚨 Error al verificar el token:", error);
      signOut();
    } finally {
      setLoadingPatient(false);
      console.log("🔵 Finalizando revisión de login (setLoadingPatient=false)");
    }
  };

  useEffect(() => {
    reviewLogin();
  }, []);

  return (
    <PatientContext.Provider
      value={{
        patient,
        signUp,
        signIn,
        signOut,
        reviewLogin,
        isAuthenticatedPatient,
        loadingPatient,
      }}
    >
      {children}
    </PatientContext.Provider>
  );
};
