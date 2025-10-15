// src/features/auth/components/SuccessToast.jsx
import { Toast } from "react-bootstrap";

const SuccessToast = ({ show, onClose }) => (
  <Toast
    show={show}
    onClose={onClose}
    delay={3000}
    autohide
    className="position-fixed top-0 end-0 m-3 bg-success text-white"
  >
    <Toast.Header closeButton={false}>
      <strong className="me-auto">Registro exitoso</strong>
    </Toast.Header>
    <Toast.Body>¡Tu cuenta ha sido creada correctamente!</Toast.Body>
  </Toast>
);

export default SuccessToast;
