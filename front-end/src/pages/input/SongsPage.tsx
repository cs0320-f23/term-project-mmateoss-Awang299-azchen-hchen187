import React, { useState, useEffect, useInsertionEffect, useRef } from 'react';
import './SongsPage.css';
import { motion } from 'framer-motion';
import ProgressBar from '../../components/progress/ProgressBar'

function SongsPage() {

  return (
    //initial={{opacity:0}} animate={{opacity: 1}}  
    <motion.div className="body">
      <div className="main-container">
        <ProgressBar step={1}/>
      </div>
    </motion.div>
  );
}

export default SongsPage;
