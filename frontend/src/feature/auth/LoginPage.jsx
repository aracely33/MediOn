import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import Card from "react-bootstrap/Card";
import "./LoginPage.css";

function LoginPage() {
  return (
    <div className="login-container">
      <div className="login-wrapper">
        <h1 className="brand-title">🩺 Portal de Citas MediON</h1>

        <Card className="login-card">
          <Card.Body className="login-card-body">
            <Card.Title className="mb-3">Iniciar Sesión</Card.Title>
            <Card.Subtitle className="mb-3 text-muted">
              Bienvenido al portal de coordinación de citas.
            </Card.Subtitle>

            <Form>
              <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label>Correo electrónico</Form.Label>
                <Form.Control
                  type="email"
                  name="email"
                  placeholder="usuario@correo.com"
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="formBasicPassword">
                <Form.Label>Contraseña</Form.Label>
                <Form.Control
                  type="password"
                  name="password"
                  placeholder="Ingresa tu contraseña"
                />
                <div className="text-end">
                  <Card.Link href="#">¿Olvidaste tu contraseña?</Card.Link>
                </div>
              </Form.Group>

              <Button variant="primary" type="submit" className="login-btn">
                Iniciar Sesión
              </Button>

              <p className="register-text">
                ¿No tienes una cuenta?
                <Card.Link href="#"> Regístrate aquí</Card.Link>
              </p>
            </Form>
          </Card.Body>
        </Card>

        <footer>
          © 2025 Portal de Citas. Todos los derechos reservados.{" "}
          <a href="#">Política de Privacidad</a>
        </footer>
      </div>
    </div>
  );
}

export default LoginPage;
