// src/features/auth/components/TermsModal.jsx
import { Modal } from "react-bootstrap";

const TermsModal = ({ show, onHide }) => (
  <Modal show={show} onHide={onHide} centered>
    <Modal.Header closeButton>
      <Modal.Title>Términos y Condiciones</Modal.Title>
    </Modal.Header>
    <Modal.Body>
      <p>
        Aquí van los términos y condiciones de uso del portal HealthConnect...
      </p>
    </Modal.Body>
  </Modal>
);

export default TermsModal;
