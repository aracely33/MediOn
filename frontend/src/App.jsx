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
import ForgotPasswordPage from "./feature/auth/ForgotPasswordPage";
import ConfigurationProfile from "./feature/auth/ConfigurationProfile";
import { DoctorProvider } from "./context/DoctorContext";
import { AppointmentProvider } from "./context/AppointmentContext";
import MedicalHistory from "./feature/Patient/EHR/MedicalHistory";
import MessagesAndAlerts from "./feature/Patient/Messages/MessagesAndAlerts";

function App() {
  return (
    <BrowserRouter>
      <AppointmentProvider>
        <PatientProvider>
          <DoctorProvider>
            <Routes>
              <Route path="/" element={<LandingPage />} />
              <Route path="/login" element={<LoginPage />} />
              <Route path="/signup" element={<SignupPage />} />
              <Route path="/confirm-email" element={<ConfirmEmailPage />} />
              <Route path="/forgot-password" element={<ForgotPasswordPage />} />

              {/*Lo pongo aquí provisionalmente para probar la vista*/}
              <Route path="/doctor-home" element={<DoctorDashboard />} />

              <Route element={<ProtectedRoutePatient />}>
                {/*Se agregan las rutas que solo puede acceder el paciente*/}
                {/* Dashboards */}
                <Route path="/patient-home" element={<PatientDashboard />} />
                <Route path="/patient-history" element={<MedicalHistory />} />
                <Route
                  path="/patient-messages"
                  element={<MessagesAndAlerts />}
                />

                <Route
                  path="/patient-home/patient-appointments"
                  element={<AppointmentBook />}
                />
                <Route
                  path="/patient-home/patient-config"
                  element={<ConfigurationProfile />}
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
