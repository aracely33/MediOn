import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import VerificationForm from "./components/VerificationForm";
import {
  verifyEmailCode,
  resendVerificationCode,
} from "./services/authService";
import { usePatient } from "../../context/PatientContext";

const ConfirmEmailPage = () => {
  const navigate = useNavigate();
  const { reviewLogin } = usePatient();
  const [isVerifying, setIsVerifying] = useState(false);

  const handleVerify = async (code) => {
    console.log("🚀 Enviando código de verificación al backend:", code);
    setIsVerifying(true);

    try {
      const response = await verifyEmailCode(code);
      console.log("✅ Respuesta del backend (verificación):", response.data);

      const { success, message, token } = response.data;

      if (!success) {
        alert("❌ Código inválido o expirado.");
        return;
      }

      // success === true
      alert(message || "✅ ¡Correo verificado correctamente!");
      console.log("💖 Token recibido del backend:", token);

      if (!token) {
        console.warn("⚠️ No se recibió token del backend.");
        // intentar reviewLogin sin token (por si token ya estaba en localStorage)
        await reviewLogin();
        navigate("/patient-home");
        return;
      }

      // GUARDA token y LUEGO llama reviewLogin PASÁNDOLO explícitamente
      localStorage.setItem("patient_token", token);
      console.log(
        "💾 Token guardado en localStorage:",
        localStorage.getItem("patient_token")
      );

      // Pasa el token explicitamente para evitar race conditions
      const ok = await reviewLogin(token);
      console.log("🔁 reviewLogin terminó. Resultado:", ok);

      if (ok) {
        navigate("/patient-home");
      } else {
        // si reviewLogin devolvió false, mostrar info y no navegar
        alert(
          "⚠️ No fue posible restaurar la sesión. Intenta iniciar sesión manualmente."
        );
      }
    } catch (error) {
      console.error("⚠️ Error al verificar el código:", error);
      const msg = error.response?.data?.message;
      if (msg?.includes("ya está verificado")) {
        alert(
          "✅ Tu correo ya estaba verificado. Intentando restaurar sesión..."
        );
        await reviewLogin();
        navigate("/patient-home");
      } else {
        alert("⚠️ Ocurrió un error al verificar tu correo.");
      }
    } finally {
      setIsVerifying(false);
    }
  };

  const handleResend = async () => {
    const email = localStorage.getItem("patient_email");
    if (!email) return alert("⚠️ No se encontró el correo registrado.");
    try {
      const response = await resendVerificationCode(email);
      console.log("✅ Reenvío ok:", response);
      alert("📧 Código reenviado.");
    } catch (err) {
      console.error("❌ Error reenviar:", err.response?.data || err);
      alert(err.response?.data?.message || "No se pudo reenviar el código.");
    }
  };

  return (
    <div>
      {isVerifying ? (
        <div className="d-flex justify-content-center align-items-center vh-100">
          <div className="text-center">
            <div className="spinner-border text-primary mb-3" role="status" />
            <p>🔄 Verificando tu cuenta...</p>
          </div>
        </div>
      ) : (
        <VerificationForm onVerify={handleVerify} onResend={handleResend} />
      )}
    </div>
  );
};

export default ConfirmEmailPage;
