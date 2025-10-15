// src/features/auth/components/PasswordRequirements.jsx
import { CheckCircleFill, XCircleFill } from "react-bootstrap-icons";
import { useState, useEffect } from "react";

const PasswordRequirements = ({ password }) => {
  const rules = [
    { label: "Mínimo 8 caracteres", test: (v) => v.length >= 8 },
    { label: "Al menos una letra mayúscula", test: (v) => /[A-Z]/.test(v) },
    { label: "Al menos un número", test: (v) => /[0-9]/.test(v) },
    {
      label: "Al menos un carácter especial (!@#$%^&*)",
      test: (v) => /[!@#$%^&*]/.test(v),
    },
  ];

  const [touched, setTouched] = useState(false);

  // Detect when the user starts typing
  useEffect(() => {
    if (password.length > 0) setTouched(true);
    else setTouched(false);
  }, [password]);

  const allPassed = rules.every((rule) => rule.test(password));

  // Hide everything if user hasn't typed or all rules pass
  if (!touched || allPassed) return null;

  return (
    <ul className="list-unstyled mt-2">
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
    </ul>
  );
};

export default PasswordRequirements;
