import React from "react";
import { Image } from "react-bootstrap";

const Avatar = ({ src, size = 40, alt = "Avatar" }) => {
  return (
    <div
      style={{
        width: size,
        height: size,
        borderRadius: "50%",
        backgroundImage: `url(${src})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
      }}
      alt={alt}
    />
  );
};

export default Avatar;
