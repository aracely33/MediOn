import { useState, useEffect } from "react";
import { Container, Row, Col, Button } from "react-bootstrap";
import { List } from "react-bootstrap-icons";
import Header from "../../components/header/Header";
import Sidebar from "../../components/sidebar/Sidebar";
import DoctorScheduleCard from "../../components/doctorScheduleCard/DoctorScheduleCard";
import NotificationCard from "../../components/notificationCard/NotificationCard";
import CalendarView from "../../components/calendarView/calendarView";
import AppointmentDetails from "../../components/appointmentDetail/AppointmentDetails";
import { getProfessionalById } from "./doctorService";
import "./DoctorDashboard.css";

const DoctorDashboard = () => {
  // Estado para el doctor
  const [doctor, setDoctor] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedAppointment, setSelectedAppointment] = useState(null);
  const [showCalendar, setShowCalendar] = useState(false);
  const [showSidebar, setShowSidebar] = useState(false);

  // Simulación temporal del ID del médico
  const doctorId = 6;

  useEffect(() => {
    const fetchDoctorData = async () => {
      try {
        const data = await getProfessionalById(doctorId);
        setDoctor(data);
      } catch (error) {
        console.error("Error al obtener los datos del doctor:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchDoctorData();
  }, []);

  // Datos temporales de citas y notificaciones
  const schedule = [
    {
      time: "10:00 AM",
      patient: "Sofía Sánchez",
      age: 25,
      gender: "Femenino",
      specialty: "Cardiología",
      status: "Confirmada",
      dateTime: "2025-10-14T15:00:00",
      motive: "Chequeo de presión arterial",
      isTeleconsultation: false,
      doctor: doctor?.name,
    },
    {
      time: "11:30 AM",
      patient: "Luis Martínez",
      age: 45,
      gender: "Masculino",
      specialty: "Dermatología",
      status: "Pendiente",
      dateTime: "2025-10-14T17:30:00",
      motive: "Revisión de lunares",
      isTeleconsultation: true,
      doctor: doctor?.name,
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

  const handleLogout = () => alert("Logout");

  if (loading) return <p>Cargando información del médico...</p>;
  if (!doctor) return <p>No se pudo cargar la información del médico.</p>;

  return (
    <div className="d-flex doctor-dashboard">
      {/* Sidebar retráctil */}
      <Sidebar
        user={{
          name: doctor.name,
          avatar:
            doctor.avatarUrl ||
            "https://cdn-icons-png.flaticon.com/512/387/387561.png",
        }}
        role="doctor"
        show={showSidebar}
        onHide={() => setShowSidebar(false)}
      />

      <div className="flex-grow-1">
        <Header
          title="Portal Médico"
          avatarUrl={
            doctor.avatarUrl ||
            "https://cdn-icons-png.flaticon.com/512/387/387561.png"
          }
          onLogout={handleLogout}
        />

        {/* Botón hamburguesa visible solo en móviles */}
        <div className="d-lg-none p-2">
          <Button
            variant="outline-primary"
            onClick={() => setShowSidebar(true)}
          >
            <List size={24} />
          </Button>
        </div>

        <Container className="py-4">
          <h2>¡Bienvenido, {doctor.name}!</h2>

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
                      { dateStyle: "long", timeStyle: "short" }
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
