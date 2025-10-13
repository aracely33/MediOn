import Header from "./components/Header";
import Sidebar from "./components/Sidebar";
import CardAppointment from "./components/CardAppointment";
import { Container, Row, Col } from "react-bootstrap";

const PatientDashboard = () => {
  const user = {
    name: "Sofía Sánchez",
    id: 789123,
    avatar:
      "https://lh3.googleusercontent.com/aida-public/AB6AXuDUYbpIPUHkmvCQgSP8TXDFj3_qFgcw7EkbNkIpKcguT2aaEougplE9ZVRo7AvkOudTs__NOpqWc3xr_P7qOuM0CN5NaKFgDGdcA_45P6EpScJ0m5mdv8ejKg3H_j7Icadd1zgeMPXFnUHKuBDKiOyAvsz-qE76pBkq2ehA5owS-Y00XxErSgJ7c4aj8_eEnKbftusSpRIm7hgaiEA6UHD3KdaLRzJQoY3Xb2L2wE0ZhlWCbRe_tTrNNhDw2sFxE_sFob6XuwGAt3M",
    links: [
      { label: "Panel de Control", href: "#", icon: "dashboard", active: true },
      { label: "Mis Citas", href: "#", icon: "calendar_month" },
      { label: "Historial Médico", href: "#", icon: "medical_information" },
      { label: "Mensajes y Alertas", href: "#", icon: "notifications" },
      { label: "Configuración", href: "#", icon: "settings" },
    ],
  };

  const appointments = [
    {
      dateTime: "15 de Mayo, 2024 - 10:30 AM",
      doctor: "Dr. Carlos Rivas",
      specialty: "Cardiología",
      clinic: "Clínica Corazón Sano",
    },
    {
      dateTime: "22 de Mayo, 2024 - 09:00 AM",
      doctor: "Dra. Elena Gómez",
      specialty: "Dermatología",
      clinic: "Centro Médico Piel Bella",
    },
  ];

  return (
    <div className="d-flex">
      <Sidebar user={user} />
      <div className="flex-grow-1">
        <Header
          title="Portal de Salud"
          avatarUrl={user.avatar}
          onLogout={() => alert("Logout")}
        />
        <Container className="py-4">
          <h2>¡Bienvenida, {user.name}!</h2>
          <Row>
            {appointments.map((appt, idx) => (
              <Col md={6} lg={4} key={idx}>
                <CardAppointment {...appt} />
              </Col>
            ))}
          </Row>
        </Container>
      </div>
    </div>
  );
};

export default PatientDashboard;
