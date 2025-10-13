import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import LoginPage from "./feature/auth/LoginPage";
import SignupPage from "./feature/auth/SignupPage";
import PatientDashboard from "./feature/auth/PatientDashboard";
import DoctorDashboard from "./feature//auth/DoctorDashboard";

function App() {
  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route path="/registro" element={<SignupPage />}></Route>
          {/* Dashboards de prueba */}
          <Route path="/doctor-dashboard" element={<DoctorDashboard />} />
          <Route path="/paciente-dashboard" element={<PatientDashboard />} />
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
