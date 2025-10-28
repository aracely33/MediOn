import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import VerificationForm from "./components/VerificationForm";
import {
  verifyEmailCode,
  resendVerificationCode,
} from "./services/authService";
import { usePatient } from "../../context/PatientContext";
import CustomToast from "./components/CustomToast";

const ConfirmEmailPage = () => {
  const navigate = useNavigate();
  const [showOverlay, setShowOverlay] = useState(false);
  const { signOut } = usePatient(); // Limpia sesión previa
  const [isVerifying, setIsVerifying] = useState(false);
  const [toast, setToast] = useState({
    show: false,
    type: "", // success | error | info
    title: "",
    message: "",
  });

  const handleVerify = async (code) => {
    console.log("🚀 Enviando código de verificación al backend:", code);
    setIsVerifying(true);

    try {
      const response = await verifyEmailCode(code);
      const { success, message } = response.data;

      if (!success) {
        setToast({
          show: true,
          type: "error",
          title: "Código inválido",
          message: "❌ Código inválido o expirado.",
        });
        return;
      }

      // Verificación exitosa
      setToast({
        show: true,
        type: "success",
        title: "¡Verificación exitosa!",
        message:
          "✅ Tu correo fue verificado correctamente. Serás redirigido al login.",
      });
      setShowOverlay(true);

      console.log("🎉 Correo confirmado, redirigiendo al login...");
      localStorage.removeItem("patient_token");

      setTimeout(() => navigate("/login"), 2000);
    } catch (error) {
      console.error("⚠️ Error al verificar el código:", error);
      const msg = error.response?.data?.message;

      setToast({
        show: true,
        type: "error",
        title: "Error",
        message: msg?.includes("ya está verificado")
          ? "✅ Tu correo ya estaba verificado. Ve al login para iniciar sesión."
          : "⚠️ Ocurrió un error al verificar tu correo.",
      });

      if (msg?.includes("ya está verificado")) {
        setTimeout(() => navigate("/login"), 6000);
      }
    } finally {
      setIsVerifying(false);
    }
  };

  const handleResend = async () => {
    const email = localStorage.getItem("patient_email");
    if (!email) {
      setToast({
        show: true,
        type: "error",
        title: "Error",
        message: "⚠️ No se encontró el correo registrado.",
      });
      return;
    }

    try {
      const response = await resendVerificationCode(email);
      setToast({
        show: true,
        type: "info",
        title: "Código reenviado",
        message: response.data?.message || "📧 Código reenviado correctamente.",
      });
    } catch (err) {
      console.error("❌ Error reenviar:", err.response?.data || err);
      setToast({
        show: true,
        type: "error",
        title: "Error",
        message:
          err.response?.data?.message || "No se pudo reenviar el código.",
      });
    }
  };

  return (
    <div className="auth-bg min-vh-100 ">
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

      {/* 🔹 Overlay mientras redirige */}
      {showOverlay && (
        <div
          style={{
            position: "fixed",
            top: 0,
            left: 0,
            width: "100vw",
            height: "100vh",
            backgroundColor: "rgba(0, 0, 0, 0.6)",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            color: "white",
            zIndex: 2000,
            flexDirection: "column",
            backdropFilter: "blur(2px)",
          }}
        >
          <div className="spinner-border text-light mb-3" role="status" />
          <p>🔒 Redirigiendo al login, por favor espera...</p>
        </div>
      )}

      {/* 🔹 Toast — aparece por encima del overlay */}
      <div
        style={{
          position: "fixed",
          top: "1rem",
          right: "1rem",
          zIndex: 3000, // 🔸 más alto que el overlay
        }}
      >
        <CustomToast
          show={toast.show}
          onClose={() => setToast({ ...toast, show: false })}
          type={toast.type}
          title={toast.title}
          message={toast.message}
        />
      </div>
    </div>
  );
};

export default ConfirmEmailPage;
