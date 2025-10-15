import { Button, Card } from "react-bootstrap";

const CardAppointment = ({ dateTime, doctor, specialty, clinic, onSelect }) => (
  <Card
    className="mb-3 shadow-sm"
    onClick={onSelect}
    style={{ cursor: "pointer" }}
  >
    <Card.Body>
      <Card.Title>{dateTime}</Card.Title>
      <Card.Text>
        {doctor}, {specialty} <br />
        {clinic}
      </Card.Text>
      <div className="d-flex justify-content-end gap-2">
        <Button variant="secondary" size="sm">
          Cancelar
        </Button>
        <Button variant="primary" size="sm">
          Reprogramar
        </Button>
      </div>
    </Card.Body>
  </Card>
);

export default CardAppointment;
