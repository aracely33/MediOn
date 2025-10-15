import { Nav } from "react-bootstrap";
import {
  GridFill,
  Calendar2Week,
  FileMedical,
  Bell,
  Gear,
  QuestionCircle,
} from "react-bootstrap-icons";

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
    <div className="bg-white border-end vh-100 p-3 d-flex flex-column justify-content-between">
      {/* Parte superior: avatar y nombre */}
      <div>
        <div className="d-flex align-items-center gap-2 mb-4">
          <img
            src={user.avatar}
            alt="Avatar"
            className="rounded-circle"
            width={40}
            height={40}
          />
          <div>
            <h6 className="mb-0">{user.name}</h6>
            <small className="text-muted">ID: {user.id}</small>
          </div>
        </div>

        {/* Links fijos */}
        <Nav className="flex-column gap-2">
          {links.map((link) => {
            const Icon = iconMap[link.icon];
            return (
              <Nav.Link
                key={link.label}
                href={link.href}
                className={link.active ? "bg-primary text-white rounded" : ""}
              >
                {Icon && <Icon className="me-2" />}
                {link.label}
              </Nav.Link>
            );
          })}
        </Nav>
      </div>

      {/* Parte inferior: ayuda */}
      <Nav className="flex-column gap-2">
        <Nav.Link href="#">
          <QuestionCircle className="me-2" />
          Ayuda y Soporte
        </Nav.Link>
      </Nav>
    </div>
  );
};

export default Sidebar;
