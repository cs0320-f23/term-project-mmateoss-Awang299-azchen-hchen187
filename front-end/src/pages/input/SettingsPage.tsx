import React, { useState, useEffect} from 'react';
import { motion } from 'framer-motion';
import NavButton from '../../components/button/NavButton';
import PersonComponent from '../../components/home/PersonComponent';
import LanguageDropdown from '../../components/input/LanguageDropdown';
import { useAppContext } from '../../components/input/ContextProvider';

import './SettingsPage.css';
import '../../components/home/Person.css';

//main component of the settings page
function SettingsPage() {
  const {difficulty, chooseDifficulty, nativeLanguage, chooseNativeLanguage, songLanguage, chooseSongLanguage} = useAppContext();
  const [hoveredButton, setHoveredButton] = useState<string | null>(null);
  const [fieldsPopulated, setFieldsPopulated] = useState(false);
  const [displayWarning, setDisplayWarning] = useState(false);

  //resets values upon page render
  useEffect(() => {
    console.log("settings page renders")
    chooseDifficulty("");
    chooseNativeLanguage("");
    chooseSongLanguage("");
  }, [])

  //updates fieldsPopulated boolean
  useEffect(() => {
    if (difficulty !== "" && nativeLanguage !== "" && songLanguage !== "") {
      setFieldsPopulated(true)
    } else {
      setFieldsPopulated(false)
    }
  }, [difficulty, nativeLanguage, songLanguage])

  //method to toggle the clicking of difficulty buttons
  const handleDifficultyButtonClick = (difficulty: string) => {
    chooseDifficulty(difficulty);
    setHoveredButton(null);
  }

  //method to toggle display warning boolean when next page button is rejected
  const handleButtonRejection = () => {
    setDisplayWarning(true);
  }

  //method to store the displayed text and return based on input string
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

  //method to get button text based on difficulty selected
  const getButtonText = (currDifficulty: string | null) => {
    if (currDifficulty != null && difficulty === "") {
      return getDisplayedText(currDifficulty)
    } else {
      return getDisplayedText(difficulty)
    }
  }



  //returns the page's component
  return (
    <div className="settings-page">
      <motion.div className="body">
        <div className="main-container">
          <div className="step-2-lineart-container">
            <div className="step-2-lineart" />
          </div>
          <div className="instructions-container-step2">
            Step 2:
            <br></br>
            select your {' '}
            <span style={{textDecoration: 'underline'}}>song's language</span>:
            <br></br>
            <br/>
            select your {' '}
            <span style={{textDecoration: 'underline'}}>native language</span>:
          </div>
          <div className="step-3-lineart-container">
            <div className="step-3-lineart" />
          </div>
          <div className="instructions-container-step3">
            Step 3:
            <br></br>
            select your {' '}
            <span style={{textDecoration: 'underline'}}>game difficulty</span>
          </div>
          <div className="dropdown-container">
            <LanguageDropdown setSongLang={true}/>
            <br/>
            <br/>
            <LanguageDropdown setSongLang={false}/>
          </div>
          <div className="difficulty-container">
            {["easy", "medium", "hard"].map((level) => (
              <button
                key={level}
                onClick={() => handleDifficultyButtonClick(level)}
                className={`difficulty-button ${
                  difficulty === level ? "clicked" : ""
                }`}
                onMouseEnter={() => setHoveredButton(level)}
                onMouseLeave={() => setHoveredButton(null)}
              >
                {level}
              </button>
            ))}
          </div>
          <div className="difficulty-text-container">
            {<p>{getButtonText(hoveredButton)}</p>}
          </div>
          <NavButton nextPage="/input/generation" displayedText="Start Game" proceedToNextPage={fieldsPopulated} onClickRejection={handleButtonRejection} />
          <div className="person-container-small">
            <div className={`${displayWarning ? "warning-message-container" : ""}`}>Please input the languages & difficulty!</div>
            <PersonComponent
              handleHeadClick={() => {}}
              headClicked={false}
              disabledHover={true}
            />
          </div>
        </div>
      </motion.div>
    </div>
  );
}

export default SettingsPage;
