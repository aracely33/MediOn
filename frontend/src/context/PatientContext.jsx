import { createContext, useContext, useEffect, useState } from "react";
import {
  loginUser,
  logoutUser,
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

      localStorage.setItem("patient_email", patientData.email);
      setPatient({ email: patientData.email });

      return response.data;
    } catch (error) {
      console.error("⚠️ Error al registrar paciente:", error.response?.data);
      throw error;
    }
  };

  // 🟢 Crear historia clínica tras el primer login
  const createMedicalRecord = async (patientId, token) => {
    try {
      const number = `HC-${patientId.toString().padStart(6, "0")}`;
      const creationDate = new Date().toISOString().split("T")[0];
      console.log("🩺 Creando historia clínica con número:", number);

      const response = await api.post(
        "/medical-records",
        {
          number,
          creationDate,
          observations: "Historial clínico inicial.",
          patientId,
        },
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      if (response.data?.id) {
        console.log("✅ Historia clínica creada con ID:", response.data.id);
        return response.data.id;
      } else {
        console.warn("⚠️ No se devolvió ID del historial clínico");
        return null;
      }
    } catch (error) {
      console.error(
        "🚨 Error al crear historial clínico:",
        error.response?.data || error
      );
      return null;
    }
  };

  // 🟢 Login del paciente
  const signIn = async (patientData) => {
    try {
      console.log("🚀 Iniciando sesión del paciente...");
      const response = await loginUser(patientData);
      console.log("Respuesta en PatientContext:", response.data);

      const { token, id } = response.data;
      console.log("🔑 Token obtenido:", token);

      if (token) {
        localStorage.setItem("patient_token", token);
      }

      // ✅ Consultar datos del paciente
      const meResponse = await api.get("/auth/me", {
        headers: { Authorization: `Bearer ${token}` },
      });

      console.log("👤 Datos del paciente:", meResponse.data);

      setPatient(meResponse.data);
      setIsAuthenticatedPatient(true);

      // 🩺 Crear historia clínica si aún no existe
      if (id) {
        console.log("🧩 Verificando si tiene historia clínica...");
        try {
          const fullDetails = await api.get(
            `/medical-records/${id}/full-details`,
            {
              headers: { Authorization: `Bearer ${token}` },
            }
          );
          if (fullDetails.data) {
            console.log("🩵 Ya existe historia clínica, no se crea nueva.");
          }
        } catch (err) {
          console.log(
            "⚠️ No se encontró historia clínica, creando una nueva..."
          );
          await createMedicalRecord(id, token);
        }
      }

      //await reviewLogin(token);
    } catch (error) {
      console.log("⚠️ Error al iniciar sesión:", error.response?.data || error);
      throw error;
    }
  };

  // 🟢 Logout
  const signOut = async () => {
    try {
      await logoutUser(); // <-- llamada al backend
    } catch (error) {
      console.error("⚠️ Error al cerrar sesión:", error);
    } finally {
      localStorage.removeItem("patient_token");
      setPatient(null);
      setIsAuthenticatedPatient(false);
      setLoadingPatient(false);
    }
  };

  // 🟢 Revisar sesión
  const reviewLogin = async (token) => {
    console.log("🟡 Ejecutando reviewLogin...");
    setLoadingPatient(true);

    const authToken = token || localStorage.getItem("patient_token");
    console.log("📦 Token usado en reviewLogin:", authToken);

    if (!authToken) {
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
