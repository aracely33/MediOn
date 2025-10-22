import { useState } from "react";
import { dateFormatter } from "../../../../utils/formatters";
import { Card, Button, Alert } from "react-bootstrap";
import { Link } from "react-router-dom";

export const StepConfirm = ({
  typeAppointment,
  doctor,
  date,
  time,
  reason,
  note,
  linkVideo,
  currentStep,
  prev,
}) => {
  const [confirmed, setConfirmed] = useState(false);

  const confirmAppointment = () => {
    setConfirmed(true);
  };

  return (
    <Card className="p-4 shadow-sm animate-bounce-in">
      <h4 className="mb-3 text-primary fw-bold">Confirmación de Cita</h4>

      {confirmed ? (
        <Alert variant="success" className="mt-3 text-center">
          🎉 ¡Tu cita ha sido agendada con éxito!
          <br />
          Recibirás un correo con los detalles.
          <br />
          <Link to="/patient-home">
            <Button variant="primary">Ir al inicio</Button>
          </Link>
        </Alert>
      ) : (
        <>
          <ul className="text-secondary">
            <li>
              <strong>Tipo de cita:</strong>{" "}
              {typeAppointment === "presencial" ? "Presencial" : "Virtual"}
            </li>
            <li>
              <strong>Médico:</strong> {doctor}
            </li>
            <li>
              <strong>Fecha:</strong> {dateFormatter.format(new Date(date))}
            </li>
            <li>
              <strong>Hora:</strong> {time}
            </li>
            {typeAppointment === "virtual" && linkVideo && (
              <li>
                <strong>Enlace videollamada:</strong>{" "}
                <a href={linkVideo} target="_blank" rel="noreferrer">
                  {linkVideo}
                </a>
              </li>
            )}
            <li>
              <strong>Motivo:</strong> {reason}
            </li>
            <li>
              <strong>Nota:</strong> {note ? note : "Sin nota"}
            </li>
          </ul>

          <div className="d-flex justify-content-between mt-4">
            <div className="text-start mt-3">
              <Button
                variant="outline-secondary"
                disabled={currentStep === 1}
                onClick={prev}
              >
                Paso Anterior
              </Button>
            </div>

            <div className="text-end">
              <Button variant="success" onClick={confirmAppointment}>
                Confirmar Cita
              </Button>
            </div>
          </div>
        </>
      )}
    </Card>
  );
};
