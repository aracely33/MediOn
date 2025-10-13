import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import LoginPage from "./feature/auth/LoginPage";
import { PatientProvider } from "./context/PatientContext";
import { ProtectedRoutePatient } from "./routes/ProtectedRoutePatient";
import SignupPage from "./feature/auth/SignupPage";
import PatientDashboard from "./feature/Patient/PatientDashboard";
import DoctorDashboard from "./feature/Doctor/DoctorDashboard";

function App() {
  return (
    <>
      <PatientProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<LoginPage />} />
            <Route path="/registro" element={<SignupPage />} />

            {/*Lo pongo aquí provisionalmente para probar la vista*/}
            <Route path="/doctor-home" element={<DoctorDashboard />} />

            <Route element={<ProtectedRoutePatient />}>
              {/*Se agregan las rutas que solo puede acceder el paciente*/}

              {/* Dashboards */}
              <Route path="/patient-home" element={<PatientDashboard />} />
            </Route>
          </Routes>
        </BrowserRouter>
      </PatientProvider>
    </>
  );
}

export default App;
