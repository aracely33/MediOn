import { Button, Card } from "react-bootstrap";

const CardAppointment = ({ dateTime, doctor, specialty, clinic }) => (
  <Card className="mb-3 shadow-sm">
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
