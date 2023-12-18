import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { LineAnimationComponent } from '../../components/home/LineAnimationComponent';
import PersonComponent from '../../components/home/PersonComponent';

import './HomePage.css';
import '../../components/home/Person.css';

//main component of the homepage
function HomePage() {
    const navigate = useNavigate();
    const [animate, setAnimate] = useState<boolean>(false);
    const [headClicked, setHeadClicked] = useState<boolean>(false);
    const [hovered, setHovered] = useState(false);
    const transition = {duration: 1, ease: [0.43, 0.13, 0.23, 0.96]}
    
    //method to handle navigating to the next page when head is clicked
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
  
    //outlining the transition for rendering the next page
    const animateStyle = {
      scale: headClicked ? 50 : 1,
      transformOrigin: '50% 25%',
    };
  
    //returning the page component
    return (
      <>
        <div className="home-page">
          <motion.div
            exit={{ opacity: 0 }}
            transition={transition}
            className="main-container"
          >
            <div className="filler-container"></div>
            <LineAnimationComponent startAnimation={animate} />
            <div className="person-container">
              <motion.div
                key="text-container"
                exit={{ opacity: 0 }}
                transition={transition}
                className="text-container"
              >
                <div className="text-bottom">
                  SpotiDuo:
                  <br></br>
                  Type the Beat,
                  <br></br>
                  Speak the Language!
                </div>
              </motion.div>

              <motion.div
                onHoverStart={() => setHovered(true)}
                onHoverEnd={() => setHovered(false)}
                initial={{ scale: 1 }}
                transition={{ duration: 0.75 }}
                animate={animateStyle}
                className="expanded-container"
              >
                <PersonComponent
                  handleHeadClick={handleHeadClick}
                  headClicked={headClicked}
                  disabledHover={false}
                />
              </motion.div>

              <div className={hovered ? "click-me-container-populated" : "click-me-container-empty"}>
                Click me!
              </div>
            </div>
          </motion.div>
        </div>
      </>
    );
  }
  
  export default HomePage;