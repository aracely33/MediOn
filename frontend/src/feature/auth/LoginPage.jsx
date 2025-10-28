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
import { loginUser, getMe } from "./services/authService";
import { usePatient } from "../../context/PatientContext";
import { useDoctor } from "../../context/DoctorContext";
import { useNavigate, Link } from "react-router-dom";

import logo from "../../assets/logoMed.svg";

export const validationSchema = Yup.object({
  email: Yup.string()
    .email("Debe ser un correo válido")
    .required("El correo es obligatorio")
    .matches(
      /^(?=.*[A-Za-z])[A-Za-z0-9@._-]+$/,
      "El correo debe contener al menos una letra y puede incluir números"
    ),
  password: Yup.string().required("La contraseña es obligatoria"),
});

function LoginPage() {
  const { signIn: signInPatient } = usePatient();
  const { signIn: signInDoctor } = useDoctor();

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
              <Link to="/">
                <img
                  src={logo}
                  alt="MedTech Logo"
                  style={{
                    width: "100px",
                    marginBottom: "1rem",
                    cursor: "pointer",
                    borderRadius: "30px",
                  }}
                />
              </Link>
              <h1 className="brand-title">Portal de Citas MedTech</h1>
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
                    try {
                      const response = await loginUser(values);
                      const { token } = response.data;

                      if (!token) {
                        console.error("No se recibió token del backend");
                        return;
                      }

                      // Guarda el token genérico (para getMe)
                      localStorage.setItem("auth_token", token);

                      // Obtener datos del usuario autenticado
                      const userData = await getMe();
                      console.log("Usuario autenticado:", userData);

                      // Detectar el rol automáticamente
                      if (userData.roles.includes("PATIENT")) {
                        await signInPatient(values); // usa tu contexto
                        navigate("/patient-home");
                      } else if (userData.roles.includes("PROFESSIONAL")) {
                        await signInDoctor(values);
                        navigate("/doctor-home");
                      } else {
                        console.warn("Rol desconocido:", userData.roles);
                      }
                    } catch (error) {
                      console.error("⚠️ Error al iniciar sesión:", error);
                    }
                  }}
                >
                  {({
                    handleSubmit,
                    handleChange,
                    values,
                    errors,
                    touched,
                    handleBlur,
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
                          onBlur={handleBlur}
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
                        <Form.Label className="fw-bold">Contraseña</Form.Label>
                        <Form.Control
                          type={showPassword ? "text" : "password"}
                          name="password"
                          placeholder="Ingresa tu contraseña"
                          value={values.password}
                          onBlur={handleBlur}
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
                            href="/forgot-password"
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
                        <Card.Link href="/signup"> Regístrate aquí</Card.Link>
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
