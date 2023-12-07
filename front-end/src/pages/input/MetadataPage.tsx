import React, { useState, useEffect, useInsertionEffect, useRef } from 'react';
import { motion } from 'framer-motion';
import ProgressBar from '../../components/progress/ProgressBar'
import NavButton from '../../components/button/NavButton';
import PersonComponent from '../../components/home/PersonComponent';

import './MetadataPage.css';
import '../../components/home/Person.css';
import LanguageDropdown from '../../components/input/LanguageDropdown';
import { useAppContext } from '../../components/input/ContextProvider';

function MetadataPage() {
  const {chooseDifficulty} = useAppContext();

  const handleDifficultyButtonClick = (difficulty: string) => {
    chooseDifficulty(difficulty);
    console.log('button clicked')
  }

  return (
    //initial={{opacity:0}} animate={{opacity: 1}}
    <div className="songs-page">
      <motion.div className="body">
        <div className="main-container">
          <div className="dropdown-container">
            <LanguageDropdown />
          </div>
          <div className="difficulty-container">
          {/* {`difficulty-button ${buttonClicked ? 'clicked' : ''}`} */}
            <button onClick={() => handleDifficultyButtonClick("easy")} className="difficulty-button">easy</button>
            <button onClick={() => chooseDifficulty("medium")} className="difficulty-button">medium</button>
            <button onClick={() => chooseDifficulty("hard")} className="difficulty-button">hard</button>
          </div>
          <NavButton nextPage="/input/generation" displayedText="Start Game" />
          <div className="person-container-small">
            <PersonComponent handleHeadClick={() => {}} headClicked={false} disabledHover={true}/>
          </div>
        </div>
      </motion.div>
    </div>
  );
}

export default MetadataPage;
