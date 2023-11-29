import React, { useState, useEffect} from 'react';
import EyeComponent from './EyeComponent';

interface FaceProps {
  headClicked: boolean;
}

function FaceComponent(props: FaceProps) {
  const [nosePosition, setNosePosition] = useState({ x: 0, y: 0 });
  const [mouthPosition, setMouthPosition] = useState({ x: 0, y: 0 });
  const [browPosition, setBrowPosition] = useState({ x: 0, y: 0 });
  

  useEffect(() => {
    const container = document.querySelector('.head-container') as HTMLElement;
    const nose = document.querySelector('.nose') as HTMLElement;
    const mouth = document.querySelector('.mouth') as HTMLElement;
    const leftbrow = document.querySelector('.leftbrow') as HTMLElement;
    const rightbrow = document.querySelector('.rightbrow') as HTMLElement;
  
    if (container && nose && mouth && leftbrow && rightbrow) {
      document.addEventListener('mousemove', updateLocation);
      container.addEventListener('mouseenter', handleMouseEnter);
      container.addEventListener('mouseleave', handleMouseLeave);
      return () => {
        document.removeEventListener('mousemove', updateLocation);
        container.removeEventListener('mouseenter', handleMouseEnter);
        container.removeEventListener('mouseleave', handleMouseLeave);
      };
    }
  }, []);

  const handleMouseEnter = () => {
    const mouth = document.querySelector('.mouth') as HTMLElement;
    mouth.classList.remove('mouth-shrink')
    mouth.classList.add('mouth-grow')
  }

  const handleMouseLeave = () => {
    const mouth = document.querySelector('.mouth') as HTMLElement;
    mouth.classList.remove('mouth-grow')
    mouth.classList.add('mouth-shrink')
  }


  const updateLocation = (e: { clientX: number; clientY: number; }) => {
    const container = document.querySelector('.head-container') as HTMLElement;

    const containerRect = container.getBoundingClientRect();
    const scaleX = containerRect.width / window.innerWidth*0.3;
    const scaleY = containerRect.height / window.innerHeight*0.3;
    const scaledX = (e.clientX - (containerRect.left + containerRect.width/2)) * scaleX;
    const scaledY = (e.clientY - (containerRect.top + containerRect.height/2)) * scaleY;
    setNosePosition({x: scaledX, y: scaledY});
    setMouthPosition({x: scaledX, y: scaledY});
    setBrowPosition({x: scaledX, y: scaledY});
  }


  return (
    <div className="face">
      <div className="upper-face">
        <div className="leftbrow" style={props.headClicked ? {} : {left: `${browPosition.x}px`, top: `${browPosition.y}px`}}></div>
        <div className="rightbrow" style={props.headClicked ? {} : {left: `${browPosition.x}px`, top: `${browPosition.y}px`}}></div>
      </div>
      <div className="mid-face">
        <div className="eye-container">
          <EyeComponent headClicked={props.headClicked}/>
        </div>
        <div className="space-between-eyes"></div>
        <div className="eye-container">
          <EyeComponent headClicked={props.headClicked}/>
        </div>
      </div>
      <div className="mid-lower-face">
        <div className="nose" style={props.headClicked ? {} : {left: `${nosePosition.x}px`, top: `${nosePosition.y}px`}}></div>
      </div>
      <div className="lower-face">
          <div className="mouth" style={props.headClicked ? {} : {left: `${mouthPosition.x}px`, top: `${mouthPosition.y}px`}}></div>
      </div>
    </div>
  );
}

export default FaceComponent;