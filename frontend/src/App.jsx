import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import LoginPage from "./feature/auth/LoginPage";
import { PatientProvider } from "./context/PatientContext";
import Dashboard from "./feature/Dashboard";
import { ProtectedRoutePatient } from "./routes/ProtectedRoutePatient";

function App() {
  return (
    <>
      <PatientProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<LoginPage />} />
            <Route element={<ProtectedRoutePatient />}>
              <Route path="/dashboard" element={<Dashboard />} />
            </Route>
          </Routes>
        </BrowserRouter>
      </PatientProvider>
    </>
  );
}

export default App;
