import { useState } from "react";
import "./AppointmentBook.css";
import Header from "../../../components/header/Header";
import { Container } from "react-bootstrap";
import { ProgressSteps } from "./components/ProgressSteps";
import { StepTypeAppointment } from "./components/StepTypeAppointment";
import { useSteps } from "../../../utils/useSteps";
import { StepSpeciality } from "./components/StepSpeciality";
import { StepDoctor } from "./components/StepDoctor";
import { StepDateAndTime } from "./components/StepDateAndTime";
import { StepGenerateLink } from "./components/StepGenerateLink";
import { StepConfirm } from "./components/StepConfirm";
import { StepReasonNote } from "./components/StepReasonNote";
// import { useFormik } from "formik";
// import { appointmentValidation } from "../../../utils/validationSchemas";

function AppointmentBook() {
  const [typeAppointment, setTypeAppointment] = useState(null);
  const [selectedMedico, setSelectedMedico] = useState(null);
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [selectedTime, setSelectedTime] = useState(null);
  const [reason, setReason] = useState();
  const [notes, setNotes] = useState();
  const [linkVideo, setLinkVideo] = useState(null);
  const { currentStep, next, prev } = useSteps(
    1,
    typeAppointment === "virtual" ? 6 : 7
  );

  // const formik = useFormik({
  //   initialValues: {
  //     typeAppointment: "",
  //     speciality: "",
  //     doctor: "",
  //     date: null,
  //     time: null,
  //     linkVideo: null,
  //   },
  //   validationSchema: appointmentValidation,
  //   onSubmit: (values) => {
  //     console.log(values);
  //   },
  // });

  return (
    <div className="app bg-light min-vh-100 d-flex flex-column">
      <Header />
      <Container className="flex-grow-1 py-4">
        <ProgressSteps
          currentStep={currentStep}
          typeAppointment={typeAppointment}
        />

        {currentStep === 1 && (
          <StepTypeAppointment
            currentStep={currentStep}
            onNext={next}
            prev={prev}
            setTypeAppointment={setTypeAppointment}
          />
        )}
        {currentStep === 2 && (
          <StepSpeciality currentStep={currentStep} onNext={next} prev={prev} />
        )}
        {currentStep === 3 && (
          <StepDoctor
            currentStep={currentStep}
            onNext={next}
            prev={prev}
            professional={selectedMedico}
            setProfessional={setSelectedMedico}
          />
        )}
        {currentStep === 4 && (
          <StepDateAndTime
            currentStep={currentStep}
            onNext={next}
            prev={prev}
            chooseDate={selectedDate}
            setChooseDate={setSelectedDate}
            chooseTime={selectedTime}
            setChooseTime={setSelectedTime}
          />
        )}
        {currentStep === 5 && (
          <StepReasonNote
            currentStep={currentStep}
            onNext={next}
            prev={prev}
            reason={reason}
            setReason={setReason}
            notes={notes}
            setNotes={setNotes}
          />
        )}
        {currentStep === 6 && typeAppointment === "virtual" ? (
          <StepGenerateLink
            onNext={next}
            setLinkVideo={setLinkVideo}
            currentStep={currentStep}
            prev={prev}
          />
        ) : currentStep === 6 && typeAppointment === "presencial" ? (
          <StepConfirm
            typeAppointment={typeAppointment}
            doctor={selectedMedico}
            date={selectedDate}
            time={selectedTime}
            reason={reason}
            note={notes}
            linkVideo={linkVideo}
            currentStep={currentStep}
            prev={prev}
          />
        ) : (
          currentStep === 7 && (
            <StepConfirm
              typeAppointment={typeAppointment}
              doctor={selectedMedico}
              date={selectedDate}
              time={selectedTime}
              reason={reason}
              note={notes}
              linkVideo={linkVideo}
              currentStep={currentStep}
              prev={prev}
            />
          )
        )}
      </Container>
    </div>
  );
}

export default AppointmentBook;
