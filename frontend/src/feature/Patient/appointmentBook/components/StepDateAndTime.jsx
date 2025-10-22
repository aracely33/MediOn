import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { Button, Row, Col, Card } from "react-bootstrap";

export const StepDateAndTime = ({
  onNext,
  currentStep,
  prev,
  chooseDate,
  setChooseDate,
  chooseTime,
  setChooseTime,
}) => {
  const times = [
    "09:00 AM",
    "09:30 AM",
    "10:00 AM",
    "10:30 AM",
    "11:30 AM",
    "02:00 PM",
    "02:30 PM",
    "03:00 PM",
  ];

  return (
    <Card className="p-4 shadow-sm animate-bounce-in">
      <h4 className="mb-3 text-primary fw-bold">Paso 3 de 4: Fecha y Hora</h4>
      <Row>
        <Col md={6}>
          <DatePicker
            selected={chooseDate}
            onChange={(date) => setChooseDate(date)}
            inline
          />
        </Col>
        <Col md={6}>
          <h6 className="fw-bold text-dark mb-3">
            Horarios Disponibles ({chooseDate.toLocaleDateString("es-ES")})
          </h6>
          <div className="d-flex flex-wrap gap-2">
            {times.map((t) => (
              <Button
                key={t}
                variant={t === chooseTime ? "primary" : "outline-secondary"}
                onClick={() => setChooseTime(t)}
              >
                {t}
              </Button>
            ))}
          </div>
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
            disabled={!chooseDate || !chooseTime}
            onClick={onNext}
          >
            Continuar
          </Button>
        </div>
      </div>
    </Card>
  );
};
