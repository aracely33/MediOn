import * as Yup from "yup";

export const appointmentValidation = Yup.object({
  typeAppointment: Yup.string().required("El tipo de cita es obligatorio"),
  speciality: Yup.string().required("La especialidad es obligatoria"),
  doctor: Yup.string().required("El doctor es obligatorio"),
  date: Yup.date()
    .min(new Date(), "La fecha no puede ser en el pasado")
    .required("La fecha es obligatoria"),
  time: Yup.string().required("La hora es obligatoria"),
  link: Yup.string().when("typeAppointment", {
    is: "virtual",
    then: Yup.string().required("El enlace es obligatorio"),
    otherwise: Yup.string().notRequired(),
  }),
  notes: Yup.string().max(
    500,
    "Las notas no pueden exceder los 500 caracteres"
  ),
});
