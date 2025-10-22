import { Container, Button } from "react-bootstrap";
import "./HeroSection.css";

import { useNavigate } from "react-router-dom";

const HeroSection = () => {
  const navigate = useNavigate();
  return (
    <section className="hero-section text-center text-white d-flex align-items-center justify-content-center">
      <div className="hero-overlay"></div>
      <Container className="position-relative z-2">
        <h1 className="fw-bold display-4">MedTech</h1>
        <h2 className="fw-bold display-4">Revolucionando la Gestión Médica</h2>
        <p className="lead my-3">
          Nuestra plataforma unifica la gestión de citas, teleasistencia y
          expedientes clínicos para ofrecerte una experiencia médica sin igual.
        </p>
        <div className="d-flex justify-content-center gap-3 mt-4 flex-wrap">
          <Button
            variant="primary"
            size="lg"
            className="header-btn"
            onClick={() => navigate("/login")}
          >
            Iniciar sesión
          </Button>
          <Button
            variant="light"
            size="lg"
            className="header-btn text-white"
            onClick={() => navigate("/registro")}
          >
            Registrarme
          </Button>
        </div>
      </Container>
    </section>
  );
};

export default HeroSection;
