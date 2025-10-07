import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import Card from "react-bootstrap/Card";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import { Formik, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import "./LoginPage.css";

const validationSchema = Yup.object({
  email: Yup.string()
    .email("Debe ser un correo válido")
    .required("El correo es obligatorio"),
  password: Yup.string()
    .min(8, "Debe tener al menos 8 caracteres")
    .matches(/[A-Z]/, "Debe contener al menos una mayúscula")
    .matches(/[0-9]/, "Debe contener al menos un número")
    .matches(/[!@#$%^&*]/, "Debe contener al menos un carácter especial")
    .required("La contraseña es obligatoria"),
});

function LoginPage() {
  return (
    <div className="login-bg">
      <Container
        fluid
        className="vh-100 d-flex align-items-center justify-content-center"
      >
        <Row className="w-100 justify-content-center">
          <Col xs={11} sm={9} md={7} lg={5} xl={4}>
            <div className="text-center mb-4">
              <h1 className="brand-title">🩺 Portal de Citas MediON</h1>
            </div>

            <Card className="shadow-sm border-0 rounded-4">
              <Card.Body className="p-4">
                <Card.Title className="mb-3 fw-bold fs-4 text-dark text-center">
                  Iniciar Sesión
                </Card.Title>
                <Card.Subtitle className="mb-4 text-muted text-center">
                  Bienvenido al portal de coordinación de citas.
                </Card.Subtitle>

                <Formik
                  initialValues={{ email: "", password: "" }}
                  validationSchema={validationSchema}
                  // Aquí iría la solicitud a la api
                  onSubmit={(values) => {
                    console.log("Datos enviados:", values);
                  }}
                >
                  {({
                    handleSubmit,
                    handleChange,
                    values,
                    errors,
                    touched,
                  }) => (
                    <Form noValidate onSubmit={handleSubmit}>
                      <Form.Group className="mb-3" controlId="formBasicEmail">
                        <Form.Label>Correo electrónico</Form.Label>
                        <Form.Control
                          type="email"
                          name="email"
                          placeholder="usuario@correo.com"
                          value={values.email}
                          onChange={handleChange}
                          isInvalid={touched.email && !!errors.email}
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.email}
                        </Form.Control.Feedback>
                      </Form.Group>

                      <Form.Group
                        className="mb-3"
                        controlId="formBasicPassword"
                      >
                        <Form.Label>Contraseña</Form.Label>
                        <Form.Control
                          type="password"
                          name="password"
                          placeholder="Ingresa tu contraseña"
                          value={values.password}
                          onChange={handleChange}
                          isInvalid={touched.password && !!errors.password}
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.password}
                        </Form.Control.Feedback>
                        <div className="text-end mt-1">
                          <Card.Link href="#">
                            ¿Olvidaste tu contraseña?
                          </Card.Link>
                        </div>
                      </Form.Group>

                      <Button
                        variant="primary"
                        type="submit"
                        className="login-btn w-100"
                      >
                        Iniciar Sesión
                      </Button>

                      <p className="register-text mt-3">
                        ¿No tienes una cuenta?
                        <Card.Link href="#"> Regístrate aquí</Card.Link>
                      </p>
                    </Form>
                  )}
                </Formik>
              </Card.Body>
            </Card>

            <footer className="text-center mt-4 text-light small">
              © 2025 Portal de Citas. Todos los derechos reservados.{" "}
              <a href="#">Política de Privacidad</a>
            </footer>
          </Col>
        </Row>
      </Container>
    </div>
  );
}

export default LoginPage;
