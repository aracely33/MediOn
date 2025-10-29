import { useState } from "react";
import { Card, Row, Col, Button } from "react-bootstrap";

export const StepSpeciality = ({ onNext, currentStep, prev }) => {
  const [speciality, setspeciality] = useState(null);

  const specialities = [
    { id: "general", label: "Consulta General", icon: "stethoscope" },
    { id: "odontologia", label: "Odontología", icon: "dentistry" },
    { id: "oftalmologia", label: "Oftalmología", icon: "visibility" },
    { id: "dermatologia", label: "Dermatología", icon: "healing" },
  ];

  return (
    <Card className="p-4 shadow-sm animate-bounce-in">
      <h4 className="mb-3 text-primary fw-bold">Paso 2 de 5: Especialidad</h4>
      <Row>
        {specialities.map((esp) => (
          <Col md={6} lg={3} key={esp.id} className="mb-3">
            <Card
              className={`text-center p-3 ${
                speciality === esp.id
                  ? "border-primary border-3 shadow-sm"
                  : "border-light"
              }`}
              onClick={() => setspeciality(esp.id)}
              style={{ cursor: "pointer" }}
            >
              <span className="material-symbols-outlined fs-1 text-info">
                {esp.icon}
              </span>
              <Card.Text className="fw-semibold mt-2">{esp.label}</Card.Text>
            </Card>
          </Col>
        ))}
      </Row>

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

        <div className="text-end mt-3">
          <Button variant="success" disabled={!speciality} onClick={onNext}>
            Continuar
          </Button>
        </div>
      </div>
    </Card>
  );
};
