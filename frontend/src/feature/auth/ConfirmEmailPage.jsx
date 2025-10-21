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
  const handleVerify = async (code) => {
    try {
      const token = localStorage.getItem("patient_token");

      if (!token) {
        alert("⚠️ No se encontró token, inicia sesión o regístrate de nuevo.");
        return;
      }

      const response = await verifyEmailCode(code, code); // solo código
      const { status, message } = response.data;

      if (status) {
        alert(message || "✅ ¡Correo verificado correctamente!");
        await reviewLogin(); // aquí sí obtienes token al hacer login
        navigate("/patient-home");
      } else {
        alert("❌ Código inválido o expirado.");
      }
    } catch (error) {
      console.error("Error al verificar el código:", error);
      alert("⚠️ Ocurrió un error al verificar tu correo.");
    }
  };

  const handleResend = async () => {
    const email = localStorage.getItem("patient_email");
    if (!email) {
      alert("⚠️ No se encontró el correo registrado.");
      return;
    }

    try {
      await resendVerificationCode(email);
      alert("📧 Se ha reenviado el código a tu correo.");
    } catch (error) {
      console.error("Error al reenviar código:", error);
      alert("⚠️ No se pudo reenviar el código.");
    }
  };

  return (
    <div>
      <VerificationForm onVerify={handleVerify} onResend={handleResend} />
    </div>
  );
};

export default ConfirmEmailPage;
