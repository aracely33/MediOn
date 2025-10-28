import Header from "../../components/header/Header";
import HeroSection from "../../components/heroSection/HeroSection";
import FeaturesSection from "../../components/featureSection/FeatureSection";
import BenefitsSection from "../../components/benefitSection/BenefitsSection";
import TestimonialsSection from "../../components/testimonialSection/TestimonialsSection";
import Footer from "../../components/footer/Footer";
import "./LandingPage.css";
import logo from "../../assets/logoMed.svg";

const LandingPage = () => {
  return (
    <>
      <Header
        className="hide-title-mobile"
        title="Plataforma Médica"
        logoUrl={logo}
        buttons={[
          {
            label: "Características",
            onClick: () => scrollToSection("caracteristicas"),
            className: "header-btn buttons",
          },
          {
            label: "Beneficios",
            onClick: () => scrollToSection("beneficios"),
            className: "header-btn buttons",
          },
          {
            label: "Testimonios",
            onClick: () => scrollToSection("testimonios"),
            className: "header-btn buttons",
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

const scrollToSection = (id) => {
  const section = document.getElementById(id);
  if (section) {
    section.scrollIntoView({ behavior: "smooth" });
  }
};

export default LandingPage;
