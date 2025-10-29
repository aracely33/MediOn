import { useState } from "react";
import { Container, Row, Col, Card, Button } from "react-bootstrap";
import Header from "../../../components/header/Header";
import Sidebar from "../../../components/sidebar/Sidebar";
import { usePatient } from "../../../context/PatientContext";

const MessagesAndAlerts = () => {
  const { signOut } = usePatient();
  const [showSidebar, setShowSidebar] = useState(false);

  const handleLogout = async () => {
    await signOut();
    window.location.href = "/login";
  };

  return (
    <div className="d-flex patient-dashboard">
      {/* Sidebar */}
      <Sidebar
        user={{
          id: 1,
          name: "Juan",
          lastName: "Pérez",
          avatar: "https://cdn-icons-png.flaticon.com/512/149/149071.png",
        }}
        role="patient"
        show={showSidebar}
        onHide={() => setShowSidebar(false)}
      />

      {/* Contenido principal */}
      <div className="flex-grow-1">
        <Header
          title="MedTech: Tu portal de salud"
          avatarUrl="https://cdn-icons-png.flaticon.com/512/149/149071.png"
          buttons={[
            {
              label: "Cerrar sesión",
              onClick: handleLogout,
              className: "header-btn",
            },
          ]}
        />

        {/* Botón hamburguesa para móvil */}
        <div className="d-lg-none p-2">
          <Button
            variant="outline-primary"
            onClick={() => setShowSidebar(true)}
          >
            ☰
          </Button>
        </div>

        <Container className="py-4">
          <h2 className="mb-4">Mensajes y Alertas del Paciente</h2>

          {/* Tarjeta de alerta de ejemplo */}
          <Card className="p-3 mb-3 shadow-sm">
            <Row>
              <Col>
                <h5 className="fw-bold">Alerta de ejemplo</h5>
                <p className="text-muted mb-0">
                  Esta es una alerta de prueba para el paciente.
                </p>
              </Col>
            </Row>
          </Card>
        </Container>
      </div>
    </div>
  );
};

export default MessagesAndAlerts;
