import {
  GridFill,
  Calendar2Week,
  FileMedical,
  Bell,
  Gear,
  QuestionCircle,
} from "react-bootstrap-icons";
import "./Sidebar.css";

export const iconMap = {
  dashboard: GridFill,
  calendar_month: Calendar2Week,
  medical_information: FileMedical,
  notifications: Bell,
  settings: Gear,
  help_outline: QuestionCircle,
};

// Links dinámicos según rol
export const sidebarLinks = {
  doctor: [
    {
      label: "Panel de Control",
      href: "#",
      icon: "dashboard",
      active: true,
    },
    { label: "Pacientes", href: "#", icon: "medical_information" },
    { label: "Citas", href: "#", icon: "calendar_month" },
    {
      label: "Informes",
      href: "#",
      icon: "notifications",
    },
    { label: "Configuración", href: "#", icon: "settings" },
  ],
  patient: [
    {
      label: "Panel de Control",
      href: "#",
      icon: "dashboard",
      active: true,
    },
    {
      label: "Agendar Citas",
      href: "#",
      icon: "calendar_month",
    },
    { label: "Historial Médico", href: "#", icon: "medical_information" },
    { label: "Mensajes y Alertas", href: "#", icon: "notifications" },
    { label: "Configuración", href: "#", icon: "settings" },
  ],
};
