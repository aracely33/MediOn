import { useState } from "react";
import {
  convertTo24Hour,
  dateFormatter,
} from "../../../../../utils/formatters";
import { Card, Button, Alert } from "react-bootstrap";
import { useAppointment } from "../../../../../context/AppointmentContext";

export const StepConfirm = ({ formik, currentStep, prev, doctor }) => {
  const [confirmed, setConfirmed] = useState(false);
  const [apiError, setApiError] = useState("");
  const { addAppointment } = useAppointment();

  const confirmAppointment = async () => {
    setApiError("");

    const errors = await formik.validateForm();
    if (Object.keys(errors).length > 0) {
      formik.setTouched(
        Object.keys(formik.values).reduce(
          (acc, key) => ({ ...acc, [key]: true }),
          {}
        )
      );
      return;
    }

    const userConfirmed = window.confirm("¿Estás seguro de confirmar la cita?");
    if (!userConfirmed) return;

    try {
      await addAppointment({
        type: formik.values.type,
        doctorId: formik.values.doctorId,
        patientId: formik.values.patientId,
        appointmentDate: formik.values.appointmentDate
          .toISOString()
          .split("T")[0],
        appointmentTime: convertTo24Hour(formik.values.appointmentTime),
        reason: formik.values.reason,
        notes: formik.values.notes || "",
      });

      setConfirmed(true);
    } catch (error) {
      console.error("Error al crear la cita:", error);

      const apiMessage =
        error?.response?.data?.details?.[0] ||
        error?.response?.data?.message ||
        "Error al programar la cita";

      setApiError(apiMessage);
      setConfirmed(false);
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
          🎉 ¡Tu cita ha sido agendada con éxito! También se agendó en tu Google
          Calendar.
          <br />
          Por favor dirígete a tu Panel de Control para ver las citas agendadas.
          <br />
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
