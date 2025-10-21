import { useState } from "react";
import { Calendar, momentLocalizer } from "react-big-calendar";
import moment from "moment";
import "react-big-calendar/lib/css/react-big-calendar.css";
import "./calendarView.css";

const localizer = momentLocalizer(moment);

const CalendarView = ({ appointments = [], onSelectEvent }) => {
  const [view, setView] = useState("week");

  const events = Array.isArray(appointments)
    ? appointments.map((appt) => {
        const dateObj = new Date(appt.dateTime);

        return {
          title: appt.motive,
          start: dateObj,
          end: new Date(dateObj.getTime() + 60 * 60 * 1000), // 1 hora después
          allDay: false,
          resource: appt,
        };
      })
    : [];

  return (
    <div className="calendar-container">
      <div className="calendar-header">
        <h5>Programación de consultas</h5>
      </div>

      <Calendar
        localizer={localizer}
        events={events}
        startAccessor="start"
        endAccessor="end"
        style={{ height: 500 }}
        view={view}
        onView={(newView) => setView(newView)}
        onSelectEvent={(event) => onSelectEvent(event.resource)}
        views={["day", "week", "month"]}
        messages={{
          day: "Día",
          week: "Semana",
          month: "Mes",
          today: "Hoy",
          previous: "<",
          next: ">",
        }}
      />
    </div>
  );
};

export default CalendarView;
