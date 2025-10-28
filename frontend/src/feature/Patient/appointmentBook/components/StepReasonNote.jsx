import { Button, Card, Form } from "react-bootstrap";

export const StepReasonNote = ({ currentStep, prev, onNext, formik }) => {
  return (
    <Card className="p-4 shadow-sm animate-bounce-in">
      <h4 className="mb-3 text-primary fw-bold">
        Paso 4 de 5: Introduce el motivo de la cita y/o una nota adicional
      </h4>
      <Form.Group controlId="notes" className="mb-3">
        <Form.Label>Motivo:</Form.Label>
        <Form.Control
          as="textarea"
          rows={3}
          value={formik.values.reason}
          onChange={(e) => formik.setFieldValue("reason", e.target.value)}
        />

        <br />

        <Form.Label>Nota (opcional):</Form.Label>
        <Form.Control
          as="textarea"
          rows={3}
          value={formik.values.notes}
          onChange={(e) => formik.setFieldValue("notes", e.target.value)}
        />
      </Form.Group>

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
            disabled={!formik.values.reason}
            onClick={onNext}
          >
            Continuar
          </Button>
        </div>
      </div>
    </Card>
  );
};
