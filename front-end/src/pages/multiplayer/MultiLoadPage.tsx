import React from 'react'
import { useNavigate } from 'react-router-dom';
import NavButton from '../../components/button/NavButton';
import LyricsAudio from '../../components/input/LyricsAudio';


import "./MultiLoadPage.css"

export default function MultiLoadPage() {
    const navigate = useNavigate();
    const handleButtonClick = () => {
        navigate('/multi/game');
    }


  return (
    <div className="mp-load-page">
    <div className="main-container-mp-load">
        <div className="loading-text-container">
            Waiting for opponent...
        </div>
        <div>
            <LyricsAudio/>
        </div>
    </div>
    <NavButton
        nextPage="/multi/game"
        displayedText="go to game (will be removed)"
        //proceedToNextPage={fieldsPopulated}
        proceedToNextPage={true}
        // onClickRejection={handleButtonRejection}
        onClickRejection={() => {}}
      />
    <div className="exit-button-container">
        <button onClick={handleButtonClick} className="nav-button">cancel</button>
    </div>
</div>
  )
}
