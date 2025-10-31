// src/features/auth/LoginPage.jsx
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
import CustomToast from "./components/CustomToast";

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

  const [showOverlay, setShowOverlay] = useState(false);
  const [toast, setToast] = useState({
    show: false,
    type: "success",
    title: "",
    message: "",
  });
  const [loginError, setLoginError] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const navigate = useNavigate();
  const togglePasswordVisibility = () => setShowPassword((prev) => !prev);

  const showToast = (type, title, message) => {
    setToast({ show: true, type, title, message });
  };

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
                  onSubmit={async (values) => {
                    try {
                      setLoginError(false);
                      setShowOverlay(true);
                      const response = await loginUser(values);
                      const { token } = response.data;

                      if (!token)
                        throw new Error("No se recibió token del backend");

                      localStorage.setItem("auth_token", token);
                      const userData = await getMe();

                      showToast(
                        "success",
                        "Inicio de sesión exitoso 🎉",
                        "¡Bienvenido de nuevo!"
                      );

                      setTimeout(async () => {
                        if (userData.roles.includes("PATIENT")) {
                          await signInPatient(values);
                          navigate("/patient-home");
                        } else if (userData.roles.includes("PROFESSIONAL")) {
                          await signInDoctor(values);
                          navigate("/doctor-home");
                        }
                        setShowOverlay(false);
                      }, 1200);
                    } catch (error) {
                      console.error("⚠️ Error al iniciar sesión:", error);
                      setShowOverlay(false);
                      setLoginError(true);
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
                      {/* Email */}
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
                          isInvalid={
                            (touched.email && !!errors.email) || loginError
                          }
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.email ||
                            (loginError && "Verifica tus credenciales")}
                        </Form.Control.Feedback>
                      </Form.Group>

                      {/* Password */}
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
                          onBlur={handleBlur}
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

                      <p className="register-text mt-3 text-center">
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

      {/* Overlay de carga */}
      {showOverlay && (
        <div
          style={{
            position: "fixed",
            top: 0,
            left: 0,
            width: "100vw",
            height: "100vh",
            backgroundColor: "rgba(0, 0, 0, 0.6)",
            zIndex: 2000,
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            flexDirection: "column",
            color: "white",
            backdropFilter: "blur(2px)",
          }}
        >
          <div className="spinner-border text-light mb-3" role="status" />
          <p>🔐 Iniciando sesión...</p>
        </div>
      )}

      {/* Toast flotante */}
      <div
        style={{
          position: "fixed",
          bottom: "2rem",
          right: "2rem",
          zIndex: 3000,
        }}
      >
        <CustomToast
          show={toast.show}
          onClose={() => setToast({ ...toast, show: false })}
          type={toast.type}
          title={toast.title}
          message={toast.message}
        />
      </div>
    </div>
  );
}

export default LoginPage;
