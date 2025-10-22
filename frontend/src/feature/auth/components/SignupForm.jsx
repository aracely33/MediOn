import { useState } from "react";
import { Button, Form, Card, InputGroup } from "react-bootstrap";
import { Formik } from "formik";
import { Eye, EyeSlash, CheckCircleFill } from "react-bootstrap-icons";
import { signupValidation } from "../utils/validationSchema";
import PasswordRequirements from "./PasswordRequirements";
import { useNavigate } from "react-router-dom";
import { usePatient } from "../../../context/PatientContext"; //cual usePatient?
import "./SignupForm.css";

const SignupForm = ({ onSuccess, onShowTerms, onShowPrivacy }) => {
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const navigate = useNavigate();
  const { signUp } = usePatient();

  return (
    <Formik
      initialValues={{
        name: "",
        email: "",
        password: "",
        confirmPassword: "",
        terms: false,
      }}
      validationSchema={signupValidation}
      onSubmit={async (values, { resetForm, setSubmitting, setErrors }) => {
        if (values.password !== values.confirmPassword) {
          setErrors({ confirmPassword: "Las contraseñas no coinciden" });
          setSubmitting(false);
          return;
        }

        try {
          console.log("Intentando registro...", values);
          const patient = await signUp({
            name: values.firstName,
            lastName: values.lastName,
            email: values.email,
            password: values.password,
            confirmPassword: values.password,
          });

          console.log("Registro exitoso:", patient);
          //onSuccess();
          resetForm();
          navigate("/confirm-email");
        } catch (error) {
          console.error("Error al registrar:", error.response?.data);
          setErrors({
            email:
              error.response?.data?.message ||
              "Hubo un problema al registrarte",
          });
        } finally {
          setSubmitting(false);
        }
      }}
    >
      {({
        handleSubmit,
        handleChange,
        values,
        errors,
        touched,
        isSubmitting,
      }) => {
        const passwordsMatch =
          values.password && values.password === values.confirmPassword;

        return (
          <Form noValidate onSubmit={handleSubmit}>
            {/* Nombre */}
            <Form.Group className="mb-3">
              <Form.Label className="fw-bold">Nombre(s)</Form.Label>
              <Form.Control
                type="text"
                name="firstName"
                placeholder="Tu nombre"
                value={values.firstName}
                onChange={handleChange}
                isInvalid={touched.firstName && !!errors.firstName}
              />
              <Form.Control.Feedback type="invalid">
                {errors.firstName}
              </Form.Control.Feedback>
            </Form.Group>
            {/* Apellidos */}
            <Form.Group className="mb-3">
              <Form.Label className="fw-bold">Apellidos</Form.Label>
              <Form.Control
                type="text"
                name="lastName"
                placeholder="Tus apellidos"
                value={values.lastName}
                onChange={handleChange}
                isInvalid={touched.lastName && !!errors.lastName}
              />
              <Form.Control.Feedback type="invalid">
                {errors.lastName}
              </Form.Control.Feedback>
            </Form.Group>

            {/* Email */}
            <Form.Group className="mb-3">
              <Form.Label className="fw-bold">Correo electrónico</Form.Label>
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
            <Form.Group className="mb-3 position-relative">
              <Form.Label className="fw-bold">Contraseña</Form.Label>
              <InputGroup>
                <Form.Control
                  type={showPassword ? "text" : "password"}
                  name="password"
                  placeholder="Crea una contraseña segura"
                  value={values.password}
                  onChange={handleChange}
                  isInvalid={touched.password && !!errors.password}
                />
                <Button
                  variant="link"
                  type="button"
                  className="text-muted"
                  onClick={() => setShowPassword(!showPassword)}
                >
                  {showPassword ? <EyeSlash /> : <Eye />}
                </Button>
              </InputGroup>
              <PasswordRequirements password={values.password} />
              {touched.password && errors.password && (
                <div className="text-danger mt-1 small">{errors.password}</div>
              )}
            </Form.Group>

            {/* Confirmar contraseña */}
            <Form.Group className="mb-3 position-relative">
              <Form.Label className="fw-bold">Confirmar contraseña</Form.Label>
              <InputGroup>
                <Form.Control
                  type={showConfirm ? "text" : "password"}
                  name="confirmPassword"
                  placeholder="Repite tu contraseña"
                  value={values.confirmPassword}
                  onChange={handleChange}
                  isInvalid={
                    touched.confirmPassword &&
                    (!!errors.confirmPassword || !passwordsMatch)
                  }
                />
                <Button
                  variant="link"
                  type="button"
                  className="text-muted"
                  onClick={() => setShowConfirm(!showConfirm)}
                >
                  {showConfirm ? <EyeSlash /> : <Eye />}
                </Button>
                {passwordsMatch && (
                  <CheckCircleFill
                    className="text-success position-absolute end-0 me-5 top-50 translate-middle-y"
                    size={20}
                  />
                )}
              </InputGroup>

              {touched.confirmPassword && errors.confirmPassword && (
                <div className="text-danger mt-1 small">
                  {errors.confirmPassword}
                </div>
              )}
            </Form.Group>

            {/* Términos */}
            <Form.Group className="mb-3">
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
                  <Card.Link href="#" onClick={onShowTerms}>
                    Términos y Condiciones
                  </Card.Link>{" "}
                  y el{" "}
                  <Card.Link href="#" onClick={onShowPrivacy}>
                    Aviso de Privacidad
                  </Card.Link>
                </Form.Label>
              </div>
              {touched.terms && errors.terms && (
                <div className="text-danger mt-1 small">{errors.terms}</div>
              )}
            </Form.Group>

            {/* Botón */}
            <Button
              variant="primary"
              type="submit"
              className="signup-btn w-100 fw-bold"
              disabled={isSubmitting}
            >
              Registrarse
            </Button>

            <p className="register-text mt-3 text-center">
              ¿Ya tienes una cuenta?{" "}
              <Card.Link href="/login">Inicia sesión</Card.Link>
            </p>
          </Form>
        );
      }}
    </Formik>
  );
};

export default SignupForm;
