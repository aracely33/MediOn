import { Navigate, Outlet } from "react-router-dom";
import { usePatient } from "../context/PatientContext";

export const ProtectedRoutePatient = () => {
  const { patient, isAuthenticatedPatient, loadingPatient } = usePatient();

  console.log("🧩 Estado en ProtectedRoutePatient:", {
    isAuthenticatedPatient,
    loadingPatient,
    patient,
  });

  if (loadingPatient) {
    return (
      <div className="d-flex justify-content-center align-items-center vh-100">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">🌀Verificando sesión...</span>
        </div>
      </div>
    );
  }

  if (!isAuthenticatedPatient) {
    return <Navigate to={"/login"} replace />;
  }

  if (!patient || !patient.roles.includes("PATIENT")) {
    return (
      <div className="text-center text-danger">
        Accesso denegado: Sólo pueden acceder los pacientes
      </div>
    );
  }

  return <Outlet />;
};
