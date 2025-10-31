import { Button, Card } from "react-bootstrap";
import "./CardAppointment.css";

const CardAppointment = ({
  dateTime,
  doctor,
  specialty,
  license,
  onSelect,
  onCancel,
}) => (
  <Card
    className="card-appointment"
    onClick={onSelect}
    style={{ cursor: "pointer" }}
  >
    <Card.Body>
      <Card.Title className="appointment-title">{dateTime}</Card.Title>
      <Card.Text className="appointment-info">
        {doctor}, {specialty} <br />
        {"Licencia médica: " + license}
      </Card.Text>
      <div className="d-flex justify-content-end gap-2">
        <Button
          variant="secondary"
          size="sm"
          className="btn-cancel"
          onClick={onCancel}
        >
          Cancelar
        </Button>
        <Button
          variant="primary"
          size="sm"
          className="btn-reschedule"
          onClick={(e) => e.stopPropagation()}
        >
          Reprogramar
        </Button>
      </div>
    </Card.Body>
  </Card>
);

export default CardAppointment;
