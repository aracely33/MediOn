import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import LoginPage from "./feature/auth/LoginPage";
import { PatientProvider } from "./context/PatientContext";
import { ProtectedRoutePatient } from "./routes/ProtectedRoutePatient";
import SignupPage from "./feature/auth/SignupPage";

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
            </Route>
          </Routes>
        </BrowserRouter>
      </PatientProvider>
    </>
  );
}

export default App;
