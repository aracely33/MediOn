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
import axios from "axios";
import { medicalEntries as mockEntries } from "../../../data/medicalEntries";

const MedicalHistory = () => {
  const [filter, setFilter] = useState("all");
  const [search, setSearch] = useState("");
  const [selectedEntry, setSelectedEntry] = useState(null);
  const [entries, setEntries] = useState([]); // 🔹 datos cargados
  const [loading, setLoading] = useState(true);

  const now = new Date();
  const useMockData = true; // 🧪 cambia a false cuando tu API esté lista

  // 🧩 Simulación o consumo real de la API
  const fetchMedicalEntries = async () => {
    try {
      setLoading(true);
      if (useMockData) {
        console.log("🧪 Usando datos simulados...");
        // Simula un delay de red
        setTimeout(() => {
          setEntries(mockEntries);
          setLoading(false);
        }, 5000);
      } else {
        console.log("🌐 Llamando API real...");
        const medicalRecordId = 1; // ⚙️ Reemplaza con el ID real del paciente logueado
        const response = await axios.get(
          `http://localhost:8080/medical-records/${medicalRecordId}/entries/details`,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("patient_token")}`,
            },
          }
        );
        console.log("✅ Datos obtenidos:", response.data);
        // Adapta los datos del backend al formato actual
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

  // 🔍 Filtros (se aplican igual que antes)
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

  return (
    <div className="p-4">
      {/* ENCABEZADO */}
      <header className="d-flex justify-content-between align-items-center border-bottom pb-3 mb-4">
        <h2 className="fw-bold text-dark">Historial Clínico del Paciente</h2>
        <div className="d-flex gap-2">
          <Button variant="primary" className="d-flex align-items-center gap-2">
            <svg
              className="w-6 h-6 text-gray-800"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              fill="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                fillRule="evenodd"
                d="M9 7V2.221a2 2 0 0 0-.5.365L4.586 6.5a2 2 0 0 0-.365.5H9Zm2 0V2h7a2 2 0 0 1 2 2v6.41A7.5 7.5 0 1 0 10.5 22H6a2 2 0 0 1-2-2V9h5a2 2 0 0 0 2-2Z"
                clipRule="evenodd"
              />
              <path
                fillRule="evenodd"
                d="M9 16a6 6 0 1 1 12 0 6 6 0 0 1-12 0Zm6-3a1 1 0 0 1 1 1v1h1a1 1 0 1 1 0 2h-1v1a1 1 0 1 1-2 0v-1h-1a1 1 0 1 1 0-2h1v-1a1 1 0 0 1 1-1Z"
                clipRule="evenodd"
              />
            </svg>
            Añadir Entrada
          </Button>
          <Button variant="primary">
            <svg
              className="w-8 h-8 text-gray-800"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              fill="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                fillRule="evenodd"
                d="M8 3a2 2 0 0 0-2 2v3h12V5a2 2 0 0 0-2-2H8Zm-3 7a2 2 0 0 0-2 2v5a2 2 0 0 0 2 2h1v-4a1 1 0 0 1 1-1h10a1 1 0 0 1 1 1v4h1a2 2 0 0 0 2-2v-5a2 2 0 0 0-2-2H5Zm4 11a1 1 0 0 1-1-1v-4h8v4a1 1 0 0 1-1 1H9Z"
                clipRule="evenodd"
              />
            </svg>
            Imprimir
          </Button>
          <Button variant="primary">
            <svg
              className="w-[29px] h-[29px] text-gray-800"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              fill="none"
              viewBox="0 0 24 24"
            >
              <path
                stroke="currentColor"
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M4 15v2a3 3 0 0 0 3 3h10a3 3 0 0 0 3-3v-2m-8 1V4m0 12-4-4m4 4 4-4"
              />
            </svg>
            Descargar
          </Button>
        </div>
      </header>

      {/* PACIENTE */}
      <Card className="p-3 mb-4">
        <Row className="align-items-center">
          <Col
            md={8}
            className="d-flex align-items-center gap-3 bg-danger bg-opacity-25 rounded"
          >
            <img
              src="https://lh3.googleusercontent.com/aida-public/AB6AXuDPcYD4rpaHO9ixkvhXAV5VBoDhpRWQg0o1m3yHvrogyzmbEfyHG9wN27bKsbOPyIgklR0pUWmn5DKF_8viMcbc2Sgmo7kJIvzyHssMie5cK3ZePfDbBSOELi-tkMFx8ONC0BlIuH27-IXpupGip5-n4rY4v1T7WXAcqor--seGr4eMBiROvcHDPYJlcYqcwdp4JmIsqC38ynfilgOpwUS3Bv3Irsr5JpDNHjVeHUF3VqBSu_FxIfWdxnevXvU0C8QMNBQE2oaOWMY"
              alt="Paciente"
              className="rounded"
              width={70}
            />
            <div>
              <h5 className="mb-1 fw-bold">Juan Pérez</h5>
              <p className="text-muted mb-0">
                Fecha de nacimiento: 15/05/1985 | ID Médico: 789456
              </p>
            </div>
          </Col>
          <Col md={4} className="d-flex flex-wrap gap-2 justify-content-end">
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

      {/* BUSCADOR Y TIPOS */}
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

      {/* HISTORIAL */}
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
                  <Card className="p-3 shadow-sm">
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

      {/* FOOTER */}
      <footer className="text-center text-muted mt-5">
        <small>
          La confidencialidad de su historial clínico está protegida. Todos los
          datos están encriptados y seguros.
        </small>
      </footer>

      {/* MODAL POPUP */}
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
            {selectedEntry && new Date(selectedEntry.date).toLocaleDateString()}
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
  );
};

export default MedicalHistory;
