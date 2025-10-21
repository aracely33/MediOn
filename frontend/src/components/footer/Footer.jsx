// src/components/footer/Footer.jsx
import { Container, Row, Col } from "react-bootstrap";

const Footer = () => (
  <footer className="bg-light py-4 mt-5 border-top">
    <Container>
      <Row className="text-center text-md-start">
        <Col md={6}>
          <h5 className="fw-bold">Plataforma Médica</h5>
          <p className="text-muted small">
            Innovando la salud, conectando pacientes y profesionales.
          </p>
        </Col>
        <Col md={3}>
          <h6 className="fw-bold">Navegación</h6>
          <ul className="list-unstyled small">
            <li>
              <a href="#">Sobre Nosotros</a>
            </li>
            <li>
              <a href="#">Contacto</a>
            </li>
            <li>
              <a href="#">FAQs</a>
            </li>
          </ul>
        </Col>
        <Col md={3}>
          <h6 className="fw-bold">Legal</h6>
          <ul className="list-unstyled small">
            <li>
              <a href="#">Política de Privacidad</a>
            </li>
            <li>
              <a href="#">Términos de Servicio</a>
            </li>
          </ul>
        </Col>
      </Row>
      <hr />
      <p className="text-center small text-muted mb-0">
        © 2025 Plataforma Médica. Todos los derechos reservados.
      </p>
    </Container>
  </footer>
);

export default Footer;
