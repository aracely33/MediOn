import "./AppointmentDetails.css";

function AppointmentDetails({
  name,
  age,
  gender,
  date,
  time,
  motive,
  assignedTo,
  isTeleconsultation,
  role, // 'doctor' o 'patient'
}) {
  return (
    <div className="appointment-card">
      <h3 className="appointment-title">Detalles de la Cita</h3>

      <div className="appointment-user">
        <img
          src="/assets/avatar-placeholder.png"
          alt="Paciente"
          className="appointment-avatar"
        />
        <div>
          <p className="appointment-name">{name}</p>
          <p className="appointment-info">
            {age} años, {gender}
          </p>
        </div>
      </div>

      <p className="appointment-item">
        <strong>Fecha y Hora:</strong> {date}, {time}
      </p>

      <p className="appointment-item">
        <strong>Motivo:</strong> {motive}{" "}
        {isTeleconsultation && <span>(Teleconsulta)</span>}
      </p>

      <p className="appointment-item">
        <strong>Asignado a:</strong> {assignedTo}
      </p>

      <div className="appointment-actions">
        <button
          variant="primary"
          disabled={!isTeleconsultation}
          onClick={() => {
            // Aquí iría la lógica para iniciar la videollamada
            alert("Iniciando teleconsulta...");
          }}
        >
          Iniciar Teleconsulta
        </button>
        <button className="btn-secondary">Ver Historial Médico</button>
      </div>

      {role === "doctor" && (
        <div className="appointment-notes">
          <label>Notas del personal:</label>
          <textarea
            placeholder="Añadir notas para otros miembros del personal..."
            rows="3"
          ></textarea>
          <button className="btn-save">Guardar Nota</button>

          <div className="appointment-footer-buttons">
            <button className="btn-link">Reprogramar</button>
            <button className="btn-link">Completar</button>
          </div>
        </div>
      )}
    </div>
  );
}

export default AppointmentDetails;
