// src/features/auth/components/SignupForm.jsx
import { useState } from "react";
import { Button, Form, Card } from "react-bootstrap";
import { Formik } from "formik";
import { Eye, EyeSlash } from "react-bootstrap-icons";
import { signupValidation } from "../utils/validationSchema";
import PasswordRequirements from "./PasswordRequirements";

const SignupForm = ({ onSuccess, onShowTerms, onShowPrivacy }) => {
  const [showPassword, setShowPassword] = useState(false);

  return (
    <Formik
      initialValues={{ name: "", email: "", password: "", terms: false }}
      validationSchema={signupValidation}
      onSubmit={(values, { resetForm }) => {
        console.log("Datos enviados:", values);
        onSuccess();
        resetForm();
      }}
    >
      {({ handleSubmit, handleChange, values, errors, touched }) => (
        <Form noValidate onSubmit={handleSubmit}>
          {/* Nombre */}
          <Form.Group className="mb-3">
            <Form.Label className="fw-bold">Nombre completo</Form.Label>
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
          <Form.Group className="mb-3">
            <Form.Label className="fw-bold">Contraseña</Form.Label>
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
            <PasswordRequirements password={values.password} />
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
          <Button variant="primary" type="submit" className="w-100 fw-bold">
            Registrarse
          </Button>

          <p className="register-text mt-3 text-center">
            ¿Ya tienes una cuenta? <Card.Link href="#">Inicia sesión</Card.Link>
          </p>
        </Form>
      )}
    </Formik>
  );
};

export default SignupForm;
