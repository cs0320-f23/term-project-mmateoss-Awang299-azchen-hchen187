import './App.css';
import { BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import {AnimatePresence, motion} from 'framer-motion';
import React from 'react';
import HomePage from './pages/home/HomePage';
import SongsPage from './pages/input/SongsPage/SongsPage';

/**
 * This is the highest level component!
 */
function App() {

  const LandingPageComponent = () => (
      <motion.div key="home" initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }}>
        <HomePage />
      </motion.div>
  )

  const SongsPageComponent = () => (
      <motion.div key="songs" initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }}>
        <SongsPage />
      </motion.div>
  )

  
  return (
    <Router>
      <Routes>
          <Route
            path="/"
            element={
              <AnimatePresence initial={false}>
                <LandingPageComponent />
              </AnimatePresence>
            }
          />
          <Route
          path="/SongsPage"
          element={
            <AnimatePresence initial={false}>
              <SongsPageComponent />
            </AnimatePresence>
          }
          />
      </Routes>
    </Router>
  );
}

export default App;
