import { Navbar, Container, Button, Image } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";

const Header = ({ title, avatarUrl, onLogout }) => (
  <Navbar bg="light" className="px-4 py-2" expand="lg">
    <Container fluid>
      <Navbar.Brand className="d-flex align-items-center gap-2">
        <svg width="32" height="32" viewBox="0 0 48 48" fill="#1193d4">
          <path d="M8.57829 8.57829C5.52816 11.6284 ... L24 24L8.57829 8.57829Z" />
        </svg>
        <span className="fw-bold">{title}</span>
      </Navbar.Brand>
      <div className="d-flex align-items-center gap-3">
        <Button variant="primary" onClick={onLogout}>
          Cerrar Sesión
        </Button>
        <Image src={avatarUrl} roundedCircle width={40} height={40} />
      </div>
    </Container>
  </Navbar>
);

export default Header;
