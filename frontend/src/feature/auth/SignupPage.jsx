// src/features/auth/SignupPage.jsx
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Container, Row, Col, Card } from "react-bootstrap";
import SignupForm from "./components/SignupForm";
import TermsModal from "./components/TermsModal";
import PrivacyModal from "./components/PrivacyModal";
import SuccessToast from "./components/SuccessToast";

import "./auth.css";

const SignupPage = () => {
  const [showTerms, setShowTerms] = useState(false);
  const [showPrivacy, setShowPrivacy] = useState(false);
  const [showToast, setShowToast] = useState(false);
  const navigate = useNavigate();
  return (
    <div className="auth-bg">
      <Container
        fluid
        className="vh-100 d-flex align-items-center justify-content-center"
      >
        <Row className="w-100 justify-content-center">
          <Col xs={11} sm={9} md={7} lg={5} xl={4}>
            <div className="text-center mb-4">
              <h1 className="brand-title">🩺 HealthConnect</h1>
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
                  onSuccess={() => setShowToast(true)}
                  onShowTerms={() => setShowTerms(true)}
                  onShowPrivacy={() => setShowPrivacy(true)}
                />
              </Card.Body>
            </Card>

            <footer className="text-center mt-4 text-light small">
              © 2025 HealthConnect. Todos los derechos reservados.{" "}
              <a href="#">Política de Privacidad</a>
            </footer>
          </Col>
        </Row>
      </Container>

      <TermsModal show={showTerms} onHide={() => setShowTerms(false)} />
      <PrivacyModal show={showPrivacy} onHide={() => setShowPrivacy(false)} />
      <SuccessToast show={showToast} onClose={() => setShowToast(false)} />
    </div>
  );
};

export default SignupPage;
