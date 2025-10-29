import { useState, useEffect } from "react";
import {
  Container,
  Row,
  Col,
  Card,
  Button,
  Form,
  Spinner,
  Modal,
} from "react-bootstrap";
import { List } from "react-bootstrap-icons";
import Header from "../../components/header/Header";
import Sidebar from "../../components/sidebar/Sidebar";
import { useNavigate } from "react-router-dom";
import { usePatient } from "../../context/PatientContext";
import { updatePatient } from "../Patient/patientService";
import { getMe } from "./services/authService";
import "./ConfigurationProfile.css";

const ConfigurationProfile = () => {
  const { signOut } = usePatient();
  const navigate = useNavigate();

  const [user, setUser] = useState(null);
  const [editMode, setEditMode] = useState(false);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [showSuccess, setShowSuccess] = useState(false);
  const [showSidebar, setShowSidebar] = useState(false);

  // Cargar usuario
  useEffect(() => {
    const fetchUser = async () => {
      try {
        const data = await getMe();
        setUser(data);
      } catch (error) {
        console.error("Error al obtener usuario:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchUser();
  }, []);

  // Cerrar modal automáticamente después de 3 segundos
  useEffect(() => {
    if (showSuccess) {
      const timer = setTimeout(() => setShowSuccess(false), 3000);
      return () => clearTimeout(timer);
    }
  }, [showSuccess]);

  const handleChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const handleSave = async () => {
    if (!user) return;
    try {
      setSaving(true);
      const updated = await updatePatient(user.id, user);
      setUser(updated);
      setEditMode(false);
      setShowSuccess(true);
    } catch (error) {
      console.error("Error al actualizar paciente:", error);
      alert("Ocurrió un error al guardar los cambios.");
    } finally {
      setSaving(false);
    }
  };

  const handleLogout = async () => {
    await signOut();
    navigate("/login");
  };

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center vh-100">
        <Spinner animation="border" />
      </div>
    );
  }

  if (!user) {
    return (
      <p className="text-center mt-5">
        No se pudieron cargar los datos del perfil.
      </p>
    );
  }

  return (
    <div className="d-flex patient-dashboard">
      <Sidebar
        user={{
          id: user.id,
          name: user.name,
          lastName: user.lastName,
          avatar: "https://cdn-icons-png.flaticon.com/512/149/149071.png",
        }}
        role="patient"
        show={showSidebar}
        onHide={() => setShowSidebar(false)}
      />

      <div className="flex-grow-1">
        <Header
          title="MedTech: Tu portal de salud"
          avatarUrl="https://cdn-icons-png.flaticon.com/512/149/149071.png"
          buttons={[
            {
              label: "Cerrar sesión",
              onClick: handleLogout,
              className: "header-btn",
            },
          ]}
        />

        <div className="d-lg-none p-2">
          <Button
            variant="outline-primary"
            onClick={() => setShowSidebar(true)}
          >
            <List size={24} />
          </Button>
        </div>

        <Container className="py-4">
          <h1 className="fw-bold mb-4">Perfil del Paciente</h1>

          <Card className="p-4 shadow-sm border-0">
            <div className="d-flex justify-content-between align-items-center mb-3">
              <h4 className="fw-bold mb-0">Datos Personales</h4>
              <Button
                variant="primary"
                onClick={() => setEditMode(!editMode)}
                disabled={saving}
              >
                {editMode ? "Cancelar" : "Editar"}
              </Button>
            </div>

            <Form>
              <Row className="g-3">
                {[
                  ["name", "Nombre"],
                  ["lastName", "Apellido"],
                  ["email", "Correo electrónico", "email"],
                  ["birthDate", "Fecha de nacimiento", "date"],
                  ["gender", "Género"],
                  ["address", "Dirección"],
                  ["phone", "Teléfono"],
                  ["bloodType", "Tipo de sangre"],
                  ["city", "Ciudad"],
                  ["country", "País"],
                  ["zip", "Código Postal"],
                ].map(([key, label, type = "text"]) => (
                  <Col md={6} key={key}>
                    <Form.Group>
                      <Form.Label>{label}</Form.Label>
                      <Form.Control
                        type={type}
                        name={key}
                        value={user[key] || ""}
                        disabled={!editMode}
                        onChange={handleChange}
                      />
                    </Form.Group>
                  </Col>
                ))}
              </Row>

              {editMode && (
                <div className="text-end mt-4">
                  <Button
                    variant="secondary"
                    className="me-2"
                    onClick={() => setEditMode(false)}
                    disabled={saving}
                  >
                    Cancelar
                  </Button>
                  <Button
                    variant="primary"
                    onClick={handleSave}
                    disabled={saving}
                  >
                    {saving ? (
                      <>
                        <Spinner size="sm" className="me-2" /> Guardando...
                      </>
                    ) : (
                      "Guardar cambios"
                    )}
                  </Button>
                </div>
              )}
            </Form>
          </Card>

          <Modal
            show={showSuccess}
            onHide={() => setShowSuccess(false)}
            centered
          >
            <Modal.Header closeButton>
              <Modal.Title>Datos actualizados</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              Los datos del paciente se han actualizado correctamente.
            </Modal.Body>
          </Modal>
        </Container>
      </div>
    </div>
  );
};

export default ConfigurationProfile;
