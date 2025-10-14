import { Nav } from "react-bootstrap";
import { QuestionCircle } from "react-bootstrap-icons";
import { sidebarLinks, iconMap } from "./sidebarLinks";
import "./Sidebar.css";

const Sidebar = ({ user, role }) => {
  // links según rol
  const links = sidebarLinks[role]; // ya no hace falta el fallback

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
