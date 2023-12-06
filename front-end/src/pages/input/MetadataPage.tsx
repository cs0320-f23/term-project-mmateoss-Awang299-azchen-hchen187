import React, { useState, useEffect, useInsertionEffect, useRef } from 'react';
import { motion } from 'framer-motion';
import ProgressBar from '../../components/progress/ProgressBar'
import NavButton from '../../components/button/NavButton';
import PersonComponent from '../../components/home/PersonComponent';

import './MetadataPage.css';
import '../../components/home/Person.css';

function MetadataPage() {

  return (
    //initial={{opacity:0}} animate={{opacity: 1}}
    <div className="songs-page">
      <motion.div className="body">
        <div className="main-container">
          <ProgressBar step={2} />
          <NavButton nextPage="/input/generation"/>
          <div className="person-container-small">
            <PersonComponent handleHeadClick={() => {}} headClicked={false} disabledHover={true}/>
          </div>
        </div>
      </motion.div>
    </div>
  );
}

export default MetadataPage;
