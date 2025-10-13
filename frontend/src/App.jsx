import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import LoginPage from "./feature/auth/LoginPage";
import { PatientProvider } from "./context/PatientContext";
import { ProtectedRoutePatient } from "./routes/ProtectedRoutePatient";
import SignupPage from "./feature/auth/SignupPage";
import ConfirmEmailPage from "./feature/auth/ConfirmEmailPage";
import DashboardPage from "./feature/auth/dashboard/DashboardPage";

function App() {
  return (
    <BrowserRouter>
      <PatientProvider>
        <Routes>
          {/* Rutas públicas */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/registro" element={<SignupPage />} />
          <Route path="/confirm-email" element={<ConfirmEmailPage />} />

          {/* Rutas privadas */}
          <Route element={<ProtectedRoutePatient />}>
            {/*Se agregan las rutas que solo puede acceder el paciente*/}
            <Route path="/dashboard" element={<DashboardPage />} />
          </Route>
        </Routes>
      </PatientProvider>
    </BrowserRouter>
  );
}

export default App;
