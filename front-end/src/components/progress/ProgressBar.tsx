import { motion } from 'framer-motion';
import React from 'react'

interface ProgressBarProps {
    step : number;
}

export default function ProgressBar( {step  } : ProgressBarProps ) {
  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="progress-container">
      <div className={step === 1? "progress-bar-highlight" : "progress-bar"}>Step 1</div>
      <div className={step === 2? "progress-bar-highlight" : "progress-bar"}>Step 2</div>
      <div className={step === 3? "progress-bar-highlight" : "progress-bar"}>Step 3</div>
    </motion.div>
  );
}
 