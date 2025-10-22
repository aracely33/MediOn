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

  console.log("👀 Estado inicial del paciente:", patient);
  // 🟢 Registro
  const signUp = async (patientData) => {
    console.log("🚀 Registrando paciente...");
    try {
      const response = await registerPatient({
        name: patientData.name,
        lastName: patientData.lastName,
        email: patientData.email,
        password: patientData.password,
        confirmPassword: patientData.confirmPassword,
      });

      console.log("✅ Registro exitoso:", response.data);

      // Guardamos email para reenviar código o recordar usuario
      localStorage.setItem("patient_email", patientData.email);

      // Guardamos email en el contexto temporalmente
      setPatient({ email: patientData.email });

      return response.data;
    } catch (error) {
      console.error("⚠️ Error al registrar paciente:", error.response?.data);
      throw error;
    }
  };
  // 🟢 Login
  const signIn = async (patientData) => {
    try {
      const response = await loginPatient(patientData);
      const { token } = response.data;
      console.log("🔑 Token obtenido en login:", token);

      if (token) {
        localStorage.setItem("patient_token", token);
      }
      /*
      const meResponse = await api.get("/auth/me", {
        headers: { Authorization: `Bearer ${token}` },
      });

      console.log("👤 Datos del paciente después del login:", meResponse.data);

      setPatient(meResponse.data);
      setIsAuthenticatedPatient(true);*/

      await reviewLogin(token);
    } catch (error) {
      console.log("⚠️ Error al iniciar sesión:", error);
    }
  };

  // 🟢 Logout
  const signOut = async () => {
    try {
      console.log("🚪 Cerrando sesión...");
      await logoutPatient();
    } catch (error) {
      console.error("⚠️ Error al cerrar sesión:", error);
    } finally {
      localStorage.removeItem("patient_token");
      setPatient(null);
      setIsAuthenticatedPatient(false);
      setLoadingPatient(false); // 👋esto es nuevo
    }
  };

  // 🟢 Revisar sesión
  const reviewLogin = async (token) => {
    console.log("🟡 Ejecutando reviewLogin...");
    setLoadingPatient(true);

    const authToken = token || localStorage.getItem("patient_token");
    console.log("📦 Token usado en reviewLogin:", authToken);

    if (!token) {
      console.log("❌ No hay token. No autenticado.");
      setIsAuthenticatedPatient(false);
      setLoadingPatient(false);
      return false;
    }

    try {
      console.log("➡️ Solicitando /auth/me con token...");
      const response = await api.get("/auth/me", {
        headers: { Accept: "*/*", Authorization: `Bearer ${token}` },
      });

      console.log("✅ /auth/me respondió con éxito:", response.data);

      setPatient(response.data);
      setIsAuthenticatedPatient(true);
      console.log("🟢 Paciente autenticado correctamente");
      return true;
    } catch (error) {
      console.error(
        "🚨 Error al verificar el token:",
        error.response?.data || error
      );
      // limpiar token inválido
      localStorage.removeItem("patient_token"); // limpiar token inválido
      setIsAuthenticatedPatient(false);
      return false;
    } finally {
      setLoadingPatient(false);
      console.log("🔵 reviewLogin finalizado");
    }
  };
  // 🟢 Ejecutar revisión automática al iniciar app
  useEffect(() => {
    const token = localStorage.getItem("patient_token");
    if (token) {
      reviewLogin(token);
    } else {
      setLoadingPatient(false);
    }
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
