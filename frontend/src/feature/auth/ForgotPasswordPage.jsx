// src/features/auth/ForgotPasswordPage.jsx
import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { Container, Row, Col, Card, Form, Button } from "react-bootstrap";
import { Formik } from "formik";
import * as Yup from "yup";
import CustomToast from "./components/CustomToast";
import "./auth.css"; // asumiendo que tienes tus estilos de login
import logo from "../../assets/logoMed.svg";

const ForgotPasswordPage = () => {
  const [toast, setToast] = useState({
    show: false,
    type: "success",
    title: "",
    message: "",
  });
  const [showOverlay, setShowOverlay] = useState(false);
  const navigate = useNavigate();

  // Simulación de llamada a API con delay
  const sendResetEmail = async (email, scenario = "success") => {
    setShowOverlay(true);
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        setShowOverlay(false);
        if (scenario === "success") {
          resolve({
            success: true,
            message: `Correo de recuperación enviado a ${email}`,
          });
        } else if (scenario === "not_found") {
          reject({ success: false, message: "El correo no está registrado" });
        } else {
          reject({ success: false, message: "Error inesperado" });
        }
      }, 1500);
    });
  };

  const showToast = (type, title, message) => {
    setToast({ show: true, type, title, message });
  };

  const validationSchema = Yup.object({
    email: Yup.string()
      .email("Correo no válido")
      .matches(
        /^(?=.*[A-Za-z])[A-Za-z0-9@._-]+$/,
        "El correo debe contener al menos una letra y puede incluir números"
      )
      .required("El correo es obligatorio"),
  });

  return (
    <div className="login-bg min-vh-100 d-flex flex-column position-relative">
      <Container
        fluid
        className="flex-grow-1 d-flex align-items-center justify-content-center"
      >
        <Row className="w-100 justify-content-center">
          <Col xs={11} sm={9} md={7} lg={5} xl={4}>
            <div className="text-center mb-4">
              <Link to="/">
                <img
                  src={logo}
                  alt="MedTech Logo"
                  style={{
                    width: "120px",
                    marginBottom: "1rem",
                    cursor: "pointer",
                    borderRadius: "30px",
                  }}
                />
              </Link>
              <h2 className="text-light">Recupera tu contraseña</h2>
            </div>

            <Card className="shadow-sm border-0 rounded-4">
              <Card.Body className="p-4">
                <Formik
                  initialValues={{ email: "" }}
                  validationSchema={validationSchema}
                  onSubmit={async (values, { setSubmitting, resetForm }) => {
                    setSubmitting(true);
                    try {
                      // Puedes cambiar el segundo argumento a "not_found" o "error" para probar
                      const response = await sendResetEmail(
                        values.email,
                        "success"
                      );
                      showToast(
                        "success",
                        "Correo enviado 🎉",
                        response.message
                      );
                      resetForm();
                    } catch (error) {
                      showToast("error", "Error 😕", error.message);
                    } finally {
                      setSubmitting(false);
                    }
                  }}
                >
                  {({
                    handleSubmit,
                    handleChange,
                    handleBlur,
                    values,
                    errors,
                    touched,
                    isSubmitting,
                  }) => (
                    <Form noValidate onSubmit={handleSubmit}>
                      <Form.Group className="mb-3">
                        <Form.Label className="fw-bold text-dark">
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

                      <Button
                        type="submit"
                        className="login-btn mb-2"
                        disabled={
                          isSubmitting || !values.email || !!errors.email
                        } // deshabilitado si hay errores o no hay texto
                      >
                        {isSubmitting ? "Enviando..." : "Enviar correo"}
                      </Button>

                      <Card.Link
                        href="/login"
                        className="text-decoration-none text-secondary"
                      >
                        Volver al inicio de sesión
                      </Card.Link>

                      <hr />
                      {/* Botones para simular distintos mensajes del backend */}
                      <div className="d-flex flex-column gap-2">
                        <Button
                          variant="outline-success"
                          onClick={async () => {
                            try {
                              await sendResetEmail(
                                "usuario@example.com",
                                "success"
                              );
                              showToast(
                                "success",
                                "Correo enviado 🎉",
                                "Correo enviado correctamente (simulación)"
                              );
                            } catch {}
                          }}
                        >
                          Simular éxito
                        </Button>
                        <Button
                          variant="outline-warning"
                          onClick={async () => {
                            try {
                              await sendResetEmail(
                                "usuario@example.com",
                                "not_found"
                              );
                            } catch (error) {
                              showToast(
                                "error",
                                "No registrado ⚠️",
                                error.message
                              );
                            }
                          }}
                        >
                          Simular correo no registrado
                        </Button>
                        <Button
                          variant="outline-danger"
                          onClick={async () => {
                            try {
                              await sendResetEmail(
                                "usuario@example.com",
                                "error"
                              );
                            } catch (error) {
                              showToast(
                                "error",
                                "Error inesperado ❌",
                                error.message
                              );
                            }
                          }}
                        >
                          Simular error
                        </Button>
                      </div>
                    </Form>
                  )}
                </Formik>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>

      {/* Overlay */}
      {showOverlay && (
        <div
          style={{
            position: "fixed",
            top: 0,
            left: 0,
            width: "100vw",
            height: "100vh",
            backgroundColor: "rgba(0,0,0,0.6)",
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
          <p>🔄 Procesando tu solicitud...</p>
        </div>
      )}

      {/* Toast */}
      <div
        style={{
          position: "fixed",
          top: "1rem",
          right: "1rem",
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
};

export default ForgotPasswordPage;
