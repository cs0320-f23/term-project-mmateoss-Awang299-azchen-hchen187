import { motion } from 'framer-motion';
import React from 'react'
import { useNavigate } from 'react-router-dom';

interface NavButtonProps {
    nextPage: string;
    displayedText: string;
}

export default function NavButton( {nextPage, displayedText} : NavButtonProps ) {

  const navigate = useNavigate();
  const handleButtonClick = () => {
    navigate(nextPage);
  }


  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="button-container">
      <button onClick={handleButtonClick} className="nav-button">{displayedText}</button>
    </motion.div>
  );
}