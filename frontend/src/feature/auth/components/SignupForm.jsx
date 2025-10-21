import { useState } from "react";
import { Button, Form, Card, InputGroup } from "react-bootstrap";
import { Formik } from "formik";
import { Eye, EyeSlash, CheckCircleFill } from "react-bootstrap-icons";
import { signupValidation } from "../utils/validationSchema";
import PasswordRequirements from "./PasswordRequirements";
import { registerPatient } from "../services/authService";
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
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        confirmPassword: "",
        terms: false,
      }}
      validationSchema={signupValidation}
      validateOnMount={true} // activa isValid desde el inicio
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
          console.log(patient);

          console.log("Registro exitoso:", patient);
          //onSuccess();
          resetForm();
          //llamada a la api para  verificar código en email
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
        handleBlur,
        values,
        errors,
        touched,
        isSubmitting,
        isValid,
        setFieldTouched,
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
                onChange={(e) => {
                  handleChange(e);
                  // Marca el campo como tocado apenas escriba algo
                  if (!touched.firstName) setFieldTouched("firstName", true);
                }}
                onBlur={handleBlur}
                isInvalid={touched.firstName && !!errors.firstName}
              />
              {touched.firstName && errors.firstName && (
                <div className="text-danger mt-1 small">{errors.firstName}</div>
              )}
            </Form.Group>
            {/* Apellidos */}
            <Form.Group className="mb-3">
              <Form.Label className="fw-bold">Apellidos</Form.Label>
              <Form.Control
                type="text"
                name="lastName"
                placeholder="Tus apellidos"
                value={values.lastName}
                onChange={(e) => {
                  handleChange(e);
                  // Marca el campo como tocado apenas escriba algo
                  if (!touched.lastName) setFieldTouched("lastName", true);
                }}
                onBlur={handleBlur}
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
                onChange={(e) => {
                  handleChange(e);
                  // Marca el campo como tocado apenas escriba algo
                  if (!touched.email) setFieldTouched("email", true);
                }}
                onBlur={handleBlur}
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
                  placeholder="Contraseñ@3"
                  value={values.password}
                  onChange={handleChange}
                  onBlur={handleBlur}
                  isInvalid={!!errors.password && touched.password}
                />
              </InputGroup>
              <div className="mt-1">
                <Card.Link
                  href="#"
                  onClick={(e) => {
                    e.preventDefault();
                    setShowPassword(!showPassword);
                  }}
                  className="text-decoration-none text-secondary"
                >
                  {showPassword ? "Ocultar contraseña" : "Mostrar contraseña"}
                </Card.Link>
              </div>
              <PasswordRequirements
                password={values.password}
                touched={touched.password}
              />
            </Form.Group>

            {/* Confirmar contraseña */}
            <Form.Group className="mb-3 position-relative">
              <Form.Label className="fw-bold">Confirmar contraseña</Form.Label>
              <Form.Control
                type={showConfirm ? "text" : "password"}
                name="confirmPassword"
                placeholder="Repite tu contraseña"
                value={values.confirmPassword}
                onChange={handleChange}
                onBlur={handleBlur}
                isInvalid={
                  touched.confirmPassword &&
                  (!!errors.confirmPassword ||
                    (!passwordsMatch && values.confirmPassword !== ""))
                }
              />
              {passwordsMatch && values.confirmPassword && (
                <CheckCircleFill
                  className="text-success position-absolute"
                  size={20}
                  style={{
                    right: "10px",
                    top: "50%",
                    transform: "translateY(-50%)",
                  }}
                />
              )}
              <Form.Control.Feedback type="invalid">
                {errors.confirmPassword}
              </Form.Control.Feedback>

              <div className="mt-1 d-flex justify-content-between align-items-center">
                <Card.Link
                  href="#"
                  onClick={(e) => {
                    e.preventDefault();
                    setShowConfirm(!showConfirm);
                  }}
                  className="text-decoration-none text-secondary"
                >
                  {showConfirm ? "Ocultar contraseña " : "Mostrar contraseña"}
                </Card.Link>
              </div>
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
                  onBlur={() => setFieldTouched("terms", true)}
                  isInvalid={touched.terms && !!errors.terms}
                />

                <Form.Label htmlFor="terms" className=" m-2 mb-0">
                  Acepto los{" "}
                  <Card.Link href="#" onClick={onShowTerms}>
                    Términos y Condiciones
                  </Card.Link>{" "}
                  y el
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
              disabled={!isValid || isSubmitting}
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
