import { useState, useEffect } from "react";
import { Container, Row, Col, Button } from "react-bootstrap";
import { List } from "react-bootstrap-icons";
import Header from "../../components/header/Header";
import Sidebar from "../../components/sidebar/Sidebar";
import DoctorScheduleCard from "../../components/doctorScheduleCard/DoctorScheduleCard";
import NotificationCard from "../../components/notificationCard/NotificationCard";
import CalendarView from "../../components/calendarView/calendarView";
import AppointmentDetails from "../../components/appointmentDetail/AppointmentDetails";
import { useNavigate } from "react-router-dom";
import { getMe } from "../auth/services/authService";
import { getDoctorAppointments } from "./doctorService";
import { getPatientById } from "../Patient/patientService";
import "./DoctorDashboard.css";
import { useDoctor } from "../../context/DoctorContext";

const DoctorDashboard = () => {
  const { signOut } = useDoctor();
  const navigate = useNavigate();
  const [doctor, setDoctor] = useState(null);
  const [loading, setLoading] = useState(true);
  const [appointments, setAppointments] = useState([]);
  const [selectedAppointment, setSelectedAppointment] = useState(null);
  const [showCalendar, setShowCalendar] = useState(false);
  const [showSidebar, setShowSidebar] = useState(false);

  // 🔹 Traer información del doctor
  useEffect(() => {
    const fetchDoctor = async () => {
      try {
        const me = await getMe();
        setDoctor({
          id: me.id,
          name: me.name,
          lastName: me.lastName,
          email: me.email,
          specialty: me.specialty,
          medicalLicense: me.medicalLicense,
          biography: me.biography,
          consultationFee: me.consultationFee,
          avatar:
            me.avatar ||
            "https://cdn-icons-png.flaticon.com/512/149/149071.png",
        });
      } catch (error) {
        console.error("Error al obtener info del doctor:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchDoctor();
  }, []);

  // 🔹 Traer citas del doctor y asociar datos del paciente
  useEffect(() => {
    const fetchAppointments = async () => {
      if (!doctor?.id) return;

      try {
        const data = await getDoctorAppointments(doctor.id);

        const formatted = await Promise.all(
          data.map(async (appt) => {
            let patientName = `Paciente ${appt.patientId}`;
            let patientData = null;

            try {
              const patient = await getPatientById(appt.patientId);
              patientName = `${patient.name} ${patient.lastName}`;
              patientData = patient;
            } catch (err) {
              console.warn("No se pudo traer paciente:", appt.patientId);
            }

            return {
              id: appt.id,
              time: new Date(
                appt.appointmentDate + "T" + appt.appointmentTime
              ).toLocaleTimeString("es-MX", {
                hour: "2-digit",
                minute: "2-digit",
              }),
              patient: patientName,
              age: patientData?.age || "-",
              gender: patientData?.gender || "-",
              specialty: doctor?.specialty || "",
              status: appt.status === "PENDIENTE" ? "Pendiente" : "Confirmada",
              dateTime: appt.appointmentEndDateTime,
              motive: appt.reason,
              notes: appt.notes,
              isTeleconsultation: appt.type,
              doctor: doctor?.name,
            };
          })
        );

        setAppointments(formatted);
      } catch (error) {
        console.error("Error al obtener citas:", error);
      }
    };

    fetchAppointments();
  }, [doctor]);

  // 🔹 Filtrar citas de hoy
  const today = new Date();
  const todaySchedule = appointments.filter((appt) => {
    const slotDate = new Date(appt.dateTime);
    return (
      slotDate.getFullYear() === today.getFullYear() &&
      slotDate.getMonth() === today.getMonth() &&
      slotDate.getDate() === today.getDate()
    );
  });

  const calendarAppointments = appointments;

  const notifications = [
    {
      title: "Recordatorio de perfil",
      description:
        "Actualiza tu información personal y fotografía para mantener tu perfil completo en servicio técnico.",
    },
    {
      title: "Mantenimiento de la plataforma",
      description:
        "La plataforma estará en mantenimiento este viernes de 22:00 a 23:00.",
    },
  ];

  const handleLogout = async () => {
    await signOut();
    navigate("/login");
  };

  if (loading) return <p>Cargando información del médico...</p>;
  if (!doctor) return <p>No se pudo cargar la información del médico.</p>;

  return (
    <div className="d-flex doctor-dashboard">
      {/* Sidebar */}
      <Sidebar
        user={{
          name: doctor.name,
          specialty: doctor.specialty,
          id: doctor.id,
          avatar: doctor.avatar,
        }}
        role="doctor"
        show={showSidebar}
        onHide={() => setShowSidebar(false)}
      />

      <div className="flex-grow-1">
        <Header
          title="MedTech: Portal Médico"
          avatarUrl={doctor.avatar}
          buttons={[
            {
              label: "Cerrar sesión",
              onClick: handleLogout,
              className: "header-btn",
            },
          ]}
        />

        {/* Botón hamburguesa móvil */}
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
                {todaySchedule.length > 0 ? (
                  <Row>
                    {todaySchedule.map((item, idx) => (
                      <Col md={12} key={idx} className="mb-3">
                        <DoctorScheduleCard
                          {...item}
                          onSelect={() => setSelectedAppointment(item)}
                        />
                      </Col>
                    ))}
                  </Row>
                ) : (
                  <p className="mt-3">Sin citas para hoy</p>
                )}

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
                {selectedAppointment && (
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
                    assignedTo={selectedAppointment.doctor}
                    isTeleconsultation={selectedAppointment.isTeleconsultation}
                    role="doctor"
                    onClose={() => setSelectedAppointment(null)}
                  />
                )}
              </Col>
            </Row>
          ) : (
            <CalendarView
              appointments={calendarAppointments}
              onSelectEvent={(appt) => setSelectedAppointment(appt)}
            />
          )}
        </Container>
      </div>
    </div>
  );
};

export default DoctorDashboard;
