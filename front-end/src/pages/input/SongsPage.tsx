import React, { useState, useEffect, useInsertionEffect, useRef } from 'react';
import { motion } from 'framer-motion';
import ProgressBar from '../../components/progress/ProgressBar'
import NavButton from '../../components/button/NavButton';
import PersonComponent from '../../components/home/PersonComponent';

import './SongsPage.css';
import '../../components/home/Person.css';

function SongsPage() {

  return (
    //initial={{opacity:0}} animate={{opacity: 1}}
    <div className="songs-page">
      <motion.div className="body">
        <div className="main-container">
          <ProgressBar step={1} />
          <NavButton nextPage="/input/metadata"/>
          <div className="person-container-small">
            <PersonComponent handleHeadClick={() => {}} headClicked={false} disabledHover={true}/>
          </div>
        </div>
      </motion.div>
    </div>
  );
}

export default SongsPage;
