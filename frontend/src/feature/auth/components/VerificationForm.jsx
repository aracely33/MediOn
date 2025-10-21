import React, { useRef } from "react";
import { Formik, Form } from "formik";
import * as Yup from "yup";
import { Button, Card, Alert } from "react-bootstrap";

const VerificationSchema = Yup.object().shape({
  code: Yup.array()
    .of(
      Yup.string()
        .matches(/^[0-9]$/, "Must be a number")
        .required()
    )
    .length(6, "Enter all 6 digits"),
});

const VerificationForm = ({ onVerify, onResend }) => {
  const inputsRef = useRef([]);

  const handleChange = (e, index, values, setFieldValue) => {
    const val = e.target.value.replace(/\D/g, ""); // solo dígitos

    const newCode = [...values.code];
    newCode[index] = val;
    setFieldValue("code", newCode);

    if (val && index < 5) {
      inputsRef.current[index + 1].focus();
    }
  };

  const handleKeyDown = (e, index) => {
    if (e.key === "Backspace" && !e.target.value && index > 0) {
      inputsRef.current[index - 1].focus();
    }
  };

  return (
    <main className="d-flex justify-content-center align-items-center py-5 px-3">
      <Card
        className="p-4 shadow-lg"
        style={{ maxWidth: "400px", width: "100%" }}
      >
        <div className="text-center mb-4">
          <h2 className="fw-bold">Enter Verification Code</h2>
          <p className="text-muted">
            A verification code has been sent to your email address. Please
            enter it below.
          </p>
        </div>

        <Formik
          initialValues={{ code: ["", "", "", "", "", ""] }}
          validationSchema={VerificationSchema}
          onSubmit={(values, { setSubmitting }) => {
            const finalCode = values.code.join("");
            onVerify(finalCode);
            setSubmitting(false);
          }}
        >
          {({ values, setFieldValue, errors, touched, isSubmitting }) => (
            <Form>
              <div className="d-flex justify-content-center gap-2 mb-3">
                {values.code.map((digit, i) => (
                  <input
                    key={i}
                    type="text"
                    inputMode="numeric"
                    maxLength="1"
                    className="form-control text-center fs-4 fw-bold"
                    style={{ width: "48px", height: "60px" }}
                    value={digit}
                    onChange={(e) => handleChange(e, i, values, setFieldValue)}
                    onKeyDown={(e) => handleKeyDown(e, i)}
                    ref={(el) => (inputsRef.current[i] = el)}
                  />
                ))}
              </div>

              {errors.code && touched.code && (
                <Alert variant="danger" className="text-center py-1">
                  {typeof errors.code === "string"
                    ? errors.code
                    : "Please enter valid numbers"}
                </Alert>
              )}

              <div className="text-center mb-3">
                <p className="text-muted">
                  Didn’t receive the code?{" "}
                  <button
                    type="button"
                    className="btn btn-link fw-semibold text-primary p-0"
                    onClick={onResend}
                  >
                    Resend Code
                  </button>
                </p>
              </div>

              <Button
                type="submit"
                className="w-100"
                variant="primary"
                disabled={isSubmitting}
              >
                {isSubmitting ? "Verifying..." : "Verify"}
              </Button>
            </Form>
          )}
        </Formik>
      </Card>
    </main>
  );
};

export default VerificationForm;
