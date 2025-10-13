import { Card } from "react-bootstrap";

const NotificationCard = ({ title, description }) => (
  <Card className="mb-3 shadow-sm border-start border-4 border-primary">
    <Card.Body>
      <Card.Title>{title}</Card.Title>
      <Card.Text>{description}</Card.Text>
    </Card.Body>
  </Card>
);

export default NotificationCard;
