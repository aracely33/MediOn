import { Nav } from "react-bootstrap";

const Sidebar = ({ user }) => (
  <div className="bg-white border-end vh-100 p-3 d-flex flex-column justify-content-between">
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
          <small className="text-muted">ID Paciente: {user.id}</small>
        </div>
      </div>
      <Nav className="flex-column gap-2">
        {user.links.map((link) => (
          <Nav.Link
            key={link.label}
            href={link.href}
            className={link.active ? "bg-primary text-white rounded" : ""}
          >
            <span className="material-symbols-outlined">{link.icon}</span>{" "}
            {link.label}
          </Nav.Link>
        ))}
      </Nav>
    </div>
    <Nav className="flex-column gap-2">
      <Nav.Link href="#">
        <span className="material-symbols-outlined">help_outline</span> Ayuda y
        Soporte
      </Nav.Link>
    </Nav>
  </div>
);

export default Sidebar;
