import { Navbar, Container, Button, Image } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import "./Header.css";

const Header = ({ title, avatarUrl, onLogout }) => (
  <Navbar bg="light" className="header-container px-4 py-2" expand="lg">
    <Container fluid>
      <Navbar.Brand className="header-brand d-flex align-items-center gap-2">
        <span className="header-title fw-bold">{title}</span>
      </Navbar.Brand>

      <div className="header-actions d-flex align-items-center gap-3">
        <Button className="logout-btn" onClick={onLogout}>
          Cerrar Sesión
        </Button>
        <Image
          src={avatarUrl}
          roundedCircle
          width={40}
          height={40}
          className="user-avatar"
        />
      </div>
    </Container>
  </Navbar>
);

export default Header;
