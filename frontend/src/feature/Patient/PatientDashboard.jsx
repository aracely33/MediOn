import { useState, useEffect } from "react";
import { Container, Row, Col, Button, Modal } from "react-bootstrap";
import { List } from "react-bootstrap-icons"; // ícono hamburguesa
import Header from "../../components/header/Header";
import Sidebar from "../../components/sidebar/Sidebar";
import CardAppointment from "../../components/cardAppointment/CardAppointment";
import AppointmentDetails from "../../components/appointmentDetail/AppointmentDetails";
import NotificationCard from "../../components/notificationCard/NotificationCard";
import { useNavigate } from "react-router-dom";
import { usePatient } from "../../context/PatientContext";
import {
  getMe,
  getAppointmentsByPatientId,
  cancelAppointment,
} from "./patientService";
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
  const [appointments, setAppointments] = useState([]);
  const [appointmentToCancel, setAppointmentToCancel] = useState(null);

  // Cargar usuario
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
          avatar: "https://cdn-icons-png.flaticon.com/512/149/149071.png",
        });
      } catch (error) {
        console.error(error);
      }
    };
    fetchUser();
  }, []);

  // Cargar citas
  useEffect(() => {
    const fetchAppointments = async () => {
      if (!user) return;
      try {
        const response = await getAppointmentsByPatientId(user.id);
        const appointmentsFromBackend = response.data;

        const mappedAppointments = await Promise.all(
          appointmentsFromBackend.map(async (appt) => {
            try {
              const doctor = await getProfessionalById(appt.doctorId);

              const { date, time } = formatDateTime(
                appt.appointmentDate,
                appt.appointmentTime,
                false
              );
              const { date: dateWithDay } = formatDateTime(
                appt.appointmentDate,
                appt.appointmentTime,
                true
              );

              return {
                dateTime: `${date} - ${time}`,
                fullDateTime: `${dateWithDay} - ${time}`,
                doctor: doctor
                  ? `${doctor.name} ${doctor.lastName}`
                  : "Médico no disponible",
                specialty: doctor?.specialty || "Sin especialidad",
                license: doctor?.medicalLicense || "No disponible",
                motive: appt.reason,
                isTeleconsultation: appt.type,
                status: appt.status,
                id: appt.id,
              };
            } catch (error) {
              console.error(`Error obteniendo médico ${appt.doctorId}:`, error);
              return null;
            }
          })
        );

        setAppointments(
          mappedAppointments.filter(
            (appt) => appt && appt.status !== "CANCELADA"
          )
        );
      } catch (error) {
        console.error("Error cargando citas:", error);
      }
    };
    fetchAppointments();
  }, [user]);

  // Mensajes de notificación automáticos
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

  const handlePrepareCancel = (appt) => setAppointmentToCancel(appt);

  const handleConfirmCancel = async () => {
    if (!appointmentToCancel) return;
    try {
      await cancelAppointment(appointmentToCancel.id);
      setAppointments((prev) =>
        prev.filter((appt) => appt.id !== appointmentToCancel.id)
      );
      if (selectedAppointment?.id === appointmentToCancel.id)
        setSelectedAppointment(null);
      setNotification({
        title: "Cita cancelada",
        description: "La cita se canceló exitosamente.",
      });
    } catch (error) {
      console.error("Error al cancelar la cita:", error);
      alert("No se pudo cancelar la cita. Inténtalo de nuevo más tarde.");
    } finally {
      setAppointmentToCancel(null);
    }
  };

  const handleLogout = async () => {
    await signOut();
    navigate("/login");
  };

  if (!user) return <p>Cargando usuario...</p>;

  return (
    <div className="d-flex patient-dashboard">
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
                      onCancel={() => handlePrepareCancel(appt)}
                    />
                  </Col>
                ))}
              </Row>
            </Col>

            <Col md={5}>
              {selectedAppointment ? (
                <AppointmentDetails
                  name={user.name}
                  age={user.age !== null ? user.age + " años" : "No disponible"}
                  gender={user.gender}
                  date={selectedAppointment.fullDateTime.split(" - ")[0]}
                  time={selectedAppointment.fullDateTime.split(" - ")[1]}
                  motive={selectedAppointment.motive}
                  isTeleconsultation={selectedAppointment.isTeleconsultation}
                  assignedTo={selectedAppointment.doctor}
                  role="patient"
                  onClose={() => setSelectedAppointment(null)}
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

          {/* Modal de confirmación de cancelación */}
          <Modal
            show={!!appointmentToCancel}
            onHide={() => setAppointmentToCancel(null)}
            centered
          >
            <Modal.Header closeButton>
              <Modal.Title>Cancelar cita</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              ¿Estás segura/o de que deseas cancelar la cita con{" "}
              {appointmentToCancel?.doctor} el {appointmentToCancel?.dateTime}?
            </Modal.Body>
            <Modal.Footer>
              <Button
                variant="secondary"
                onClick={() => setAppointmentToCancel(null)}
              >
                No
              </Button>
              <Button variant="danger" onClick={handleConfirmCancel}>
                Sí, cancelar
              </Button>
            </Modal.Footer>
          </Modal>
        </Container>
      </div>
    </div>
  );
};

export default PatientDashboard;
