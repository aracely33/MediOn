import { useState, useEffect } from "react";
import { Container, Row, Col, Button } from "react-bootstrap";
import { List } from "react-bootstrap-icons"; // ícono hamburguesa
import Header from "../../components/header/Header";
import Sidebar from "../../components/sidebar/Sidebar";
import CardAppointment from "../../components/cardAppointment/CardAppointment";
import AppointmentDetails from "../../components/appointmentDetail/AppointmentDetails";
import NotificationCard from "../../components/notificationCard/NotificationCard";
import { useNavigate } from "react-router-dom";
import { usePatient } from "../../context/PatientContext";
import { getMe, getPatientById } from "./patientService";
import "./PatientDashboard.css";

const PatientDashboard = () => {
  const { signOut } = usePatient();
  const navigate = useNavigate();
  const [showSidebar, setShowSidebar] = useState(false);
  const [user, setUser] = useState(null);
  const [selectedAppointment, setSelectedAppointment] = useState(null);
  const [notification, setNotification] = useState(null);

  // Cargar usuario real desde backend
  useEffect(() => {
    const fetchUser = async () => {
      try {
        const me = await getMe();
        const patient = await getPatientById(me.id);

        setUser({
          id: patient.id,
          name: patient.name,
          lastName: patient.lastName,
          email: patient.email,
          age: patient.birthDate
            ? Math.floor(
                (new Date() - new Date(patient.birthDate)) /
                  (1000 * 60 * 60 * 24 * 365)
              )
            : null,
          gender: patient.gender || "No especificado",
          avatar: "https://cdn-icons-png.flaticon.com/512/149/149071.png", // avatar genérico
        });
      } catch (error) {
        console.error(error);
      }
    };

    fetchUser();
  }, []);

  const appointments = [
    {
      dateTime: "15 de Mayo, 2025 - 10:30 AM",
      doctor: "Dr. Carlos Rivas",
      specialty: "Cardiología",
      clinic: "Clínica Corazón Sano",
      motive: "Chequeo de presión arterial",
      isTeleconsultation: false,
    },
    {
      dateTime: "22 de Mayo, 2026 - 09:00 AM",
      doctor: "Dra. Elena Gómez",
      specialty: "Dermatología",
      clinic: "Centro Médico Piel Bella",
      motive: "Revisión de lunares",
      isTeleconsultation: true,
    },
  ];

  const mensajes = [
    {
      title: "Recordatorio de cita",
      description: "Se acerca su próxima cita.",
    },
    {
      title: "Nuevo mensaje",
      description:
        "La especialidad de neumología ya tiene teleconsultas disponibles.",
    },
  ];

  useEffect(() => {
    let index = 0;
    const showNextMessage = () => {
      setNotification(mensajes[index]);
      index = (index + 1) % mensajes.length;
    };
    const timer = setTimeout(() => {
      showNextMessage();
      const interval = setInterval(showNextMessage, 5000);
      return () => clearInterval(interval);
    }, 3000);
    return () => clearTimeout(timer);
  }, []);

  const handleLogout = async () => {
    await signOut();
    navigate("/login");
  };

  if (!user) return <p>Cargando usuario...</p>;

  return (
    <div className="d-flex patient-dashboard">
      {/* Sidebar retráctil */}
      <Sidebar
        user={user}
        role="patient"
        show={showSidebar}
        onHide={() => setShowSidebar(false)}
      />

      <div className="flex-grow-1">
        <Header
          title="MedTech: Tu portal de salud"
          avatarUrl={user.avatar}
          buttons={[
            {
              label: "Cerrar sesión",
              onClick: handleLogout,
              className: "header-btn",
            },
          ]}
        />

        {/* Botón hamburguesa solo visible en móviles */}
        <div className="d-lg-none p-2">
          <Button
            variant="outline-primary"
            onClick={() => setShowSidebar(true)}
          >
            <List size={24} />
          </Button>
        </div>

        <Container className="py-4">
          <h2>
            ¡Bienvenid
            {user.gender === "Femenino"
              ? "a"
              : user.gender === "Masculino"
              ? "o"
              : user.name?.trim().slice(-1).toLowerCase() === "a"
              ? "a"
              : "o"}
            , {user.name}!
          </h2>
          <Row>
            <Col md={7}>
              <h4 className="mt-3 mb-3">Próximas Citas</h4>
              <Row>
                {appointments.map((appt, idx) => (
                  <Col md={12} key={idx} className="mb-3">
                    <CardAppointment
                      {...appt}
                      onSelect={() => setSelectedAppointment(appt)}
                    />
                  </Col>
                ))}
              </Row>
            </Col>
            <Col md={5}>
              {selectedAppointment ? (
                <AppointmentDetails
                  name={user.name}
                  age={user.age}
                  gender="Mujer"
                  date={selectedAppointment.dateTime.split(" - ")[0]}
                  time={selectedAppointment.dateTime.split(" - ")[1]}
                  motive={selectedAppointment.motive}
                  isTeleconsultation={selectedAppointment.isTeleconsultation}
                  assignedTo={selectedAppointment.doctor}
                  role="patient"
                />
              ) : (
                <p>Selecciona una cita para ver los detalles</p>
              )}
            </Col>
          </Row>

          <h2>Notificaciones</h2>
          {notification && (
            <NotificationCard
              title={notification.title}
              description={notification.description}
            />
          )}
        </Container>
      </div>
    </div>
  );
};

export default PatientDashboard;
