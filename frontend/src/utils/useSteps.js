import { useState } from "react";

export const useSteps = (initialStep = 1, totalSteps = 7) => {
  const [currentStep, setCurrentStep] = useState(initialStep);

  const next = () => {
    setCurrentStep((prev) => Math.min(prev + 1, totalSteps));
  };

  const prev = () => {
    setCurrentStep((prev) => Math.max(prev - 1, 1));
  };

  const goTo = (step) => {
    if (step >= 1 && step <= totalSteps) setCurrentStep(step);
  };

  return { currentStep, next, prev, goTo };
};
