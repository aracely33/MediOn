import { Navbar, Container, Button, Image, Nav } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import "./Header.css";

const Header = ({ title, avatarUrl, logoUrl, buttons = [] }) => (
  <Navbar bg="light" expand="lg" className="header-container px-4 py-2">
    <Container fluid>
      <Navbar.Brand className="header-brand d-flex align-items-center gap-2">
        {logoUrl && (
          <Image src={logoUrl} className="me-2 header-logo rounded" />
        )}
        <span className="header-title fw-bold">{title}</span>
      </Navbar.Brand>

      <Navbar.Toggle aria-controls="navbar-nav" className="ms-auto" />

      <Navbar.Collapse id="navbar-nav">
        <Nav className="ms-auto d-flex align-items-center gap-3">
          {buttons.map((btn, index) => (
            <Button key={index} className={btn.className} onClick={btn.onClick}>
              {btn.label}
            </Button>
          ))}

          {!logoUrl && avatarUrl && (
            <Image
              src={avatarUrl}
              roundedCircle
              width={40}
              height={40}
              className="user-avatar"
            />
          )}
        </Nav>
      </Navbar.Collapse>
    </Container>
  </Navbar>
);

export default Header;
