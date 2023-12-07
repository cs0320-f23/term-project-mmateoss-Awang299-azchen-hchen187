import './App.css';
import { BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import {AnimatePresence, motion} from 'framer-motion';
// import React from 'react';
import HomePage from './pages/home/HomePage';
import SongsPage from './pages/input/SongsPage';
import MetadataPage from './pages/input/MetadataPage';
import GenerationPage from './pages/input/GenerationPage';
import GeneratedPlaylistPage from './pages/result/GeneratedPlaylistPage';
import LoadingPage from './pages/result/LoadingPage';
import Toolbar from './components/toolbar/Toolbar';

import React, { useState } from 'react';

import SpotifyPlayer from 'react-spotify-web-playback';


/**
 * This is the highest level component!
 */
function App() {

  const [token, setToken] = useState(`BQBmC-wWVn4u__SntbAVhTdxShJE9lKZpEW5s4e_TrpHqb-ozOyB0Cm51_ag_aIjh2t5sxo7T133TVPsjiLrfiMZPaHJvU59M9TsIbatAjhRQAgEPGPtBGN3biB8a-LVzG78tEiLczvF4aRtDGkBOtqvBsZ-irl5jjordmAIK5i4hmfYbTHf-BEkYEa0d3X982EeRQTgoEo
  `)
  const [trackUri, setTrackUri] = useState("spotify:track:5f4Hy5mw5SRaUgXX9c6P5S")
  
  return (
    <div className="App">
      {/* <Toolbar /> */}
      <Router>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/input/generation" element={<GenerationPage />} />
          <Route path="/input/songs" element={<SongsPage />} />
          <Route path="/input/metadata" element={<MetadataPage />} />
          <Route path="/result" element={<GeneratedPlaylistPage />} />
         
        </Routes>
      </Router>
    </div>
  );

}

export default App;
