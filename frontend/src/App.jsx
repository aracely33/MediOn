import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import LoginPage from "./feature/auth/LoginPage";
import LandingPage from "./feature/landingP/LandingPage";
import { PatientProvider } from "./context/PatientContext";
import { ProtectedRoutePatient } from "./routes/ProtectedRoutePatient";
import SignupPage from "./feature/auth/SignupPage";
import ConfirmEmailPage from "./feature/auth/ConfirmEmailPage";
import PatientDashboard from "./feature/Patient/PatientDashboard";
import DoctorDashboard from "./feature/Doctor/DoctorDashboard";
import AppointmentBook from "./feature/Patient/appointmentBook/AppointmentBook";
import { DoctorProvider } from "./context/DoctorContext";
import { AppointmentProvider } from "./context/AppointmentContext";

function App() {
  return (
    <BrowserRouter>
      <AppointmentProvider>
        <PatientProvider>
          <DoctorProvider>
            <Routes>
              <Route path="/" element={<LandingPage />} />
              <Route path="/login" element={<LoginPage />} />
              <Route path="/registro" element={<SignupPage />} />
              <Route path="/confirm-email" element={<ConfirmEmailPage />} />

              {/*Lo pongo aquí provisionalmente para probar la vista*/}
              <Route path="/doctor-home" element={<DoctorDashboard />} />

              <Route element={<ProtectedRoutePatient />}>
                {/*Se agregan las rutas que solo puede acceder el paciente*/}

                {/* Dashboards */}
                <Route path="/patient-home" element={<PatientDashboard />} />
                <Route
                  path="/patient-home/appointment-book"
                  element={<AppointmentBook />}
                />
              </Route>
            </Routes>
          </DoctorProvider>
        </PatientProvider>
      </AppointmentProvider>
    </BrowserRouter>
  );
}

export default App;
