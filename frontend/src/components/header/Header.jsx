import { Navbar, Container, Button, Image } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import "./Header.css";

const Header = ({ title, avatarUrl, logoUrl, buttons = [] }) => (
  <Navbar bg="light" className="header-container px-4 py-2" expand="lg">
    <Container fluid>
      <Navbar.Brand className="header-brand d-flex align-items-center gap-2">
        {logoUrl && (
          <Image src={logoUrl} width={60} className="me-2 header-logo" />
        )}
        <span className="header-title fw-bold">{title}</span>
      </Navbar.Brand>

      <div className="header-actions d-flex align-items-center gap-3">
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
      </div>
    </Container>
  </Navbar>
);

export default Header;
