import { Navigate, Outlet } from "react-router-dom";
import { useDoctor } from "../context/DoctorContext";

export const ProtectedRouteDoctor = () => {
  const { doctor, isAuthenticatedDoctor, loadingDoctor } = useDoctor();

  console.log("🧩 Estado en ProtectedRouteDoctor:", {
    isAuthenticatedDoctor,
    loadingDoctor,
    doctor,
  });

  if (loadingDoctor) {
    return (
      <div className="d-flex justify-content-center align-items-center vh-100">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">🌀Verificando sesión...</span>
        </div>
      </div>
    );
  }

  if (!isAuthenticatedDoctor) {
    return <Navigate to={"/login"} replace />;
  }

  if (!doctor || !doctor.roles.includes("PROFESSIONAL")) {
    return (
      <div className="text-center text-danger">
        Accesso denegado: Sólo pueden acceder los doctores
      </div>
    );
  }

  return <Outlet />;
};
