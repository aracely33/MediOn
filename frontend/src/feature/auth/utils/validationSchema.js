import * as Yup from "yup";

export const signupValidation = Yup.object({
  firstName: Yup.string()
    .min(2, "debe de tener al menos 2 caracteres")
    .required("El nombre es obligatorio"),
  lastName: Yup.string()
    .min(2, "Debe tener al menos 2 caracteres")
    .required("Los apellidos son obligatorios"),
  email: Yup.string()
    .email("Debe ser un correo válido")
    .required("El correo es obligatorio")
    .matches(
      /^(?=.*[A-Za-z])[A-Za-z0-9@._-]+$/,
      "El correo debe contener al menos una letra y puede incluir números"
    ),
  password: Yup.string()
    .min(8, "Debe tener al menos 8 caracteres")
    .matches(/[A-Z]/, "Debe contener al menos una mayúscula")
    .matches(/[0-9]/, "Debe contener al menos un número")
    .matches(/[!@#$%^&*]/, "Debe contener al menos un carácter especial")
    .required("La contraseña es obligatoria"),
  confirmPassword: Yup.string()
    .oneOf([Yup.ref("password"), null], "Las contraseñas no coinciden")
    .required("Debes confirmar la contraseña"),
  terms: Yup.boolean().oneOf(
    [true],
    "Debes aceptar los términos y condiciones"
  ),
});
