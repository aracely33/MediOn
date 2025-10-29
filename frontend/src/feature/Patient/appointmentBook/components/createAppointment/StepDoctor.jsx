import { Card, Row, Col, Button, Spinner, Alert } from "react-bootstrap";
import { SearchBar } from "./SearchBar";
import { useEffect, useState } from "react";
import { getAllProfessionals } from "../../../../Doctor/doctorService";

export const StepDoctor = ({
  onNext,
  currentStep,
  prev,
  formik,
  setDoctor,
}) => {
  const [doctors, setDoctors] = useState([]);
  const [allDoctors, setAllDoctors] = useState([]);
  const [loading, setLoading] = useState(true);

  const handleDoctorSelect = (doctor) => {
    formik.setFieldValue("doctorId", doctor.id);
    setDoctor(doctor);
  };

  useEffect(() => {
    const getAllDoctors = async () => {
      try {
        const res = await getAllProfessionals();
        setDoctors(res.content);
        setAllDoctors(res.content);
        setLoading(false);
      } catch (error) {
        console.error("Error fetching doctors:", error);
        setLoading(false);
      }
    };

    getAllDoctors();
  }, []);

  const handleSearch = (query) => {
    if (!query) {
      setDoctors(allDoctors);
      return;
    }

    let filterDoctor = allDoctors;

    if (query) {
      filterDoctor = filterDoctor.filter((doctor) =>
        doctor.specialty.toLowerCase().includes(query.toLowerCase())
      );
    }

    setDoctors(filterDoctor);
  };

  return (
    <Card className="p-4 shadow-sm animate-bounce-in">
      <h4 className="mb-3 text-primary fw-bold">
        Paso 2 de 5: Busca la especialidad y selecciona a un médico
      </h4>
      {loading ? (
        <div className="d-flex justify-content-center align-items-center py-5">
          <Spinner animation="border" role="status" className="me-2" />{" "}
          <span className="text-muted">Cargando médicos...</span>
        </div>
      ) : allDoctors.length === 0 ? (
        <div className="w-100">
          <Alert variant="warning" className="text-center">
            Aún no hay médicos registrados.
          </Alert>
        </div>
      ) : doctors.length === 0 ? (
        <>
          <SearchBar onSearch={handleSearch} />
          <div className="w-100">
            <Alert variant="primary" className="text-center">
              No se encontraron médicos con esa especialidad filtrada.
            </Alert>
          </div>
        </>
      ) : (
        <>
          <SearchBar onSearch={handleSearch} />
          <Row>
            {doctors.map((doctor) => (
              <Col md={6} lg={3} key={doctor.id} className="mb-3">
                <Card
                  className={`text-center p-3 ${
                    formik.values.doctorId === doctor.id
                      ? "border-primary border-3 shadow-sm"
                      : "border-light"
                  }`}
                  onClick={() => handleDoctorSelect(doctor)}
                  style={{ cursor: "pointer" }}
                >
                  <img
                    src={"https://randomuser.me/api/portraits/men/32.jpg"}
                    alt={doctor.name}
                    className="rounded-circle mb-3 mx-auto d-block"
                    width="80"
                    height="80"
                    style={{ objectFit: "cover" }}
                  />
                  <Card.Title className="fs-6 fw-bold text-dark">
                    {doctor.name} {doctor.lastName}
                  </Card.Title>
                  <Card.Text className="text-muted small">
                    {doctor.specialty}
                  </Card.Text>
                </Card>
              </Col>
            ))}
          </Row>
        </>
      )}

      <div className="d-flex justify-content-between mt-4">
        <div className="text-start mt-3">
          <Button
            variant="outline-secondary"
            disabled={currentStep === 1}
            onClick={prev}
          >
            Paso Anterior
          </Button>
        </div>

        <div className="text-end mt-3">
          <Button
            variant="success"
            disabled={!formik.values.doctorId}
            onClick={onNext}
          >
            Continuar
          </Button>
        </div>
      </div>
    </Card>
  );
};
