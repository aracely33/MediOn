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

export const convertTo24Hour = (time) => {
  const [hours, minutes, period] = time.split(/[: ]/);

  let hours24 = parseInt(hours, 10);
  if (period === "PM" && hours24 !== 12) hours24 += 12;
  if (period === "AM" && hours24 === 12) hours24 = 0;

  return `${hours24.toString().padStart(2, "0")}:${minutes}`;
};
