import React, { useState } from 'react'
import './GenerationPage.css'
import ProgressBar from '../../components/progress/ProgressBar'

export default function GenerationPage() {

  const [selectedSongs, setSelectedSongs] = useState(["song1"])
  const [selectedMetaData, setSelectedMetaData] = useState(["metadata1"])
  const [name, setName] = useState("")
  const [length, setLength] = useState(0)

  const handleGenerateButtonClick = () => {

  }

  return (
    <div className="generation-page">
      <ProgressBar step={3} />
      <div className="selections">
        <div className="song-selections"></div>
        {selectedSongs.map((song) => song)}
        <div className="metadata-selections">{selectedMetaData.map((song) => song)}</div>
      </div>
      <div className="playlist-info">
        <div className="name">
          {" "}
          <input placeholder="name"></input>
        </div>
        <div className="length">
          {" "}
          <input placeholder="length"></input>
        </div>
      </div>
      <a href="/result">
        <button className="generate-button"> GENERATE </button>
      </a>
    </div>
  );
}
