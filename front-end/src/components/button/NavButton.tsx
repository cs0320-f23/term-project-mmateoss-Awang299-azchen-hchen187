import { motion } from 'framer-motion';
import React from 'react'
import { useNavigate } from 'react-router-dom';

//navbutton component to allow the pages to link with eachother

//interface that outlines the values passed into the button
interface NavButtonProps {
    nextPage: string;
    displayedText: string;
    proceedToNextPage: boolean;
    onClickRejection: () => void;
}

//main component that renders the button
export default function NavButton( {nextPage, displayedText, proceedToNextPage, onClickRejection} : NavButtonProps ) {

    //method that navigates to the next page if the conditions are satisfied
  const navigate = useNavigate();
  const handleButtonClick = () => {
    if (proceedToNextPage) {
        navigate(nextPage);
    } else {
        onClickRejection();
    }
  }


  //returns the button display
  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="button-container">
      <button onClick={handleButtonClick} className="nav-button">{displayedText}</button>
    </motion.div>
  );
}