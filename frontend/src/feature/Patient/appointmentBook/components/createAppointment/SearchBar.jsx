import { Form } from "react-bootstrap";

export const SearchBar = ({ onSearch }) => {
  const handleSearch = (e) => {
    onSearch(e.target.value);
  };

  return (
    <Form className="mb-4">
      <Form.Control
        type="text"
        onChange={handleSearch}
        placeholder="Buscar especialidad del médico..."
      />
    </Form>
  );
};
