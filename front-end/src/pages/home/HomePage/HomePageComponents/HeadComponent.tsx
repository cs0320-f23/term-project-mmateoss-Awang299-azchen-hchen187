import React, { useState, useEffect} from 'react';
import './../HomePage.css';
import FaceComponent from './FaceComponent';

interface HeadProps {
  handleHeadClick: () => void;
  headClicked: boolean;
}

function HeadComponent(props: HeadProps) {
  const [headPosition, setHeadPosition] = useState({ x: 0, y: 0 });
  

  useEffect(() => {
    const container = document.querySelector('.head-container') as HTMLElement;
    const head = document.querySelector('.head') as HTMLElement;
    const body = document.body;

    if (container && head && body) {
      document.addEventListener('mousemove', updateLocation);
      return () => {
        document.removeEventListener('mousemove', updateLocation);
      };
    }
  }, []);


  const updateLocation = (e: { clientX: number; clientY: number; }) => {
    const container = document.querySelector('.head-container') as HTMLElement;

    const containerRect = container.getBoundingClientRect();
    const scaleX = containerRect.width / window.innerWidth*0.1;
    const scaleY = containerRect.height / window.innerHeight*0.1;
    const scaledX = (e.clientX - (containerRect.left + containerRect.width/2)) * scaleX;
    const scaledY = (e.clientY - (containerRect.top + containerRect.height/2)) * scaleY;
    setHeadPosition({x: scaledX, y: scaledY});
  }

  return (
    <div className="head" onClick={props.handleHeadClick} style={props.headClicked ? {color:'#f037a5'} : {left: `${headPosition.x}px`, top: `${headPosition.y}px`}}>
      <FaceComponent headClicked={props.headClicked}/>
    </div>
  );
}

export default HeadComponent;