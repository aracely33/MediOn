import { Card, Row, Col, Button } from "react-bootstrap";

export const StepDoctor = ({
  onNext,
  currentStep,
  prev,
  professional,
  setProfessional,
}) => {
  const doctors = [
    {
      id: 1,
      nombre: "Dr. Juan Pérez",
      especialidad: "Médico General",
      img: "https://randomuser.me/api/portraits/men/32.jpg",
    },
    {
      id: 2,
      nombre: "Dra. Laura García",
      especialidad: "Dermatóloga",
      img: "https://randomuser.me/api/portraits/women/44.jpg",
    },
    {
      id: 3,
      nombre: "Dr. Carlos López",
      especialidad: "Odontólogo",
      img: "https://randomuser.me/api/portraits/men/45.jpg",
    },
    {
      id: 4,
      nombre: "Dra. Ana Torres",
      especialidad: "Oftalmóloga",
      img: "https://randomuser.me/api/portraits/women/50.jpg",
    },
  ];

  return (
    <Card className="p-4 shadow-sm animate-bounce-in">
      <h4 className="mb-3 text-primary fw-bold">
        Paso 2 de 4: Selecciona un Médico
      </h4>
      <Row>
        {doctors.map((doctor) => (
          <Col md={6} lg={3} key={doctor.id} className="mb-3">
            <Card
              className={`text-center p-3 ${
                professional === doctor.nombre
                  ? "border-primary border-3 shadow-sm"
                  : "border-light"
              }`}
              onClick={() => setProfessional(doctor.nombre)}
              style={{ cursor: "pointer" }}
            >
              <img
                src={doctor.img}
                alt={doctor.nombre}
                className="rounded-circle mb-3 mx-auto d-block"
                width="80"
                height="80"
                style={{ objectFit: "cover" }}
              />
              <Card.Title className="fs-6 fw-bold text-dark">
                {doctor.nombre}
              </Card.Title>
              <Card.Text className="text-muted small">
                {doctor.especialidad}
              </Card.Text>
            </Card>
          </Col>
        ))}
      </Row>

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
          <Button variant="success" disabled={!professional} onClick={onNext}>
            Continuar
          </Button>
        </div>
      </div>
    </Card>
  );
};
