// src/features/auth/components/PasswordRequirements.jsx
import { CheckCircleFill, XCircleFill } from "react-bootstrap-icons";
import { useState, useEffect } from "react";
import * as Yup from "yup";

const passwordSchema = Yup.string()
  .min(8, "Debe tener al menos 8 caracteres")
  .matches(/[A-Z]/, "Debe contener al menos una mayúscula")
  .matches(/[0-9]/, "Debe contener al menos un número")
  .matches(/[!@#$%^&*]/, "Debe contener al menos un carácter especial")
  .required("La contraseña es obligatoria");

const PasswordRequirements = ({ password, touched }) => {
  /*const rules = [
    { label: "Mínimo 8 caracteres", test: (v) => v.length >= 8 },
    { label: "Al menos una letra mayúscula", test: (v) => /[A-Z]/.test(v) },
    { label: "Al menos un número", test: (v) => /[0-9]/.test(v) },
    {
      label: "Al menos un carácter especial (!@#$%^&*)",
      test: (v) => /[!@#$%^&*]/.test(v),
    },
  ];

  //const [touched, setTouched] = useState(false);

  Detect when the user starts typing
  useEffect(() => {
    if (password.length > 0) setTouched(true);
    else setTouched(false);
  }, [password]);

  const allPassed = rules.every((rule) => rule.test(password));

  // Hide everything if user hasn't typed or all rules pass
  if (!touched || allPassed) return null;*/

  const [errors, setErrors] = useState([]);

  useEffect(() => {
    const validatePassword = async () => {
      if (!password) {
        // 👇 Si está vacío, muestra solo el mensaje de requerido
        setErrors(["La contraseña es obligatoria"]);
        return;
      }

      try {
        await passwordSchema.validate(password);
        setErrors([]); // ✅ Si todo pasa, no hay errores
      } catch (err) {
        if (err.errors) setErrors(err.errors); // ✅ Captura las reglas que fallan
      }
    };

    if (password !== "" || touched) validatePassword();
    else setErrors([]);
  }, [password, touched]);

  // No mostrar nada si el usuario no ha tocado el campo ni escrito
  if (!touched && password === "") return null;

  return (
    <ul className="list-unstyled mt-2">
      {errors.length > 0 ? (
        errors.map((msg, i) => (
          <li key={i} className="text-danger d-flex align-items-center">
            <XCircleFill className="me-2" />
            <span>{msg}</span>
          </li>
        ))
      ) : (
        <li className="text-success d-flex align-items-center">
          <CheckCircleFill className="me-2" />
          <span>La contraseña es segura</span>
        </li>
      )}
    </ul>
    /* <ul className="list-unstyled mt-2">
      {rules.map((rule, i) => {
        const passed = rule.test(password);
        return (
          <li
            key={i}
            className={`d-flex align-items-center ${
              passed ? "text-success" : "text-danger"
            }`}
          >
            {passed ? (
              <CheckCircleFill className="me-2" />
            ) : (
              <XCircleFill className="me-2" />
            )}
            <span>{rule.label}</span>
          </li>
        );
      })}
    </ul>*/
  );
};

export default PasswordRequirements;
