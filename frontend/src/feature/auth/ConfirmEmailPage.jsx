import VerificationForm from "./components/VerificationForm";

const ConfirmEmailPage = () => {
  const handleVerify = async (code) => {
    try {
      // llamada al backend
      // await axios.post("/api/verify", { code });
      console.log("Verifying code:", code);
      alert("Code verified successfully!");
    } catch (error) {
      console.error(error);
      alert("Invalid code. Please try again.");
    }
  };

  return <VerificationForm onVerify={handleVerify} />;
};

export default ConfirmEmailPage;
