import { useEffect, useState } from "react";
import "./AppointmentBook.css";
import Header from "../../../components/header/Header";
import { Container } from "react-bootstrap";
import { ProgressSteps } from "./components/createAppointment/ProgressSteps";
import { StepTypeAppointment } from "./components/createAppointment/StepTypeAppointment";
import { useSteps } from "../../../utils/useSteps";
// import { StepSpeciality } from "./components/StepSpeciality";
import { StepDoctor } from "./components/createAppointment/StepDoctor";
import { StepDateAndTime } from "./components/createAppointment/StepDateAndTime";
// import { StepGenerateLink } from "./components/StepGenerateLink";
import { StepConfirm } from "./components/createAppointment/StepConfirm";
import { StepReasonNote } from "./components/createAppointment/StepReasonNote";
import { useFormik } from "formik";
import { appointmentValidation } from "../../../utils/validationSchemas";
import { convertTo24Hour } from "../../../utils/formatters";
import { usePatient } from "../../../context/PatientContext";
import { useAppointment } from "../../../context/AppointmentContext";
import Sidebar from "../../../components/sidebar/Sidebar";
import { getMe } from "../patientService";

function CreateAppointmentBook() {
  const { patient } = usePatient();
  const [user, setUser] = useState(null);
  const [showSidebar, setShowSidebar] = useState(false);
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
    onSubmit: async (values, { setSubmitting }) => {
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
        setSubmitting(false);
        throw error;
      }
    },
  });

  const { currentStep, next, prev } = useSteps(
    1,
    formik.values.type === "VIRTUAL" ? 6 : 7
  );

  const handleLogout = async () => {
    await signOut();
    navigate("/login");
  };

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const data = await getMe();
        console.log("Datos del usuario obtenido:", data);
        setUser(data);
      } catch (error) {
        console.error("Error al obtener usuario:", error);
      }
    };
    fetchUser();
  }, []);

  return (
    <div className="d-flex patient-dashboard">
      <Sidebar
        user={{
          id: patient.id,
          name: patient.name,
          lastName: patient.lastName,
          avatar: "https://cdn-icons-png.flaticon.com/512/149/149071.png",
        }}
        role="patient"
        show={showSidebar}
        onHide={() => setShowSidebar(false)}
      />

      <Container className="flex-grow-1">
        <Header
          className="mb-4"
          title="MedTech: Tu portal de salud"
          avatarUrl={patient.avatar}
          buttons={[
            {
              label: "Cerrar sesión",
              onClick: handleLogout,
              className: "header-btn",
            },
          ]}
        />
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

export default CreateAppointmentBook;
