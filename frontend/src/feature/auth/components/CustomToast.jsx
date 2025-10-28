// src/features/auth/components/CustomToast.jsx
import { Toast } from "react-bootstrap";

const CustomToast = ({ show, onClose, type = "success", title, message }) => {
  const bgColor =
    type === "success"
      ? "bg-success"
      : type === "error"
      ? "bg-danger"
      : "bg-warning text-dark";

  return (
    <Toast
      show={show}
      onClose={onClose}
      delay={3000}
      autohide
      className={`position-fixed top-0 end-0 m-3 text-white ${bgColor}`}
    >
      <Toast.Header closeButton={false}>
        <strong className="me-auto">{title}</strong>
      </Toast.Header>
      <Toast.Body>{message}</Toast.Body>
    </Toast>
  );
};

export default CustomToast;
