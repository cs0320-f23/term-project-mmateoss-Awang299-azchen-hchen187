import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { LineAnimationComponent } from '../../components/home/LineAnimationComponent';
import PersonComponent from '../../components/home/PersonComponent';
// import HeadphonesBarAfter from "../../assets/images/HomePage/headphone-bar-after.svg";

import './HomePage.css';
import '../../components/home/Person.css';

function HomePage() {
    const navigate = useNavigate();
    const [animate, setAnimate] = useState<boolean>(false);
    const [headClicked, setHeadClicked] = useState<boolean>(false);
    const transition = {duration: 1, ease: [0.43, 0.13, 0.23, 0.96]}
    
    const handleHeadClick = () => {
      setAnimate(true);
      setTimeout(() => {
          setAnimate(false);
          setHeadClicked(true);
          setTimeout(() => {
            navigate('/input/songs');
          }, 1000);
      }, 1000);
    }
  
    const animateStyle = {
      scale: headClicked ? 50 : 1,
      transformOrigin: '50% 25%',
    };
  
  
    return (
      <>
        {/* //TODO: fix this animate/ exit issue */}
        <div className="home-page">
          <motion.div exit={{ opacity: 0 }} transition={transition} className="main-container">
            <div className="filler-container"></div>
            <LineAnimationComponent startAnimation={animate} />
            <div className="person-container">
              {/* //TODO: same issue with this one */}
              <motion.div key="text-container" exit={{ opacity: 0 }} transition={transition} className="text-container">
                <div className="text-bottom">
                  this is a super cool website
                  <br></br>
                  blah blah blah blah blah balh blah blah blah
                  <br></br>
                  yeet
                </div>
                {/* <div className="text-bottom">
                    a different bit of text to 
                    <br></br>
                    make this website even cooler lolllllllllll 
                    <br></br>
                    yeehaw
                  </div> */}
              </motion.div>
              <motion.div
                initial={{ scale: 1 }}
                transition={{ duration: 0.75 }}
                animate={animateStyle}
                className="expanded-container"
              >
                <PersonComponent handleHeadClick={handleHeadClick} headClicked={headClicked} disabledHover={false}/>
              </motion.div>
            </div>
          </motion.div>
        </div>
      </>
    );
  }
  
  export default HomePage;