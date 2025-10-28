import { Row, Col } from "react-bootstrap";

export const ProgressSteps = ({ currentStep, typeAppointment }) => {
  let steps = ["Tipo", "Médico", "Fecha y Hora", "Motivo y Nota", "Confirmar"];

  // if (typeAppointment === "virtual") {
  //   steps = [
  //     "Tipo",
  //     "Especialidad",
  //     "Médico",
  //     "Fecha y Hora",
  //     "Motivo y Nota",
  //     "Videollamada",
  //     "Confirmar",
  //   ];
  // }

  return (
    <Row className="justify-content-center mb-4">
      {steps.map((label, index) => {
        const step = index + 1;
        const isActive = step === currentStep;
        const isCompleted = step < currentStep;

        return (
          <Col
            key={step}
            xs={2}
            className={`text-center progress-leaf ${
              isActive ? "active" : isCompleted ? "completed" : ""
            }`}
          >
            <span
              className={`material-symbols-outlined fs-1 ${
                isActive || isCompleted
                  ? "text-primary"
                  : "text-secondary opacity-50"
              }`}
            >
              medical_services
            </span>
            <div className="fw-semibold small text-dark">{label}</div>
          </Col>
        );
      })}
    </Row>
  );
};
