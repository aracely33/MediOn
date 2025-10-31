import { Card, Row, Col, Button } from "react-bootstrap";

export const StepTypeAppointment = ({ formik, onNext, currentStep, prev }) => {
  const handleSelection = (tipo) => {
    formik.setFieldValue("type", tipo);
  };

  return (
    <Card className="p-4 shadow-sm animate-bounce-in">
      <h4 className="mb-3 text-primary fw-bold">Paso 1 de 5: Tipo de Cita</h4>
      <Row>
        <Col md={6}>
          <Card
            className={`text-center p-4 ${
              formik.values.type === "PRESENCIAL"
                ? "border-primary border-3"
                : ""
            }`}
            onClick={() => handleSelection("PRESENCIAL")}
            style={{ cursor: "pointer" }}
          >
            <span className="material-symbols-outlined fs-1 text-info">
              person_pin_circle
            </span>
            <Card.Text className="fw-semibold mt-2">Presencial</Card.Text>
          </Card>
        </Col>

        <Col md={6}>
          <Card
            className={`text-center p-4 ${
              formik.values.type === "VIRTUAL" ? "border-primary border-3" : ""
            }`}
            onClick={() => handleSelection("VIRTUAL")}
            style={{ cursor: "pointer" }}
          >
            <span className="material-symbols-outlined fs-1 text-info">
              videocam
            </span>
            <Card.Text className="fw-semibold mt-2">Virtual</Card.Text>
          </Card>
        </Col>
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
          <Button
            variant="success"
            disabled={!formik.values.type}
            onClick={onNext}
          >
            Continuar
          </Button>
        </div>
      </div>
    </Card>
  );
};
