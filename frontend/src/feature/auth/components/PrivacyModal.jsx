// src/features/auth/components/PrivacyModal.jsx
import { Modal } from "react-bootstrap";

const PrivacyModal = ({ show, onHide }) => (
  <Modal show={show} onHide={onHide} centered>
    <Modal.Header closeButton>
      <Modal.Title>Aviso de Privacidad</Modal.Title>
    </Modal.Header>
    <Modal.Body>
      <p>HealthConnect recopila y protege tus datos...</p>
    </Modal.Body>
  </Modal>
);

export default PrivacyModal;
