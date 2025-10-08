import { useState } from "react";
import {
  Button,
  Form,
  Card,
  Container,
  Row,
  Col,
  Modal,
  Toast,
} from "react-bootstrap";
import { Formik } from "formik";
import * as Yup from "yup";
import {
  Eye,
  EyeSlash,
  CheckCircleFill,
  XCircleFill,
} from "react-bootstrap-icons";
import "./SignupPage.css";

const validationSchema = Yup.object({
  name: Yup.string()
    .min(6, "Debe tener al menos 6 caracteres")
    .required("El nombre completo es obligatorio"),
  email: Yup.string()
    .email("Debe ser un correo válido")
    .required("El correo es obligatorio"),
  password: Yup.string()
    .min(8, "Debe tener al menos 8 caracteres")
    .matches(/[A-Z]/, "Debe contener al menos una mayúscula")
    .matches(/[0-9]/, "Debe contener al menos un número")
    .matches(/[!@#$%^&*]/, "Debe contener al menos un carácter especial")
    .required("La contraseña es obligatoria"),
  terms: Yup.boolean().oneOf(
    [true],
    "Debes aceptar los términos y condiciones"
  ),
});

function SignupPage() {
  const [showPassword, setShowPassword] = useState(false);
  const [showTerms, setShowTerms] = useState(false);
  const [showPrivacy, setShowPrivacy] = useState(false);
  const [showToast, setShowToast] = useState(false);

  const passwordChecks = [
    { label: "Mínimo 8 caracteres", test: (v) => v.length >= 8 },
    { label: "Al menos una letra mayúscula", test: (v) => /[A-Z]/.test(v) },
    { label: "Al menos un número", test: (v) => /[0-9]/.test(v) },
    {
      label: "Al menos un carácter especial (!@#$%^&*)",
      test: (v) => /[!@#$%^&*]/.test(v),
    },
  ];

  return (
    <div className="signup-bg">
      <div className="signup-bg">
        <Container
          fluid
          className="vh-100 d-flex align-items-center justify-content-center"
        >
          <Row className="w-100 justify-content-center">
            <Col xs={11} sm={9} md={7} lg={5} xl={4}>
              <div className="text-center mb-4">
                <h1 className="brand-title">🩺 HealthConnect</h1>
              </div>

              <Card className="shadow-sm border-0 rounded-4">
                <Card.Body className="p-4">
                  <Card.Title className="mb-3 fw-bold fs-4 text-dark text-center">
                    Crear cuenta
                  </Card.Title>
                  <Card.Subtitle className="mb-4 text-muted text-center">
                    Únete a nuestra comunidad de pacientes y doctores.
                  </Card.Subtitle>

                  <Formik
                    initialValues={{
                      name: "",
                      email: "",
                      password: "",
                      terms: false,
                    }}
                    validationSchema={validationSchema}
                    onSubmit={(values, { resetForm }) => {
                      console.log("Datos enviados:", values);
                      setShowToast(true);
                      resetForm();
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
                        {/* Nombre */}
                        <Form.Group className="mb-3">
                          <Form.Label className="fw-bold">
                            Nombre completo
                          </Form.Label>
                          <Form.Control
                            type="text"
                            name="name"
                            placeholder="Tu nombre"
                            value={values.name}
                            onChange={handleChange}
                            isInvalid={touched.name && !!errors.name}
                          />
                          <Form.Control.Feedback type="invalid">
                            {errors.name}
                          </Form.Control.Feedback>
                        </Form.Group>

                        {/* Email */}
                        <Form.Group className="mb-3">
                          <Form.Label className="fw-bold">
                            Correo electrónico
                          </Form.Label>
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

                        {/* Contraseña */}
                        <Form.Group className="mb-3">
                          <Form.Label className="fw-bold">
                            Contraseña
                          </Form.Label>
                          <div className="position-relative">
                            <Form.Control
                              type={showPassword ? "text" : "password"}
                              name="password"
                              placeholder="Crea una contraseña segura"
                              value={values.password}
                              onChange={handleChange}
                              isInvalid={touched.password && !!errors.password}
                              className="password-input-no-icon"
                            />
                            <Button
                              variant="link"
                              type="button"
                              className="position-absolute top-50 end-0 translate-middle-y me-2 p-0 text-muted"
                              onClick={() => setShowPassword(!showPassword)}
                            >
                              {showPassword ? <EyeSlash /> : <Eye />}
                            </Button>
                          </div>

                          {/* Lista de validaciones */}
                          <ul className="list-unstyled mt-2">
                            {passwordChecks.map((rule, i) => {
                              const passed = rule.test(values.password);
                              return (
                                <li
                                  key={i}
                                  className={`d-flex align-items-center ${
                                    passed ? "text-success" : "text-danger"
                                  }`}
                                >
                                  {passed ? (
                                    <CheckCircleFill className="me-2" />
                                  ) : (
                                    <XCircleFill className="me-2" />
                                  )}
                                  <span>{rule.label}</span>
                                </li>
                              );
                            })}
                          </ul>

                          <Form.Control.Feedback type="invalid">
                            {errors.password}
                          </Form.Control.Feedback>
                        </Form.Group>

                        {/* Términos */}
                        <Form.Group className="mb-3">
                          {/* Contenedor principal */}
                          <div>
                            <div className="d-flex align-items-center">
                              <Form.Check
                                type="checkbox"
                                name="terms"
                                id="terms"
                                checked={values.terms}
                                onChange={handleChange}
                                isInvalid={touched.terms && !!errors.terms}
                              />
                              <Form.Label htmlFor="terms" className="ms-2 mb-0">
                                Acepto los{" "}
                                <Card.Link
                                  href="#"
                                  onClick={() => setShowTerms(true)}
                                >
                                  Términos y Condiciones
                                </Card.Link>{" "}
                                y el{" "}
                                <Card.Link
                                  href="#"
                                  onClick={() => setShowPrivacy(true)}
                                >
                                  Aviso de Privacidad
                                </Card.Link>
                              </Form.Label>
                            </div>

                            {/* 👇 Ahora el mensaje se muestra DEBAJO */}
                            {touched.terms && errors.terms && (
                              <div className="text-danger mt-1 small">
                                {errors.terms}
                              </div>
                            )}
                          </div>
                        </Form.Group>

                        {/* Botón */}
                        <Button
                          variant="primary"
                          type="submit"
                          className="w-100 fw-bold"
                        >
                          Registrarse
                        </Button>

                        <p className="register-text mt-3 text-center">
                          ¿Ya tienes una cuenta?{" "}
                          <Card.Link href="#">Inicia sesión</Card.Link>
                        </p>
                      </Form>
                    )}
                  </Formik>
                </Card.Body>
              </Card>

              <footer className="text-center mt-4 text-light small">
                © 2025 HealthConnect. Todos los derechos reservados.{" "}
                <a href="#">Política de Privacidad</a>
              </footer>
            </Col>
          </Row>
        </Container>
      </div>

      {/* Modal Términos */}
      <Modal show={showTerms} onHide={() => setShowTerms(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>Términos y Condiciones</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>
            Aquí van los términos y condiciones de uso del portal HealthConnect.
            Al continuar, aceptas cumplir con nuestras políticas y lineamientos
            de servicio.
          </p>
        </Modal.Body>
      </Modal>

      {/* Modal Privacidad */}
      <Modal show={showPrivacy} onHide={() => setShowPrivacy(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>Aviso de Privacidad</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>
            HealthConnect recopila y protege tus datos de acuerdo con la ley de
            protección de datos personales. Tu información se usará solo para
            fines médicos y de comunicación interna.
          </p>
        </Modal.Body>
      </Modal>

      {/* Toast de éxito */}
      <Toast
        show={showToast}
        onClose={() => setShowToast(false)}
        delay={3000}
        autohide
        className="position-fixed top-0 end-0 m-3 bg-success text-white"
      >
        <Toast.Header closeButton={false}>
          <strong className="me-auto">Registro exitoso</strong>
        </Toast.Header>
        <Toast.Body>¡Tu cuenta ha sido creada correctamente!</Toast.Body>
      </Toast>
    </div>
  );
}

export default SignupPage;
