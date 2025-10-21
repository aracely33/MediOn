// src/components/features/FeaturesSection.jsx
import { Container, Row, Col, Card } from "react-bootstrap";
import {
  Calendar2Week,
  CameraVideo,
  ChatDots,
  Laptop,
} from "react-bootstrap-icons";
import "./FeatureSection.css";

const features = [
  {
    icon: Calendar2Week,
    title: "Citas Online",
    desc: "Agenda, modifica y cancela tus citas desde la comodidad de tu hogar.",
  },
  {
    icon: ChatDots,
    title: "Chat Directo",
    desc: "Comunícate directamente con tu médico de forma segura y confidencial.",
  },
  {
    icon: CameraVideo,
    title: "Videollamada Segura",
    desc: "Realiza consultas médicas virtuales con la más alta calidad.",
  },
  {
    icon: Laptop,
    title: "Expediente Electrónico",
    desc: "Accede a tu historial médico completo en cualquier momento y lugar.",
  },
];

const FeaturesSection = () => {
  return (
    <section id="caracteristicas" className="py-5">
      <Container>
        <h2 className="text-center mb-4 fw-bold text-white">
          Características Principales
        </h2>
        <Row className="g-4">
          {features.map((feature, i) => (
            <Col key={i} md={6} lg={3}>
              <Card className="h-100 text-center p-3 shadow-sm">
                <div className="d-flex justify-content-center">
                  <feature.icon className="text-primary fs-1 mb-2" />
                </div>
                <Card.Title>{feature.title}</Card.Title>
                <Card.Text>{feature.desc}</Card.Text>
              </Card>
            </Col>
          ))}
        </Row>
      </Container>
    </section>
  );
};

export default FeaturesSection;
