// src/components/testimonials/TestimonialsSection.jsx
import { useState, useEffect } from "react";
import { Container, Card, Carousel, Row, Col } from "react-bootstrap";
import "./TestimonialsSection.css";

const testimonials = [
  {
    name: "Dr. Juan Pérez",
    text: "La plataforma ha transformado la gestión de mi clínica, ahora todo es más eficiente.",
    image: "https://randomuser.me/api/portraits/men/32.jpg",
  },
  {
    name: "María García",
    text: "Agendar mis citas nunca ha sido tan fácil y cómodo. ¡Totalmente recomendado!",
    image: "https://randomuser.me/api/portraits/women/44.jpg",
  },
  {
    name: "Clínica Salud Plus",
    text: "Nuestro equipo ha reducido el tiempo de atención a pacientes gracias a MedTech.",
    image: "https://randomuser.me/api/portraits/men/65.jpg",
  },
  {
    name: "Luis Hernández",
    text: "Poder ver mi historial médico desde mi celular me da mucha tranquilidad.",
    image: "https://randomuser.me/api/portraits/men/22.jpg",
  },
  {
    name: "Dra. Ana López",
    text: "El seguimiento de pacientes nunca había sido tan sencillo y organizado.",
    image: "https://randomuser.me/api/portraits/women/33.jpg",
  },
  {
    name: "Clínica Zaragoza",
    text: "La integración con nuestro sistema interno fue rápida y sin complicaciones.",
    image: "https://randomuser.me/api/portraits/women/55.jpg",
  },
  {
    name: "Carla Méndez",
    text: "Recibir recordatorios de mis citas es genial, no se me olvida ninguna.",
    image: "https://randomuser.me/api/portraits/women/66.jpg",
  },
  {
    name: "Dr. Ricardo Torres",
    text: "La comunicación con mis pacientes mejoró significativamente con MedTech.",
    image: "https://randomuser.me/api/portraits/men/77.jpg",
  },
  {
    name: "Clínica Vida Saludable",
    text: "Ahora podemos gestionar varias sucursales desde una sola plataforma.",
    image: "https://randomuser.me/api/portraits/men/88.jpg",
  },
  {
    name: "Sofía Ramírez",
    text: "Todo es intuitivo y rápido, desde agendar hasta consultar resultados.",
    image: "https://randomuser.me/api/portraits/women/99.jpg",
  },
  {
    name: "Clínica Horizonte",
    text: "Nuestro personal se ha adaptado muy rápido gracias a la interfaz amigable de MedTech.",
    image: "https://randomuser.me/api/portraits/men/90.jpg",
  },
  {
    name: "Fernando López",
    text: "Poder recibir mis resultados en línea me ahorra mucho tiempo y estrés.",
    image: "https://randomuser.me/api/portraits/men/91.jpg",
  },
];

// Función para dividir testimonios en grupos según el tamaño del slide
const chunkArray = (arr, size) => {
  const chunks = [];
  for (let i = 0; i < arr.length; i += size) {
    chunks.push(arr.slice(i, i + size));
  }
  return chunks;
};

const TestimonialsSection = () => {
  const [itemsPerSlide, setItemsPerSlide] = useState(3);

  useEffect(() => {
    const updateItemsPerSlide = () => {
      const width = window.innerWidth;
      if (width < 768) setItemsPerSlide(1); // xs: móvil
      else if (width < 992) setItemsPerSlide(2); // md: tablet
      else setItemsPerSlide(3); // lg+: desktop
    };

    updateItemsPerSlide();
    window.addEventListener("resize", updateItemsPerSlide);

    return () => window.removeEventListener("resize", updateItemsPerSlide);
  }, []);

  const slides = chunkArray(testimonials, itemsPerSlide);

  return (
    <section id="testimonios" className="py-5">
      <Container>
        <h2 className="text-center fw-bold mb-5">Historias de éxito</h2>
        <Carousel indicators={true}>
          {slides.map((chunk, idx) => (
            <Carousel.Item key={idx}>
              <Row className="justify-content-center g-4">
                {chunk.map((t, i) => (
                  <Col key={i} xs={12} md={6} lg={4}>
                    <Card className="text-center p-3 shadow-sm h-100 testimoni">
                      <Card.Img
                        variant="top"
                        src={t.image}
                        className="rounded-circle mx-auto mt-3"
                        style={{
                          width: "100px",
                          height: "100px",
                          objectFit: "cover",
                        }}
                      />
                      <Card.Body>
                        <Card.Title>{t.name}</Card.Title>
                        <Card.Text>{t.text}</Card.Text>
                      </Card.Body>
                    </Card>
                  </Col>
                ))}
              </Row>
            </Carousel.Item>
          ))}
        </Carousel>
      </Container>
    </section>
  );
};

export default TestimonialsSection;
