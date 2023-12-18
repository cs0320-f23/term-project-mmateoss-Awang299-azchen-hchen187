import { BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import React from 'react';
import { ContextProvider } from './components/input/ContextProvider';
import HomePage from './pages/home/HomePage';
import SongsPage from './pages/input/SongsPage';
import GenerationPage from './pages/input/GenerationPage';
import GeneratedPlaylistPage from './pages/result/GeneratedPlaylistPage';
import AboutPage from './pages/home/AboutPage';
import FAQPage from './pages/home/FAQPage';

import './App.css';
import Toolbar from './components/toolbar/Toolbar';
import SettingsPage from './pages/input/SettingsPage';
import MultiLoadPage from './pages/multiplayer/MultiLoadPage';
import MultiGamePage from './pages/multiplayer/MultiGamePage';
import MultiSettingPage from './pages/multiplayer/MultiSettingPage';


/**
 * This is the highest level component!
 */
function App() {
  
  return (
    <ContextProvider>
      <div className="App">
        <Toolbar />
        <Router>
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/about" element={<AboutPage />} />
            <Route path="/faq" element={<FAQPage />} />
            <Route path="/input/songs" element={<SongsPage />} />
            <Route path="/input/settings" element={<SettingsPage />} />
            <Route path="/input/generation" element={<GenerationPage />} />
            <Route path="/result" element={<GeneratedPlaylistPage />} />
            
            <Route path="/multi/settings" element={<MultiSettingPage />} />
            <Route path="/multi/game" element={<MultiGamePage />} />
            <Route path="/multi/load" element={<MultiLoadPage />} />
          </Routes>
        </Router>
      </div>
    </ContextProvider>
  );
}

export default App;
