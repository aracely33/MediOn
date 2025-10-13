import React from "react";
import { Button, Card } from "react-bootstrap";

const AppointmentCard = ({
  date,
  time,
  doctor,
  clinic,
  onCancel,
  onReschedule,
}) => {
  return (
    <Card className="mb-3">
      <Card.Body>
        <Card.Title>{`${date} - ${time}`}</Card.Title>
        <Card.Text>{`${doctor}, ${clinic}`}</Card.Text>
        <div className="d-flex justify-content-end gap-2 mt-2">
          <Button variant="secondary" size="sm" onClick={onCancel}>
            Cancelar
          </Button>
          <Button variant="primary" size="sm" onClick={onReschedule}>
            Reprogramar
          </Button>
        </div>
      </Card.Body>
    </Card>
  );
};

export default AppointmentCard;
