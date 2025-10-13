import { Card, Badge } from "react-bootstrap";

const DoctorScheduleCard = ({ time, patient, specialty, status, onSelect }) => {
  const statusColor = status === "Confirmada" ? "success" : "warning";

  return (
    <Card
      className="mb-3 shadow-sm"
      onClick={onSelect}
      style={{ cursor: "pointer" }}
    >
      <Card.Body>
        <Card.Title>{time}</Card.Title>
        <Card.Text>
          <strong>Paciente:</strong> {patient} <br />
          <strong>Especialidad:</strong> {specialty}
        </Card.Text>
        <Badge bg={statusColor}>{status}</Badge>
      </Card.Body>
    </Card>
  );
};

export default DoctorScheduleCard;
