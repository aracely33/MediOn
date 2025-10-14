import { useState } from "react";
import Header from "../../components/header/Header";
import Sidebar from "../../components/sidebar/Sidebar";
import DoctorScheduleCard from "../../components/doctorScheduleCard/DoctorScheduleCard";
import NotificationCard from "../../components/notificationCard/NotificationCard";
import AppointmentDetails from "../../components/appointmentDetail/AppointmentDetails";
import { Container, Row, Col } from "react-bootstrap";

const DoctorDashboard = () => {
  const user = {
    name: "Dr. Juan Pérez",
    age: 40,
    id: 456789,
    avatar: "https://randomuser.me/api/portraits/men/75.jpg",
  };

  const schedule = [
    {
      time: "10:00 AM",
      patient: "Sofía Sánchez",
      age: 25,
      gender: "Femenino",
      specialty: "Cardiología",
      status: "Confirmada",
      dateTime: "15 de Mayo, 2024 - 10:00 AM",
      motive: "Chequeo de presión arterial",
      isTeleconsultation: false,
      doctor: user.name,
    },
    {
      time: "11:30 AM",
      patient: "Luis Martínez",
      age: 45,
      gender: "Masculino",
      specialty: "Dermatología",
      status: "Pendiente",
      dateTime: "15 de Mayo, 2024 - 11:30 AM",
      motive: "Revisión de lunares",
      isTeleconsultation: true,
      doctor: user.name,
    },
    {
      time: "01:00 PM",
      patient: "Ana López",
      age: 28,
      gender: "Femenino",
      specialty: "Nutrición",
      status: "Confirmada",
      dateTime: "15 de Mayo, 2024 - 13:00 PM",
      motive: "Consulta de dieta personalizada",
      isTeleconsultation: true,
      doctor: user.name,
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

  // Estado para la cita seleccionada
  const [selectedAppointment, setSelectedAppointment] = useState(null);

  return (
    <div className="d-flex">
      <Sidebar user={user} role="doctor" />
      <div className="flex-grow-1">
        <Header
          title="Portal Médico"
          avatarUrl={user.avatar}
          onLogout={() => alert("Logout")}
        />
        <Container className="py-4">
          <h2>¡Bienvenido, {user.name}!</h2>

          <Row>
            {/* Columna izquierda: agenda */}
            <Col md={7}>
              <h4 className="mt-4">Mi Agenda de Hoy</h4>
              <Row>
                {schedule.map((item, idx) => (
                  <Col md={12} key={idx} className="mb-3">
                    <DoctorScheduleCard
                      {...item}
                      onSelect={() => setSelectedAppointment(item)}
                    />
                  </Col>
                ))}
              </Row>

              <h4 className="mt-5">Notificaciones</h4>
              <Row>
                {notifications.map((notif, idx) => (
                  <Col md={12} key={idx} className="mb-3">
                    <NotificationCard {...notif} />
                  </Col>
                ))}
              </Row>
            </Col>

            {/* Columna derecha: detalles de la cita seleccionada */}
            <Col md={5}>
              {selectedAppointment ? (
                <AppointmentDetails
                  name={selectedAppointment.patient}
                  age={selectedAppointment.age} // puedes hacerlo dinámico si tienes edad
                  gender={selectedAppointment.gender} // o dinámico
                  date={selectedAppointment.dateTime.split(" - ")[0]}
                  time={selectedAppointment.dateTime.split(" - ")[1]}
                  motive={selectedAppointment.motive}
                  isTeleconsultation={selectedAppointment.isTeleconsultation}
                  assignedTo={selectedAppointment.doctor}
                  role="doctor"
                />
              ) : (
                <p>Selecciona una cita para ver los detalles</p>
              )}
            </Col>
          </Row>
        </Container>
      </div>
    </div>
  );
};

export default DoctorDashboard;
