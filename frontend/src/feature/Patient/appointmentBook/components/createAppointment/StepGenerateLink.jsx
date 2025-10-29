import { useState } from "react";
import { Card, Button, Alert } from "react-bootstrap";

export const StepGenerateLink = ({
  onNext,
  setLinkVideo,
  currentStep,
  prev,
}) => {
  const [link, setLink] = useState(null);

  const generateLink = () => {
    const randomId = Math.random().toString(36).substring(2, 10);
    const fakeLink = `https://videocall.clinicflow.app/${randomId}`;
    setLink(fakeLink);
    setLinkVideo(fakeLink);
  };

  return (
    <Card className="p-4 shadow-sm animate-bounce-in">
      <h4 className="mb-3 text-primary fw-bold">
        Paso 5 de 6: Generar enlace de videollamada
      </h4>

      {!link ? (
        <div className="text-center">
          <p className="text-dark mb-4">
            Presiona el botón para generar el enlace de la videollamada.
          </p>
          <Button variant="success" onClick={generateLink}>
            Generar enlace
          </Button>
        </div>
      ) : (
        <Alert variant="success" className="text-center">
          Enlace generado:
          <div className="fw-bold mt-2">
            <a href={link} target="_blank" rel="noreferrer">
              {link}
            </a>
          </div>
        </Alert>
      )}

      <div className="d-flex justify-content-between mt-4">
        <div className="text-start mt-3">
          <Button
            variant="outline-secondary"
            disabled={currentStep === 1}
            onClick={prev}
          >
            Paso Anterior
          </Button>
        </div>

        <div className="text-end mt-3">
          <Button variant="success" disabled={!link} onClick={onNext}>
            Continuar
          </Button>
        </div>
      </div>
    </Card>
  );
};
