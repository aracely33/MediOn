import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import LoginPage from "./feature/auth/LoginPage";
import { PatientProvider } from "./context/PatientContext";
import { ProtectedRoutePatient } from "./routes/ProtectedRoutePatient";
import SignupPage from "./feature/auth/SignupPage";
import PatientDashboard from "./feature/auth/PatientDashboard";
import DoctorDashboard from "./feature//auth/DoctorDashboard";

function App() {
  return (
    <>
      <PatientProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<LoginPage />} />
            <Route path="/registro" element={<SignupPage />} />
            <Route element={<ProtectedRoutePatient />}>
              {/*Se agregan las rutas que solo puede acceder el paciente*/}
              {/* Dashboards de prueba */}
              <Route path="/doctor-dashboard" element={<DoctorDashboard />} />
              <Route
                path="/paciente-dashboard"
                element={<PatientDashboard />}
              />
            </Route>
          </Routes>
        </BrowserRouter>
      </PatientProvider>
    </>
  );
}

export default App;
