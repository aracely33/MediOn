import * as Yup from "yup";

export const appointmentValidation = Yup.object({
  type: Yup.string()
    .oneOf(["PRESENCIAL", "VIRTUAL"], "Tipo de cita inválido")
    .required("El tipo de cita es obligatorio"),
  doctorId: Yup.number()
    .typeError("El ID del doctor es requerido")
    .required("El ID del doctor es requerido"),
  patientId: Yup.number()
    .typeError("El ID del paciente es requerido")
    .required("El ID del paciente es requerido"),
  appointmentDate: Yup.date()
    .nullable()
    .test("fecha-futura", "La fecha no puede ser en el pasado", (value) => {
      if (!value) return false;
      const day = new Date(value);
      day.setHours(0, 0, 0, 0);
      const today = new Date();
      today.setHours(0, 0, 0, 0);
      return day >= today;
    })
    .required("La fecha es obligatoria"),
  appointmentTime: Yup.string().required("La hora es obligatoria"),
  reason: Yup.string()
    .trim()
    .max(500, "El motivo no puede exceder los 500 caracteres")
    .required("El motivo es obligatorio"),
  notes: Yup.string().max(
    500,
    "Las notas no pueden exceder los 500 caracteres"
  ),
});
