// src/features/auth/utils/validationSchema.js
import * as Yup from "yup";

export const signupValidation = Yup.object({
  name: Yup.string()
    .min(6, "Debe tener al menos 6 caracteres")
    .required("El nombre completo es obligatorio"),
  email: Yup.string()
    .email("Debe ser un correo válido")
    .required("El correo es obligatorio"),
  password: Yup.string()
    .min(8, "Debe tener al menos 8 caracteres")
    .matches(/[A-Z]/, "Debe contener al menos una mayúscula")
    .matches(/[0-9]/, "Debe contener al menos un número")
    .matches(/[!@#$%^&*]/, "Debe contener al menos un carácter especial")
    .required("La contraseña es obligatoria"),
  terms: Yup.boolean().oneOf(
    [true],
    "Debes aceptar los términos y condiciones"
  ),
});
