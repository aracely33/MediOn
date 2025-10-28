export const passwordRules = [
  { label: "Mínimo 8 caracteres", test: (v) => v.length >= 8 },
  { label: "Al menos una letra mayúscula", test: (v) => /[A-Z]/.test(v) },
  { label: "Al menos un número", test: (v) => /[0-9]/.test(v) },
  {
    label: "Al menos un carácter especial (!@#$%^&*)",
    test: (v) => /[!@#$%^&*]/.test(v),
  },
];
