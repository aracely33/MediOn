import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import LoginPage from "./feature/auth/LoginPage";
import SignupPage from "./feature/auth/SignupPage";

function App() {
  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route path="/registro" element={<SignupPage />}></Route>
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
