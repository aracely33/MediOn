import { useState } from "react";
import Header from "../../components/header/Header";
import Sidebar from "../../components/sidebar/Sidebar";
import CardAppointment from "../../components/cardAppointment/CardAppointment";
import AppointmentDetails from "../../components/appointmentDetail/AppointmentDetails";
import { Container, Row, Col } from "react-bootstrap";
import "./PatientDashboard.css";

const PatientDashboard = () => {
  const user = {
    name: "Sofía Sánchez",
    age: 26,
    id: 789123,
    avatar:
      "https://lh3.googleusercontent.com/aida-public/AB6AXuDUYbpIPUHkmvCQgSP8TXDFj3_qFgcw7EkbNkIpKcguT2aaEougplE9ZVRo7AvkOudTs__NOpqWc3xr_P7qOuM0CN5NaKFgDGdcA_45P6EpScJ0m5mdv8ejKg3H_j7Icadd1zgeMPXFnUHKuBDKiOyAvsz-qE76pBkq2ehA5owS-Y00XxErSgJ7c4aj8_eEnKbftusSpRIm7hgaiEA6UHD3KdaLRzJQoY3Xb2L2wE0ZhlWCbRe_tTrNNhDw2sFxE_sFob6XuwGAt3M",
  };

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

  const [selectedAppointment, setSelectedAppointment] = useState(null);

  return (
    <div className="d-flex patient-dashboard">
      <Sidebar user={user} />
      <div className="flex-grow-1">
        <Header
          title="Tu portal de salud"
          avatarUrl={user.avatar}
          onLogout={() => alert("Logout")}
        />

        <Container className="py-4">
          <h2>¡Bienvenida, {user.name}!</h2>

          <Row>
            {/* Columna izquierda: las citas */}
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

            {/* Columna derecha: detalles de la cita seleccionada */}
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
        </Container>
      </div>
    </div>
  );
};

export default PatientDashboard;
