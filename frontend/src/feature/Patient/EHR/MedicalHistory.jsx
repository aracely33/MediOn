import { useState, useEffect } from "react";
import {
  Container,
  Row,
  Col,
  Card,
  Button,
  Form,
  Modal,
  Spinner,
} from "react-bootstrap";
import { List, Download, Printer, ArrowRepeat } from "react-bootstrap-icons";
import Header from "../../../components/header/Header";
import Sidebar from "../../../components/sidebar/Sidebar";
import axios from "axios";
import { usePatient } from "../../../context/PatientContext";
import { medicalEntries as mockEntries } from "../../../data/medicalEntries";
import "./MedicalHistory.css"; // 💅 para el efecto de animación

const MedicalHistory = () => {
  const { signOut } = usePatient();
  const [showSidebar, setShowSidebar] = useState(false);
  const [filter, setFilter] = useState("all");
  const [search, setSearch] = useState("");
  const [selectedEntry, setSelectedEntry] = useState(null);
  const [entries, setEntries] = useState([]);
  const [loading, setLoading] = useState(true);

  const now = new Date();
  const useMockData = true;

  const fetchMedicalEntries = async () => {
    try {
      setLoading(true);
      if (useMockData) {
        console.log("🧪 Usando datos simulados...");
        setTimeout(() => {
          setEntries(mockEntries);
          setLoading(false);
        }, 2000);
      } else {
        console.log("🌐 Llamando API real...");
        const medicalRecordId = 1;
        const response = await axios.get(
          `http://localhost:8080/medical-records/${medicalRecordId}/entries/details`,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("patient_token")}`,
            },
          }
        );
        const formatted = response.data.map((item) => ({
          id: item.id,
          title: item.summary || "Entrada médica",
          date: item.creationDate,
          desc: item.description || "Sin descripción",
          type: item.type,
          severity: item.diagnoses?.[0]?.severity?.toLowerCase() || "normal",
          details: item.observations || "Sin observaciones",
          icon:
            item.type === "Diagnósticos"
              ? "🚑"
              : item.type === "Tratamientos"
              ? "💉"
              : item.type === "Consultas"
              ? "🩺"
              : item.type === "Medicamentos"
              ? "💊"
              : "🧬",
          imported: Math.random() > 0.5,
        }));
        setEntries(formatted);
      }
    } catch (error) {
      console.error("⚠️ Error al obtener entradas médicas:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMedicalEntries();
  }, []);

  const handleLogout = async () => {
    await signOut();
    window.location.href = "/login";
  };

  const filteredEntries = entries.filter((entry) => {
    const entryDate = new Date(entry.date);
    const monthsDiff =
      (now.getFullYear() - entryDate.getFullYear()) * 12 +
      (now.getMonth() - entryDate.getMonth());

    if (filter === "last3") return monthsDiff <= 3;
    if (filter === "recentDiagnoses")
      return entry.type === "Diagnósticos" && monthsDiff <= 1;
    if (filter === "critical") return entry.severity === "high";
    if (filter !== "all" && filter !== "search") return entry.type === filter;

    if (search.trim() !== "") {
      const term = search.toLowerCase();
      return (
        entry.title.toLowerCase().includes(term) ||
        entry.desc.toLowerCase().includes(term)
      );
    }

    return true;
  });

  const handlePrint = () => window.print();
  const handleDownload = () => alert("Descargando historial clínico...");

  return (
    <div className="d-flex patient-dashboard">
      <Sidebar
        user={{
          id: 1,
          name: "Juan",
          lastName: "Pérez",
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
          {/* 🔹 Título con botones */}
          <div className="d-flex justify-content-between align-items-center mb-4">
            <h1 className="fw-bold mb-0">Historial Clínico del Paciente</h1>
            <div className="d-flex gap-2">
              <Button variant="outline-secondary" onClick={handlePrint}>
                <Printer size={18} className="me-1" /> Imprimir
              </Button>
              <Button variant="outline-success" onClick={handleDownload}>
                <Download size={18} className="me-1" /> Descargar
              </Button>
            </div>
          </div>

          {/* 🔹 Card encabezado */}
          <Card className="p-3 mb-4">
            <Row className="align-items-center">
              <Col md={8} className="d-flex align-items-center gap-3">
                <img
                  src="https://cdn-icons-png.flaticon.com/512/149/149071.png"
                  alt="Paciente"
                  className="rounded"
                  width={70}
                />
                <div>
                  <h5 className="mb-1 fw-bold d-flex align-items-center gap-2">
                    Juan Pérez{" "}
                    <span className="text-success d-flex align-items-center gap-1">
                      <ArrowRepeat size={16} className="spin-icon" />{" "}
                      Sincronizado con EHR
                    </span>
                  </h5>
                  <p className="text-muted mb-0">
                    Fecha de nacimiento: 15/05/1985 | ID Médico: 789456
                  </p>
                </div>
              </Col>
              <Col
                md={4}
                className="d-flex flex-wrap gap-2 justify-content-end"
              >
                <Button variant="light" onClick={() => setFilter("last3")}>
                  Últimos 3 meses
                </Button>
                <Button
                  variant="outline-secondary"
                  onClick={() => setFilter("critical")}
                >
                  Alertas críticas
                </Button>
                <Button
                  variant="outline-secondary"
                  onClick={() => setFilter("recentDiagnoses")}
                >
                  Diagnósticos recientes
                </Button>
              </Col>
            </Row>
          </Card>

          {/* 🔹 Buscador */}
          <Card className="p-3 mb-4">
            <Row className="align-items-center g-3">
              <Col md={6}>
                <Form.Control
                  type="text"
                  placeholder="Buscar en el historial clínico..."
                  value={search}
                  onChange={(e) => setSearch(e.target.value)}
                />
              </Col>
              <Col md={6} className="d-flex gap-2 flex-wrap">
                <Button variant="light" onClick={() => setFilter("all")}>
                  Todas
                </Button>
                <Button
                  variant="outline-secondary"
                  onClick={() => setFilter("Consultas")}
                >
                  Consultas
                </Button>
                <Button
                  variant="outline-secondary"
                  onClick={() => setFilter("Diagnósticos")}
                >
                  Diagnósticos
                </Button>
                <Button
                  variant="outline-secondary"
                  onClick={() => setFilter("Tratamientos")}
                >
                  Tratamientos
                </Button>
                <Button
                  variant="outline-secondary"
                  onClick={() => setFilter("Medicamentos")}
                >
                  Medicamentos
                </Button>
                <Button
                  variant="outline-secondary"
                  onClick={() => setFilter("Resultados")}
                >
                  Resultados
                </Button>
              </Col>
            </Row>
          </Card>

          {/* 🔹 Entradas */}
          <Container fluid className="overflow-auto pb-4">
            {loading ? (
              <div className="text-center p-5">
                <Spinner animation="border" /> <p>Cargando historial...</p>
              </div>
            ) : (
              <Row className="flex-nowrap">
                {filteredEntries.length > 0 ? (
                  filteredEntries.map((item) => (
                    <Col key={item.id} xs={12} md={3} className="me-3">
                      <Card className="p-3 shadow-sm position-relative">
                        {item.imported && (
                          <span className="position-absolute top-0 end-0 badge bg-success d-flex align-items-center gap-1 m-2">
                            <ArrowRepeat size={12} className="spin-icon" />{" "}
                            Importado de EHR
                          </span>
                        )}
                        <div className="d-flex align-items-center mb-2">
                          <span className="me-2 fs-4">{item.icon}</span>
                          <div>
                            <h6 className="mb-0 fw-bold">{item.title}</h6>
                            <small className="text-muted">
                              {new Date(item.date).toLocaleDateString("es-MX", {
                                day: "numeric",
                                month: "long",
                                year: "numeric",
                              })}
                            </small>
                          </div>
                        </div>
                        <p className="text-muted small mb-1">{item.desc}</p>
                        <Button
                          variant="link"
                          className="p-0"
                          onClick={() => setSelectedEntry(item)}
                        >
                          Ver reporte
                        </Button>
                      </Card>
                    </Col>
                  ))
                ) : (
                  <p className="text-muted">No se encontraron registros.</p>
                )}
              </Row>
            )}
          </Container>

          <footer className="text-center text-muted mt-5">
            <small>
              La confidencialidad de su historial clínico está protegida. Todos
              los datos están encriptados y seguros.
            </small>
          </footer>
        </Container>

        {/* Modal */}
        <Modal
          show={!!selectedEntry}
          onHide={() => setSelectedEntry(null)}
          centered
        >
          <Modal.Header closeButton>
            <Modal.Title>{selectedEntry?.title}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <p>
              <strong>Fecha:</strong>{" "}
              {selectedEntry &&
                new Date(selectedEntry.date).toLocaleDateString()}
            </p>
            <p>
              <strong>Tipo:</strong> {selectedEntry?.type}
            </p>
            <p>
              <strong>Severidad:</strong> {selectedEntry?.severity}
            </p>
            <p>{selectedEntry?.details}</p>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setSelectedEntry(null)}>
              Cerrar
            </Button>
          </Modal.Footer>
        </Modal>
      </div>
    </div>
  );
};

export default MedicalHistory;
