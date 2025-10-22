export const dateFormatter = new Intl.DateTimeFormat("es-ES", {
  weekday: "long",
  year: "numeric",
  month: "long",
  day: "numeric",
});

export const timeFormatter = new Intl.DateTimeFormat("es-ES", {
  hour: "2-digit",
  minute: "2-digit",
});
