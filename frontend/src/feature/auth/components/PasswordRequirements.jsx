// src/features/auth/components/PasswordRequirements.jsx
import { CheckCircleFill, XCircleFill } from "react-bootstrap-icons";

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
