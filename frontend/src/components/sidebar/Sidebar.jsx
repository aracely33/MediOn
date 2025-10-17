import { Nav, Offcanvas } from "react-bootstrap";
import { QuestionCircle } from "react-bootstrap-icons";
import { sidebarLinks, iconMap } from "./sidebarLinks";
import "./Sidebar.css";

const Sidebar = ({ user, role, show, onHide }) => {
  const links = sidebarLinks[role];

  return (
    <Offcanvas
      show={show}
      onHide={onHide}
      responsive="lg"
      className="sidebar-container d-flex flex-column justify-content-between p-3"
    >
      <Offcanvas.Header closeButton className="d-lg-none">
        <Offcanvas.Title>Menú</Offcanvas.Title>
      </Offcanvas.Header>

      <Offcanvas.Body className="d-flex flex-column justify-content-between">
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

        <Nav className="sidebar-help flex-column gap-2 mt-4">
          <Nav.Link href="#" className="sidebar-link d-flex align-items-center">
            <QuestionCircle className="me-2 icon" />
            Ayuda y Soporte
          </Nav.Link>
        </Nav>
      </Offcanvas.Body>
    </Offcanvas>
  );
};

export default Sidebar;
