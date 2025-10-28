import { api } from "../../../services/api";

// Endpoint para registrar un paciente
export const registerPatient = async (patient) => {
  console.log(patient);
  return await api.post("/auth/patient/register", patient);
};

// Obtener usuario autenticado
export const getMe = async () => {
  try {
    const token = localStorage.getItem("auth_token");
    if (!token) throw new Error("No hay token disponible");

    const response = await api.get("/auth/me", {
      headers: { Authorization: `Bearer ${token}` },
    });

    return response.data;
  } catch (error) {
    console.error("Error en getMe:", error);
    throw error;
  }
};

// Endpoint para iniciar sesión un paciente
export const loginUser = async (user) => {
  return await api.post("/auth/login", user);
};

export const logoutUser = async () => {
  return await api.post("/auth/logout");
};

// ✅ Verificar código con token
export const verifyEmailCode = async (code) => {
  console.log("🔍El código a validar es :", code);
  return await api.post(
    "/auth/verify-email",
    { verificationCode: code } // ⚠️ aquí debe coincidir el backend
  );
};

// 🔁 Reenviar código con email
export const resendVerificationCode = async (email) => {
  console.log("📧 Reenviando código a:", email);
  return await api.post(
    `/auth/resend-verification?email=${encodeURIComponent(email)}`
  );
};
