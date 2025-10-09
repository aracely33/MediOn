import { Navigate, Outlet } from "react-router-dom";
import { usePatient } from "../context/PatientContext";

export const ProtectedRoutePatient = () => {
  const { patient, isAuthenticatedPatient } = usePatient();

  if (!isAuthenticatedPatient) {
    return <Navigate to={"/login"} replace />;
  }

  if (!patient || patient.roles !== "PATIENT") {
    return (
      <div className="text-center text-danger">
        Accesso denegado: Sólo pueden acceder los pacientes
      </div>
    );
  }

  return <Outlet />;
};
