import React from 'react'
import { useNavigate } from 'react-router-dom';
import { recommendationInputData } from '../../mock/MockedData';

import "./MultiGamePage.css"

export default function MutltiGamePage() {
  const selectedTrack = recommendationInputData.data;

  const navigate = useNavigate();
  const handleButtonClick = () => {
      navigate('/multi/settings');
  }
  
  return (
    <div className="mp-game-page">
    <div className="main-container-mp-game">
      <div className="left-container-mp">
        <div className="album-display">
          <img
              className="track-image"
              src={selectedTrack.length !== 0 ? selectedTrack[3] : "error"}
              style={{ width: "120px", height: "120px" }}
              alt="album cover"
            />
        </div>
        <div className="p2-container">p2 screen with just history</div>
      </div>

      <div className="right-container-mp">
      <div className="p1-container">p1 screen with all components</div>
      </div>

    </div>
    <div className="exit-button-container">
        <button onClick={handleButtonClick} className="nav-button">leave game</button>
    </div>
    </div>
  )
}
