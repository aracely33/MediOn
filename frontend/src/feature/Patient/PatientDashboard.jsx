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
import { getMe, getAppointmentsByPatientId } from "./patientService";
import { getProfessionalById } from "../Doctor/doctorService";
import { calculateAge, formatDateTime } from "../auth/utils/birthday";
import "./PatientDashboard.css";

const PatientDashboard = () => {
  const { signOut } = usePatient();
  const navigate = useNavigate();
  const [showSidebar, setShowSidebar] = useState(false);
  const [user, setUser] = useState(null);
  const [selectedAppointment, setSelectedAppointment] = useState(null);
  const [notification, setNotification] = useState(null);
  const [appointments, setAppointments] = useState([]); // antes era el array fake

  // Cargar usuario real desde backend
  useEffect(() => {
    const fetchUser = async () => {
      try {
        const me = await getMe();

        setUser({
          id: me.id,
          name: me.name,
          lastName: me.lastName,
          email: me.email,
          age: calculateAge(me.birthDate),
          gender: me.gender || "No especificado",
          avatar: "https://cdn-icons-png.flaticon.com/512/149/149071.png", // avatar genérico
        });
      } catch (error) {
        console.error(error);
      }
    };

    fetchUser();
  }, []);

  useEffect(() => {
    const fetchAppointments = async () => {
      if (!user) return; // espera a que user ya esté cargado
      try {
        // Traer citas del backend
        const response = await getAppointmentsByPatientId(user.id);
        const appointmentsFromBackend = response.data;

        // Mapear citas y traer info del doctor
        const mappedAppointments = await Promise.all(
          appointmentsFromBackend.map(async (appt) => {
            const doctor = await getProfessionalById(appt.doctorId);

            // Para CardAppointment: no mostrar el día
            const { date, time } = formatDateTime(
              appt.appointmentDate,
              appt.appointmentTime,
              false // aquí false indica que no queremos el día
            );

            // Guardamos también la fecha completa con día por si se usa en AppointmentDetails
            const { date: dateWithDay } = formatDateTime(
              appt.appointmentDate,
              appt.appointmentTime,
              true // true para incluir el día
            );

            return {
              dateTime: `${date} - ${time}`, // para CardAppointment
              fullDateTime: `${dateWithDay} - ${time}`, // para AppointmentDetails
              doctor: `${doctor.name} ${doctor.lastName}`,
              specialty: doctor.specialty,
              license: doctor.medicalLicense || "No disponible",
              motive: appt.reason,
              isTeleconsultation: appt.type,
            };
          })
        );

        // Guardar en el estado para renderizar
        setAppointments(mappedAppointments);
      } catch (error) {
        console.error("Error cargando citas:", error);
      }
    };

    fetchAppointments();
  }, [user]);

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
              : user.name?.trim().slice(-1).toLowerCase() === "a" || "y"
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
                  age={
                    user.age !== null ? user.age + " años" : " No disponible"
                  }
                  gender={user.gender}
                  date={selectedAppointment.fullDateTime.split(" - ")[0]}
                  time={selectedAppointment.fullDateTime.split(" - ")[1]}
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
