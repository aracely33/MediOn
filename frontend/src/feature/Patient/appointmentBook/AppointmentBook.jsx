import { useState } from "react";
import "./AppointmentBook.css";
import Header from "../../../components/header/Header";
import { Container } from "react-bootstrap";
import { ProgressSteps } from "./components/ProgressSteps";
import { StepTypeAppointment } from "./components/StepTypeAppointment";
import { useSteps } from "../../../utils/useSteps";
// import { StepSpeciality } from "./components/StepSpeciality";
import { StepDoctor } from "./components/StepDoctor";
import { StepDateAndTime } from "./components/StepDateAndTime";
// import { StepGenerateLink } from "./components/StepGenerateLink";
import { StepConfirm } from "./components/StepConfirm";
import { StepReasonNote } from "./components/StepReasonNote";
import { useFormik } from "formik";
import { appointmentValidation } from "../../../utils/validationSchemas";
import { convertTo24Hour } from "../../../utils/formatters";
import { usePatient } from "../../../context/PatientContext";
import { useAppointment } from "../../../context/AppointmentContext";

function AppointmentBook() {
  const { patient } = usePatient();
  const { addAppointment } = useAppointment();
  const [selectedMedico, setSelectedMedico] = useState(null);

  const formik = useFormik({
    initialValues: {
      type: "",
      doctorId: "",
      patientId: patient?.id || "",
      appointmentDate: null,
      appointmentTime: null,
      reason: "",
      notes: "",
    },
    validationSchema: appointmentValidation,
    onSubmit: async (values) => {
      console.log("onSubmit invoked con values:", values);
      try {
        const dateFormatted = values.appointmentDate
          .toISOString()
          .split("T")[0];
        console.log("Fecha formateada:", dateFormatted);
        const timeFormatted = convertTo24Hour(values.appointmentTime);
        console.log("Hora formateada:", timeFormatted);
        const appointment = {
          type: values.type,
          doctorId: values.doctorId,
          patientId: values.patientId,
          appointmentDate: dateFormatted,
          appointmentTime: timeFormatted,
          reason: values.reason,
          notes: values.notes || "",
        };
        await addAppointment(appointment);
        console.log("Cita confirmada", appointment);
        console.log("Valores del formulario", values);
      } catch (error) {
        console.error("Error al crear la cita:", error);
      }
    },
  });

  const { currentStep, next, prev } = useSteps(
    1,
    formik.values.type === "VIRTUAL" ? 6 : 7
  );

  return (
    <div className="app bg-light min-vh-100 d-flex flex-column">
      <Header />
      <Container className="flex-grow-1 py-4">
        <ProgressSteps currentStep={currentStep} type={formik.values.type} />

        {currentStep === 1 && (
          <StepTypeAppointment
            formik={formik}
            currentStep={currentStep}
            onNext={next}
            prev={prev}
          />
        )}
        {/* {currentStep === 2 && (
          <StepSpeciality currentStep={currentStep} onNext={next} prev={prev} />
        )} */}
        {currentStep === 2 && (
          <StepDoctor
            formik={formik}
            currentStep={currentStep}
            onNext={next}
            prev={prev}
            setDoctor={setSelectedMedico}
          />
        )}
        {currentStep === 3 && (
          <StepDateAndTime
            formik={formik}
            currentStep={currentStep}
            onNext={next}
            prev={prev}
          />
        )}
        {currentStep === 4 && (
          <StepReasonNote
            formik={formik}
            currentStep={currentStep}
            onNext={next}
            prev={prev}
          />
        )}
        {currentStep === 5 && formik.values.type === "PRESENCIAL" ? (
          <StepConfirm
            formik={formik}
            currentStep={currentStep}
            prev={prev}
            doctor={selectedMedico}
          />
        ) : currentStep === 5 && formik.values.type === "VIRTUAL" ? (
          <StepConfirm
            formik={formik}
            currentStep={currentStep}
            prev={prev}
            doctor={selectedMedico}
          />
        ) : null}
      </Container>
    </div>
  );
}

export default AppointmentBook;
