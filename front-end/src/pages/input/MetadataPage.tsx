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
  const {difficulty, chooseDifficulty} = useAppContext();
  const [hoveredButton, setHoveredButton] = useState<string | null>(null);

  const handleDifficultyButtonClick = (difficulty: string) => {
    chooseDifficulty(difficulty);
    setHoveredButton(null);
    console.log('button clicked')
  }

  const getDisplayedText = (input: string) => {
    switch (input) {
      case 'easy':
        return (
          <span>
            Fill in the missing word, just 
            <span style={{ fontWeight: 'bolder', textDecoration: 'underline', color: 'var(--green)' }}> like </span>
            this.
            <br></br>
          </span>
        );
      case 'medium':
        return (
          <span>
            Fill in the missing line:
            <br />
            "
            <span style={{fontWeight: 'normal', fontSize: '19pt'}}>
            I just need to tell you somthing - 
            </span>
            <br /> 
            <span style={{ fontWeight: 'bolder', textDecoration: 'underline', color: 'var(--green)' }}> I really really really really really really like you.</span>
            {' '}"
            <br />
          </span>
        );
      case 'hard':
        return (
          <span>
            Fill in the missing lines:
            <br /> 
            "
            <span style={{ fontWeight: 'bolder', color: 'var(--green)'}}>________________</span>
            {' '}"
            <br /> 
            "
            <span style={{ fontWeight: 'bolder', color: 'var(--green)' }}>_____________________________</span>
            {' '}"
            <br />
          </span>
        );
      default:
        return '';
    }
  }

  const getButtonText = (currDifficulty: string | null) => {
    if (currDifficulty != null && difficulty === "") {
      return getDisplayedText(currDifficulty)
    } else {
      return getDisplayedText(difficulty)
    }
   
    // switch (currDifficulty) {
    //   case 'easy':
    //     return getDisplayedText('easy');
    //   case 'medium':
    //     return (
    //       <span>
    //         Fill in the missing line:
    //         <br />
    //         "
    //         <span style={{fontWeight: 'normal'}}>
    //         I just need to tell you somthing - 
    //         </span>
    //         <br /> 
    //         <span style={{ fontWeight: 'bolder', textDecoration: 'underline', color: 'var(--green)' }}> I really really really really really really like you.</span>
    //         {' '}"
    //         <br />
    //       </span>
    //     );
    //   case 'hard':
    //     return (
    //       <span>
    //         Fill in the missing lines:
    //         <br /> 
    //         "
    //         <span style={{ fontWeight: 'bolder', color: 'var(--green)'}}>________________</span>
    //         {' '}"
    //         <br /> 
    //         "
    //         <span style={{ fontWeight: 'bolder', color: 'var(--green)' }}>_____________________</span>
    //         {' '}"
    //         <br />
    //       </span>
    //     );
    //   default:
    //     return 'kjfalkjshkajh';
    // }
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
            {['easy', 'medium', 'hard'].map((level) => (
                <button
                  key={level}
                  onClick={() => handleDifficultyButtonClick(level)}
                  className={`difficulty-button ${difficulty === level ? 'clicked' : ''}`}
                  onMouseEnter={() => setHoveredButton(level)}
                  onMouseLeave={() => setHoveredButton(null)}
                >
                  {level}
                </button>
              ))}
          </div>
          <div className="difficulty-text-container" >
            {<p>{getButtonText(hoveredButton)}</p>}
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
