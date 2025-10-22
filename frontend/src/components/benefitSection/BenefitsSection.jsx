// src/components/benefits/BenefitsSection.jsx
import { Container, Row, Col } from "react-bootstrap";
import { CheckCircle } from "react-bootstrap-icons";
import "./BenefitsSection.css";

const BenefitsSection = () => (
  <section id="beneficios" className="py-5">
    <Container>
      <Row>
        <Col md={6}>
          <h3 className="fw-bold mb-3 text-center">Beneficios para Clínicas</h3>
          <ul className="text-center">
            <li>
              <CheckCircle /> Optimización de agenda y reducción de tiempos
              muertos.
            </li>
            <li>
              <CheckCircle /> Recordatorios automáticos que reducen ausentismo.
            </li>
            <li>
              <CheckCircle /> Comunicación centralizada con tus pacientes.
            </li>
          </ul>
        </Col>
        <Col md={6}>
          <h3 className="fw-bold mb-3 text-center">
            Beneficios para Pacientes
          </h3>
          <ul className="text-center">
            <li>
              <CheckCircle /> Agendamiento 24/7 desde cualquier dispositivo.
            </li>
            <li>
              <CheckCircle /> Acceso a información médica desde cualquier lugar.
            </li>
            <li>
              <CheckCircle /> Comodidad y seguridad con teleasistencia.
            </li>
          </ul>
        </Col>
      </Row>
    </Container>
  </section>
);

export default BenefitsSection;
