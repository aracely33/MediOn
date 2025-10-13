import Header from "./components/Header";
import Sidebar from "./components/Sidebar";
import DoctorScheduleCard from "../auth/components/DoctorScheduleCard";
import NotificationCard from "../auth/components/NotificationCard";
import { Container, Row, Col } from "react-bootstrap";

const DoctorDashboard = () => {
  const user = {
    name: "Dr. Juan Pérez",
    id: 456789,
    avatar: "https://randomuser.me/api/portraits/men/75.jpg",
    links: [
      { label: "Panel de Control", href: "#", icon: "dashboard", active: true },
      { label: "Mi Agenda", href: "#", icon: "calendar_month" },
      { label: "Pacientes", href: "#", icon: "people" },
      { label: "Mensajes y Alertas", href: "#", icon: "notifications" },
      { label: "Configuración", href: "#", icon: "settings" },
    ],
  };

  const schedule = [
    {
      time: "10:00 AM",
      patient: "Sofía Sánchez",
      specialty: "Cardiología",
      status: "Confirmada",
    },
    {
      time: "11:30 AM",
      patient: "Luis Martínez",
      specialty: "Dermatología",
      status: "Pendiente",
    },
    {
      time: "01:00 PM",
      patient: "Ana López",
      specialty: "Nutrición",
      status: "Confirmada",
    },
  ];

  const notifications = [
    {
      title: "Nueva cita agendada",
      description: "Carlos Rivas ha reservado una cita a las 15:00 PM.",
    },
    {
      title: "Paciente canceló cita",
      description: "María Fernández canceló su cita de mañana.",
    },
  ];

  return (
    <div className="d-flex">
      <Sidebar user={user} />
      <div className="flex-grow-1">
        <Header
          title="Portal Médico"
          avatarUrl={user.avatar}
          onLogout={() => alert("Logout")}
        />
        <Container className="py-4">
          <h2>¡Bienvenido, {user.name}!</h2>

          <h4 className="mt-4">Mi Agenda de Hoy</h4>
          <Row>
            {schedule.map((item, idx) => (
              <Col md={6} lg={4} key={idx}>
                <DoctorScheduleCard {...item} />
              </Col>
            ))}
          </Row>

          <h4 className="mt-5">Notificaciones</h4>
          <Row>
            {notifications.map((notif, idx) => (
              <Col md={6} lg={4} key={idx}>
                <NotificationCard {...notif} />
              </Col>
            ))}
          </Row>
        </Container>
      </div>
    </div>
  );
};

export default DoctorDashboard;
