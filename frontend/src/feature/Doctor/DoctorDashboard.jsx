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
import { getDoctorAvailability } from "./doctorService";
import "./DoctorDashboard.css";
import { useDoctor } from "../../context/DoctorContext";

const DoctorDashboard = () => {
  const { signOut } = useDoctor();
  const navigate = useNavigate();
  const [doctor, setDoctor] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedAppointment, setSelectedAppointment] = useState(null);
  const [showCalendar, setShowCalendar] = useState(false);
  const [showSidebar, setShowSidebar] = useState(false);
  const [occupiedSlots, setOccupiedSlots] = useState([]);

  // Traer info del doctor
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

  // Traer horarios ocupados
  useEffect(() => {
    const fetchAvailability = async () => {
      try {
        const slots = await getDoctorAvailability(doctor.id);
        setOccupiedSlots(slots);
      } catch (error) {
        console.error("Error al obtener disponibilidad:", error);
      }
    };
    if (doctor?.id) fetchAvailability();
  }, [doctor]);

  // Mapear citas
  const mapSlotToAppointment = (slot) => {
    const slotDate = new Date(slot);
    return {
      time: slotDate.toLocaleTimeString("es-MX", {
        hour: "2-digit",
        minute: "2-digit",
      }),
      patient: "Ocupado",
      age: "-",
      gender: "-",
      specialty: doctor?.specialty || "",
      status: "Confirmada",
      dateTime: slot,
      motive: "Cita agendada",
      isTeleconsultation: false,
      doctor: doctor?.name,
    };
  };

  // Agenda diaria: solo citas de hoy
  const today = new Date();
  const todaySchedule = occupiedSlots
    .map(mapSlotToAppointment)
    .filter((item) => {
      const slotDate = new Date(item.dateTime);
      return (
        slotDate.getFullYear() === today.getFullYear() &&
        slotDate.getMonth() === today.getMonth() &&
        slotDate.getDate() === today.getDate()
      );
    });

  // Calendario: todas las citas
  const calendarAppointments = occupiedSlots.map((slot) => ({
    ...mapSlotToAppointment(slot),
  }));

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
                    {...selectedAppointment}
                    date={new Date(selectedAppointment.dateTime).toLocaleString(
                      "es-MX",
                      { dateStyle: "long", timeStyle: "short" }
                    )}
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
