import Header from "../../components/header/Header";
import HeroSection from "../../components/heroSection/HeroSection";
import FeaturesSection from "../../components/featureSection/FeatureSection";
import BenefitsSection from "../../components/benefitSection/BenefitsSection";
import TestimonialsSection from "../../components/testimonialSection/TestimonialsSection";
import Footer from "../../components/footer/Footer";

import logo from "../../assets/logo.jpg";

const LandingPage = () => {
  return (
    <>
      <Header
        title="Plataforma Médica"
        logoUrl={logo}
        buttons={[
          {
            label: "Características",
            onClick: () => scrollToSection("caracteristicas"),
          },
          { label: "Beneficios", onClick: () => scrollToSection("beneficios") },
          {
            label: "Testimonios",
            onClick: () => scrollToSection("testimonios"),
          },
        ]}
      />
      <HeroSection />
      <FeaturesSection />
      <BenefitsSection />
      <TestimonialsSection />
      <Footer />
    </>
  );
};

// 🔹 función para desplazamiento suave
const scrollToSection = (id) => {
  const section = document.getElementById(id);
  if (section) {
    section.scrollIntoView({ behavior: "smooth" });
  }
};

export default LandingPage;
