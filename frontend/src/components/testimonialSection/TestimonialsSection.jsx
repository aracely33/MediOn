// src/components/testimonials/TestimonialsSection.jsx
import { Container, Row, Col, Card } from "react-bootstrap";

const testimonials = [
  {
    name: "Dr. Juan Pérez",
    text: "La plataforma ha transformado la gestión de mi clínica, ahora todo es más eficiente.",
    image:
      "https://lh3.googleusercontent.com/aida-public/AB6AXuC6Oc7J560E9conCqjYRWjvH2g_5CK6YqR1Aez3eEnnbugT4ZlJM0bUU7a-rb8hnXghumcSeeZkBy4ILMkhaYocGT_to3MDGkjG4aO7QVuQ9DP051hQTopakLTIvdFDJZ6HWcmYb8No27-lX_zOseY6tBwj73RiKQveuSVHqK6eI66DfHNQtFLF5ZzX2JzdJj8srohO4-Jr7nUszgUUHjqZT8nft6PB35sPI9JoF9IPqaTRnTjeLdhnmeLv5rwI4iwP2kyjiO268MM",
  },
  {
    name: "María García",
    text: "Agendar mis citas nunca ha sido tan fácil y cómodo. ¡Totalmente recomendado!",
    image:
      "https://lh3.googleusercontent.com/aida-public/AB6AXuAnMgPPyTyGKa2NE1IJDqXe6TH0iPL6XyqpSxm6YArhwgAf2HGEdfjur5O8e22IT4j6GUl7geTsfRYZvAFgK3PXHyHMYxX_Ra3GEi__CrfxCFPJ6aUzs_KeMw8iNqt9Vovw-iBpFd4dskxH-AxbRkUAJ3gyhAfR13uvhVCfLHFYSczEMRmMiGn8-gtPZWJWNHopQdbzFCfz7x10ObWLWPP109QEJzArHq5E2y0Iji078M9mvDfuN8VU2GlVDndcLqRK34-CoCRnYXE",
  },
];

const TestimonialsSection = () => (
  <section id="testimonios" className="py-5 bg-light">
    <Container>
      <h2 className="text-center fw-bold mb-5">Historias de éxito</h2>
      <Row className="g-4 justify-content-center">
        {testimonials.map((t, i) => (
          <Col key={i} md={4}>
            <Card className="text-center p-3 shadow-sm h-100">
              <Card.Img
                variant="top"
                src={t.image}
                className="rounded-circle mx-auto mt-3"
                style={{ width: "100px", height: "100px", objectFit: "cover" }}
              />
              <Card.Body>
                <Card.Title>{t.name}</Card.Title>
                <Card.Text>{t.text}</Card.Text>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
    </Container>
  </section>
);

export default TestimonialsSection;
