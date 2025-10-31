// src/features/auth/SignupPage.jsx
import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { Container, Row, Col, Card } from "react-bootstrap";
import SignupForm from "./components/SignupForm";
import TermsModal from "./components/TermsModal";
import PrivacyModal from "./components/PrivacyModal";
import CustomToast from "./components/CustomToast";
import logo from "../../assets/logoMed.svg";
import "./auth.css";

const SignupPage = () => {
  const [showTerms, setShowTerms] = useState(false);
  const [showPrivacy, setShowPrivacy] = useState(false);
  const [showOverlay, setShowOverlay] = useState(false);
  const [toast, setToast] = useState({
    show: false,
    type: "success",
    title: "",
    message: "",
  });
  const navigate = useNavigate();

  // Función para mostrar cualquier tipo de toast
  const showToast = (type, title, message) => {
    setToast({ show: true, type, title, message });
  };

  return (
    <div className="auth-bg min-vh-100 d-flex flex-column position-relative">
      <Container
        fluid
        className="flex-grow-1 d-flex align-items-center justify-content-center"
      >
        <Row className="w-100 justify-content-center">
          <Col xs={11} sm={9} md={7} lg={5} xl={4}>
            <div className="text-center m-4 justify-content-around align-items-center">
              <Link to="/">
                <img
                  src={logo}
                  alt="MedTech Logo"
                  className="img-fluid " // 🔹 hace que la imagen sea responsive y redondeada
                  style={{
                    maxWidth: "150px", // tamaño máximo
                    width: "90%", // se ajusta proporcionalmente al contenedor
                    cursor: "pointer",
                    borderRadius: "30px",
                  }}
                />
              </Link>
            </div>

            <Card className="shadow-sm border-0 rounded-4">
              <Card.Body className="p-4">
                <Card.Title className="mb-3 fw-bold fs-4 text-dark text-center">
                  Crear cuenta
                </Card.Title>
                <Card.Subtitle className="mb-4 text-muted text-center">
                  Únete a nuestra comunidad de pacientes y doctores.
                </Card.Subtitle>

                <SignupForm
                  onSuccess={(status) => {
                    if (status === "loading") {
                      setShowOverlay(true); // 🔹 aparece de inmediato
                      return;
                    }

                    if (status === "success") {
                      showToast(
                        "success",
                        "Registro exitoso 🎉",
                        "¡Tu cuenta ha sido creada correctamente!"
                      );

                      setTimeout(() => {
                        setShowOverlay(false);
                        navigate("/confirm-email");
                      }, 1000); // 🔹 deja ver el toast antes de redirigir
                    }

                    if (status === "error") {
                      setShowOverlay(false);
                      showToast(
                        "error",
                        "Error 😕",
                        "No se pudo crear tu cuenta."
                      );
                    }
                  }}
                  onShowTerms={() => setShowTerms(true)}
                  onShowPrivacy={() => setShowPrivacy(true)}
                />
              </Card.Body>
            </Card>

            <footer className="text-center m-4 text-light small">
              © 2025 HealthConnect. Todos los derechos reservados.{" "}
              <a href="#">Política de Privacidad</a>
            </footer>
          </Col>
        </Row>
      </Container>

      {/* Overlay de carga */}
      {showOverlay && (
        <div
          style={{
            position: "fixed",
            top: 0,
            left: 0,
            width: "100vw",
            height: "100vh",
            backgroundColor: "rgba(0, 0, 0, 0.6)",
            zIndex: 2000,
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            flexDirection: "column",
            color: "white",
            backdropFilter: "blur(2px)",
          }}
        >
          <div className="spinner-border text-light mb-3" role="status" />
          <p>🔒 Procesando tu registro...</p>
        </div>
      )}

      {/* Toast por encima del overlay */}
      <div
        style={{
          position: "fixed",
          top: "1rem",
          right: "1rem",
          zIndex: 3000,
        }}
      >
        <CustomToast
          show={toast.show}
          onClose={() => setToast({ ...toast, show: false })}
          type={toast.type}
          title={toast.title}
          message={toast.message}
        />
      </div>

      <TermsModal show={showTerms} onHide={() => setShowTerms(false)} />
      <PrivacyModal show={showPrivacy} onHide={() => setShowPrivacy(false)} />
    </div>
  );
};

export default SignupPage;
