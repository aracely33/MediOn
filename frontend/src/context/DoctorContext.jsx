import { createContext, useContext, useEffect, useState } from "react";
import { loginUser, logoutUser } from "../feature/auth/services/authService";
import { api } from "../services/api";

export const DoctorContext = createContext();

export const useDoctor = () => {
  const context = useContext(DoctorContext);
  if (!context) {
    throw new Error("useDoctor must be used within a DoctorProvider");
  }
  return context;
};

export const DoctorProvider = ({ children }) => {
  const [doctor, setDoctor] = useState(null);
  const [isAuthenticatedDoctor, setIsAuthenticatedDoctor] = useState(false);
  const [loadingDoctor, setLoadingDoctor] = useState(true);

  const signIn = async (doctorData) => {
    try {
      const response = await loginUser(doctorData);
      const { token } = response.data;
      localStorage.setItem("professional_token", token);

      const meResponse = await api.get("/auth/me", {
        headers: { Authorization: `Bearer ${token}` },
      });

      setDoctor(meResponse.data);
      setIsAuthenticatedDoctor(true);
    } catch (error) {
      console.error("Error al iniciar sesión doctor:", error);
    }
  };

  const signOut = async () => {
    try {
      await logoutUser();
    } catch (error) {
      console.error("Error al cerrar sesión doctor:", error);
    } finally {
      localStorage.removeItem("professional_token");
      setDoctor(null);
      setIsAuthenticatedDoctor(false);
    }
  };

  useEffect(() => {
    async function reviewLogin() {
      setLoadingDoctor(true);
      const token = localStorage.getItem("professional_token");

      if (!token) {
        setDoctor(null);
        setIsAuthenticatedDoctor(false);
        setLoadingDoctor(false);
        return;
      }

      try {
        const response = await api.get("/auth/me", {
          headers: { Authorization: `Bearer ${token}` },
        });
        setDoctor(response.data);
        setIsAuthenticatedDoctor(true);
      } catch (error) {
        signOut();
      } finally {
        setLoadingDoctor(false);
      }
    }

    reviewLogin();
  }, []);

  return (
    <DoctorContext.Provider
      value={{
        doctor,
        signIn,
        signOut,
        isAuthenticatedDoctor,
        loadingDoctor,
      }}
    >
      {children}
    </DoctorContext.Provider>
  );
};
