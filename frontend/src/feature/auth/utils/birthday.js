export const calculateAge = (birthDateString) => {
  if (!birthDateString) return null;
  const birthDate = new Date(birthDateString);
  const today = new Date();

  let age = today.getFullYear() - birthDate.getFullYear();
  const monthDiff = today.getMonth() - birthDate.getMonth();
  const dayDiff = today.getDate() - birthDate.getDate();

  if (monthDiff < 0 || (monthDiff === 0 && dayDiff < 0)) {
    age--;
  }

  return age;
};

// Función para formatear fecha y hora
export const formatDateTime = (date, time, showWeekday = false) => {
  const dateTime = new Date(`${date}T${time}`);
  let formattedDate = dateTime.toLocaleDateString("es-MX", {
    weekday: showWeekday ? "long" : undefined,
    year: "numeric",
    month: "long",
    day: "numeric",
  });
  const formattedTime = dateTime.toLocaleTimeString("es-MX", {
    hour: "2-digit",
    minute: "2-digit",
  });

  if (showWeekday) {
    formattedDate =
      formattedDate.charAt(0).toUpperCase() + formattedDate.slice(1);
  }

  return { date: formattedDate, time: formattedTime };
};
