import React, { useState, useEffect, useInsertionEffect, useRef } from 'react';
import './SongsPage.css';
import { motion } from 'framer-motion';

function SongsPage() {


  return (
    //initial={{opacity:0}} animate={{opacity: 1}}  
    <motion.div className="body">
      <div className="main-container">
        <motion.div initial={{opacity:0}} animate={{opacity: 1}} className="progress-container">
          <div className="progress-bar-highlight">Step 1</div>
          <div className="progress-bar">Step 2</div>
          <div className="progress-bar">Step 3</div>
        </motion.div>
      </div>
    </motion.div>
  );
}

export default SongsPage;
