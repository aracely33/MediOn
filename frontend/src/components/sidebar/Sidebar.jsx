import { Nav } from "react-bootstrap";
import {
  GridFill,
  Calendar2Week,
  FileMedical,
  Bell,
  Gear,
  QuestionCircle,
} from "react-bootstrap-icons";
import "./Sidebar.css";

const iconMap = {
  dashboard: GridFill,
  calendar_month: Calendar2Week,
  medical_information: FileMedical,
  notifications: Bell,
  settings: Gear,
  help_outline: QuestionCircle,
};

const Sidebar = ({ user }) => {
  const links = [
    { label: "Panel de Control", href: "#", icon: "dashboard", active: true },
    { label: "Agendar Citas", href: "#", icon: "calendar_month" },
    { label: "Historial Médico", href: "#", icon: "medical_information" },
    { label: "Mensajes y Alertas", href: "#", icon: "notifications" },
    { label: "Configuración", href: "#", icon: "settings" },
  ];

  return (
    <div className="sidebar-container d-flex flex-column justify-content-between p-3">
      {/* Parte superior: avatar y nombre */}
      <div>
        <div className="sidebar-user d-flex align-items-center gap-2 mb-4">
          <img
            src={user.avatar}
            alt="Avatar"
            className="user-avatar"
            width={40}
            height={40}
          />
          <div>
            <h6 className="user-name mb-0">{user.name}</h6>
            <small className="user-id text-muted">ID: {user.id}</small>
          </div>
        </div>

        {/* Links principales */}
        <Nav className="sidebar-links flex-column gap-1">
          {links.map((link) => {
            const Icon = iconMap[link.icon];
            return (
              <Nav.Link
                key={link.label}
                href={link.href}
                className={`sidebar-link ${
                  link.active ? "active-link" : ""
                } d-flex align-items-center`}
              >
                {Icon && <Icon className="me-2 icon" />}
                {link.label}
              </Nav.Link>
            );
          })}
        </Nav>
      </div>

      {/* Parte inferior: ayuda */}
      <Nav className="sidebar-help flex-column gap-2">
        <Nav.Link href="#" className="sidebar-link d-flex align-items-center">
          <QuestionCircle className="me-2 icon" />
          Ayuda y Soporte
        </Nav.Link>
      </Nav>
    </div>
  );
};

export default Sidebar;
