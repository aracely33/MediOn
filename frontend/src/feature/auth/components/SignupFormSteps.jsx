import { useState } from "react";
import { Button, Form, ProgressBar, Card, InputGroup } from "react-bootstrap";
import { Formik } from "formik";
import * as Yup from "yup";
import { Eye, EyeSlash, CheckCircleFill } from "react-bootstrap-icons";
import PasswordRequirements from "./PasswordRequirements";

const SignupFormSteps = () => {
  const [step, setStep] = useState(1);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);

  // Validación con Yup
  const schema = Yup.object({
    firstName: Yup.string().required("Required"),
    lastName: Yup.string().required("Required"),
    email: Yup.string().email("Invalid email").required("Required"),
    password: Yup.string()
      .min(8, "Min 8 characters")
      .matches(/[A-Z]/, "One uppercase letter")
      .matches(/[0-9]/, "One number")
      .matches(/[!@#$%^&*]/, "One special character")
      .required("Required"),
    confirmPassword: Yup.string()
      .oneOf([Yup.ref("password"), null], "Passwords must match")
      .required("Required"),
  });

  const nextStep = () => setStep((s) => s + 1);
  const prevStep = () => setStep((s) => s - 1);

  return (
    <Card className="p-4 shadow-lg rounded-4">
      <ProgressBar now={(step / 2) * 100} className="mb-4" />

      <Formik
        validationSchema={schema}
        initialValues={{
          firstName: "",
          lastName: "",
          email: "",
          password: "",
          confirmPassword: "",
        }}
        onSubmit={(values) => console.log("Submitted:", values)}
      >
        {({ handleSubmit, handleChange, values, errors, touched }) => (
          <Form noValidate onSubmit={handleSubmit}>
            {/* STEP 1: Personal info */}
            {step === 1 && (
              <>
                <Form.Group className="mb-3">
                  <Form.Label>First Name</Form.Label>
                  <Form.Control
                    name="firstName"
                    value={values.firstName}
                    onChange={handleChange}
                    isInvalid={touched.firstName && !!errors.firstName}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.firstName}
                  </Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Last Name</Form.Label>
                  <Form.Control
                    name="lastName"
                    value={values.lastName}
                    onChange={handleChange}
                    isInvalid={touched.lastName && !!errors.lastName}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.lastName}
                  </Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Email</Form.Label>
                  <Form.Control
                    type="email"
                    name="email"
                    value={values.email}
                    onChange={handleChange}
                    isInvalid={touched.email && !!errors.email}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.email}
                  </Form.Control.Feedback>
                </Form.Group>
              </>
            )}

            {/* STEP 2: Password */}
            {step === 2 && (
              <>
                <Form.Group className="mb-3 position-relative">
                  <Form.Label>Password</Form.Label>
                  <InputGroup>
                    <Form.Control
                      type={showPassword ? "text" : "password"}
                      name="password"
                      placeholder="Create a password"
                      value={values.password}
                      onChange={handleChange}
                      isInvalid={touched.password && !!errors.password}
                    />
                    <Button
                      variant="link"
                      onClick={() => setShowPassword(!showPassword)}
                    >
                      {showPassword ? <EyeSlash /> : <Eye />}
                    </Button>
                  </InputGroup>
                  <PasswordRequirements password={values.password} />
                </Form.Group>

                <Form.Group className="mb-3 position-relative">
                  <Form.Label>Confirm Password</Form.Label>
                  <InputGroup>
                    <Form.Control
                      type={showConfirm ? "text" : "password"}
                      name="confirmPassword"
                      placeholder="Repeat password"
                      value={values.confirmPassword}
                      onChange={handleChange}
                      isInvalid={
                        touched.confirmPassword && !!errors.confirmPassword
                      }
                    />
                    <Button
                      variant="link"
                      onClick={() => setShowConfirm(!showConfirm)}
                    >
                      {showConfirm ? <EyeSlash /> : <Eye />}
                    </Button>
                    {values.password === values.confirmPassword &&
                      values.password && (
                        <CheckCircleFill
                          className="text-success position-absolute end-0 me-5 top-50 translate-middle-y"
                          size={18}
                        />
                      )}
                  </InputGroup>
                  <Form.Control.Feedback type="invalid">
                    {errors.confirmPassword}
                  </Form.Control.Feedback>
                </Form.Group>
              </>
            )}

            {/* Buttons */}
            <div className="d-flex justify-content-between mt-4">
              {step > 1 && (
                <Button variant="outline-secondary" onClick={prevStep}>
                  Previous
                </Button>
              )}
              {step < 2 ? (
                <Button variant="primary" onClick={nextStep}>
                  Next
                </Button>
              ) : (
                <Button variant="success" type="submit">
                  Sign Up
                </Button>
              )}
            </div>
          </Form>
        )}
      </Formik>
    </Card>
  );
};

export default SignupFormSteps;
