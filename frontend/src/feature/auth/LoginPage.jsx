import { useState } from "react";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import Card from "react-bootstrap/Card";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import { Formik } from "formik";
import * as Yup from "yup";
import "./LoginPage.css";
import { usePatient } from "../../context/PatientContext";
import { useNavigate } from "react-router-dom";

const validationSchema = Yup.object({
  email: Yup.string()
    .email("Debe ser un correo válido")
    .required("El correo es obligatorio"),
  password: Yup.string().required("La contraseña es obligatoria"),
});

function LoginPage() {
  const { signIn: signInPatient } = usePatient();
  const [userType, setUserType] = useState("PATIENT");
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const togglePasswordVisibility = () => setShowPassword((prev) => !prev);

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
                  Bienvenido
                </Card.Title>
                <Card.Subtitle className="mb-4 text-muted text-center">
                  Portal de coordinación de citas médicas.
                </Card.Subtitle>

                <Formik
                  initialValues={{ email: "", password: "" }}
                  validationSchema={validationSchema}
                  // Aquí iría la solicitud a la api
                  onSubmit={async (values) => {
                    console.log("Datos todavía no enviados");
                    console.log("Datos enviados:", values);
                    try {
                      if (userType === "PATIENT") {
                        console.log("Se ingresa a PATIENT para signIn");
                        await signInPatient(values);
                        navigate("/patient-home");
                      }
                    } catch (error) {
                      console.log("Error to do login: ", error);
                    }
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
                        <Form.Label className="fw-bold">
                          Correo electrónico
                        </Form.Label>
                        <Form.Control
                          type="email"
                          name="email"
                          placeholder="usuario@correo.com"
                          value={values.email}
                          onChange={handleChange}
                          isInvalid={!errors.email}
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.email}
                        </Form.Control.Feedback>
                      </Form.Group>

                      <Form.Group
                        className="mb-3"
                        controlId="formBasicPassword"
                      >
                        <Form.Label className="fw-bold">Contraseña</Form.Label>
                        <Form.Control
                          type={showPassword ? "text" : "password"}
                          name="password"
                          placeholder="Ingresa tu contraseña"
                          value={values.password}
                          onChange={handleChange}
                          isInvalid={touched.password && !!errors.password}
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.password}
                        </Form.Control.Feedback>

                        <div className="d-flex justify-content-between mt-1">
                          <Card.Link
                            href="#"
                            onClick={(e) => {
                              e.preventDefault();
                              togglePasswordVisibility();
                            }}
                            className="text-decoration-none text-secondary"
                          >
                            {showPassword
                              ? "Ocultar contraseña"
                              : "Mostrar contraseña"}
                          </Card.Link>

                          <Card.Link
                            href="#"
                            className="text-decoration-none text-secondary"
                          >
                            ¿Olvidaste tu contraseña?
                          </Card.Link>
                        </div>
                      </Form.Group>

                      <Button
                        variant="primary"
                        disabled={
                          !values.email ||
                          !values.password ||
                          Object.keys(errors).length > 0
                        }
                        type="submit"
                        className="login-btn w-100"
                      >
                        Iniciar Sesión
                      </Button>

                      <p className="register-text mt-3">
                        ¿No tienes una cuenta?
                        <Card.Link href="/registro"> Regístrate aquí</Card.Link>
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
