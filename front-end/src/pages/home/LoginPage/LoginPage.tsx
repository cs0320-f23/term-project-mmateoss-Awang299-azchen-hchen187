import React, { useState, useEffect, useInsertionEffect, useRef } from 'react';
import './LoginPage.css';
import { motion } from 'framer-motion';
import { spotifyLogin } from './SpotifyConnection';
import Toolbar from '../../../components/Toolbar/Toolbar';


function LoginPage() {
  const handleHeadClick = () => {}


  return (
    //initial={{opacity:0}} animate={{opacity: 1}}  
    <motion.div className="body">
      <div className="main-container">
        <Toolbar />
        <button onClick={spotifyLogin}>Login to Spotify </button>
      </div>
      {/* <div className="person-container">
        <PersonComponent handleHeadClick={handleHeadClick} headClicked={false} />
      </div> */}
    </motion.div>
  );
}

export default LoginPage;