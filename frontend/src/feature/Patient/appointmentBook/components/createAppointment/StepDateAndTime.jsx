import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { Button, Row, Col, Card } from "react-bootstrap";

export const StepDateAndTime = ({ onNext, currentStep, prev, formik }) => {
  const times = [
    "08:00 AM",
    "08:30 AM",
    "09:00 AM",
    "09:30 AM",
    "10:00 AM",
    "10:30 AM",
    "11:00 AM",
    "11:30 AM",
    "12:00 PM",
    "12:30 PM",
    "01:00 PM",
    "01:30 PM",
    "02:00 PM",
    "02:30 PM",
    "03:00 PM",
    "03:30 PM",
    "04:00 PM",
    "04:30 PM",
    "05:00 PM",
    "05:30 PM",
    "06:00 PM",
    "06:30 PM",
    "07:00 PM",
  ];

  return (
    <Card className="p-4 shadow-sm animate-bounce-in">
      <h4 className="mb-3 text-primary fw-bold">
        Paso 3 de 5: Selecciona la fecha y hora disponibles
      </h4>
      <Row>
        <Col md={6}>
          <DatePicker
            selected={formik.values.appointmentDate}
            onChange={(appointmentDate) =>
              formik.setFieldValue("appointmentDate", appointmentDate)
            }
            inline
          />
        </Col>
        <Col md={6}>
          <h6 className="fw-bold text-dark mb-3">
            Horarios Disponibles (
            {formik.values.appointmentDate
              ? new Date(formik.values.appointmentDate).toLocaleDateString(
                  "es-ES"
                )
              : "Selecciona una fecha"}
            )
          </h6>
          <div className="d-flex flex-wrap gap-2">
            {times.map((t) => (
              <Button
                key={t}
                variant={
                  t === formik.values.appointmentTime
                    ? "primary"
                    : "outline-secondary"
                }
                onClick={() => formik.setFieldValue("appointmentTime", t)}
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
            disabled={
              !formik.values.appointmentDate || !formik.values.appointmentTime
            }
            onClick={onNext}
          >
            Continuar
          </Button>
        </div>
      </div>
    </Card>
  );
};
