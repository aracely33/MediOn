import { useState } from "react";
import Header from "../../components/header/Header";
import Sidebar from "../../components/sidebar/Sidebar";
import DoctorScheduleCard from "../../components/doctorScheduleCard/DoctorScheduleCard";
import NotificationCard from "../../components/notificationCard/NotificationCard";
import CalendarView from "../../components/calendarView/calendarView";
import AppointmentDetails from "../../components/appointmentDetail/AppointmentDetails";
import { Container, Row, Col, Button } from "react-bootstrap";

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
      dateTime: "2025-10-14T15:00:00", // ✅ formato válido
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
      dateTime: "2025-10-14T17:30:00", // ✅ formato válido
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
      dateTime: "2025-10-14T18:00:00", // ✅ formato válido
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

  const [selectedAppointment, setSelectedAppointment] = useState(null);
  const [showCalendar, setShowCalendar] = useState(false);

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

          <div className="d-flex justify-content-between align-items-center mt-4">
            <h4>{showCalendar ? "Calendario de Citas" : "Mi Agenda de Hoy"}</h4>
            <Button
              variant="primary"
              onClick={() => setShowCalendar(!showCalendar)}
            >
              {showCalendar ? "Ver Agenda" : "Ver Calendario"}
            </Button>
          </div>

          {!showCalendar ? (
            <Row className="mt-3">
              <Col md={7}>
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

              <Col md={5}>
                {selectedAppointment ? (
                  <AppointmentDetails
                    name={selectedAppointment.patient}
                    age={selectedAppointment.age}
                    gender={selectedAppointment.gender}
                    date={new Date(selectedAppointment.dateTime).toLocaleString(
                      "es-MX",
                      {
                        dateStyle: "long",
                        timeStyle: "short",
                      }
                    )}
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
          ) : (
            <CalendarView
              appointments={schedule}
              onSelectEvent={(appt) => setSelectedAppointment(appt)}
            />
          )}
        </Container>
      </div>
    </div>
  );
};

export default DoctorDashboard;
