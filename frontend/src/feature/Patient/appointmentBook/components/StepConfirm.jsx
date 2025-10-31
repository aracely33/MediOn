import { useState } from "react";
import { dateFormatter } from "../../../../utils/formatters";
import { Card, Button, Alert } from "react-bootstrap";
import { Link } from "react-router-dom";

export const StepConfirm = ({ formik, currentStep, prev, doctor }) => {
  const [confirmed, setConfirmed] = useState(false);
  const [apiError, setApiError] = useState("");

  const confirmAppointment = async () => {
    setApiError("");
    const errors = await formik.validateForm();

    if (Object.keys(errors).length > 0) {
      console.log("Errores de validación:", errors);
      formik.setTouched(
        Object.keys(formik.values).reduce(
          (acc, key) => ({ ...acc, [key]: true }),
          {}
        )
      );
      return;
    }

    if (window.confirm("¿Estás seguro de confirmar la cita?")) {
      try {
        await formik.handleSubmit();
        console.log("Cita confirmada");
        setConfirmed(true);
      } catch (error) {
        console.error("Error al confirmar la cita:", error);
        const detail =
          error?.response?.data?.details?.[0] ||
          error?.response?.data?.message ||
          "Error al confirmar la cita";
        setApiError(detail);
      }
    }
  };

  const formatType = (type) => {
    return type === "PRESENCIAL" ? "Presencial" : "Virtual";
  };

  return (
    <Card className="p-4 shadow-sm animate-bounce-in">
      <h4 className="mb-3 text-primary fw-bold">
        Paso 5 de 5: Confirmación de Cita
      </h4>

      {confirmed ? (
        <Alert variant="success" className="mt-3 text-center">
          🎉 ¡Tu cita ha sido agendada con éxito!
          <br />
          También se agendó en tu Google Calendar. Recibirás un correo con los
          detalles.
          <br />
          <Link to="/patient-home">
            <Button variant="primary">Ir al inicio</Button>
          </Link>
        </Alert>
      ) : (
        <>
          <p>Se muestra un resumen de tu cita:</p>
          <ul className="text-secondary">
            <li>
              <strong>Tipo de cita:</strong> {formatType(formik.values.type)}
            </li>
            <li>
              <strong>Médico:</strong> {doctor?.name} {doctor?.lastName}
            </li>
            <li>
              <strong>Fecha:</strong>{" "}
              {dateFormatter.format(new Date(formik.values.appointmentDate))}
              {console.log(
                "Fecha en StepConfirm:",
                dateFormatter.format(new Date(formik.values.appointmentDate))
              )}
            </li>
            <li>
              <strong>Hora:</strong> {formik.values.appointmentTime}
            </li>
            <li>
              <strong>Motivo:</strong> {formik.values.reason}
            </li>
            <li>
              <strong>Nota:</strong>{" "}
              {formik.values.notes ? formik.values.notes : "Sin nota"}
            </li>
          </ul>
          {apiError && (
            <div className="mt-3">
              <Alert variant="danger">⚠️ {apiError}</Alert>
            </div>
          )}

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
