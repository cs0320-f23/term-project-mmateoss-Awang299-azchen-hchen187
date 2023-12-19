import React, { useState, useEffect} from 'react';

//interface that specifies values passed into the component
interface EyeComponentProps {
  headClicked: boolean;
}

//main component for the eye
function EyeComponent(props: EyeComponentProps) {
  const [eyePosition, setEyePosition] = useState({ x: 0, y: 0 });
  const [pupilPosition, setPupilPosition] = useState({ x: 0, y: 0 });
  

  //sets up the event listeners for the eyes
  useEffect(() => {
    const container = document.querySelector('.head-container') as HTMLElement;
    const eye = document.querySelector('.eye') as HTMLElement;
    const pupil = document.querySelector('.pupil') as HTMLElement;

    if (container && eye && pupil) {
      document.addEventListener('mousemove', updateLocation);
      return () => {
        document.removeEventListener('mousemove', updateLocation);
      };
    }
  }, []);

  //method to calculate and update the eye location based on mouse movement
  const updateLocation = (e: { clientX: number; clientY: number; }) => {
    const container = document.querySelector('.head-container') as HTMLElement;
    const eye = document.querySelector('.eye') as HTMLElement;

    const containerRect = container.getBoundingClientRect();
    const scaleX = containerRect.width / window.innerWidth*0.2;
    const scaleY = containerRect.height / window.innerHeight*0.3;
    const scaledX = (e.clientX - (containerRect.left + containerRect.width/2)) * scaleX;
    const scaledY = (e.clientY - (containerRect.top + containerRect.height/2)) * scaleY;
    setEyePosition({x: scaledX, y: scaledY});

      
    const containerEye = eye.getBoundingClientRect(); // Use the eye as the container
    const scalePupilX = containerEye.width / window.innerWidth*0.5;
    const scalePupilY = containerEye.height / window.innerHeight*0.5;
    const scaledPupilX = (e.clientX - (containerEye.left + containerEye.width/2)) * scalePupilX;
    const scaledPupilY = (e.clientY - (containerEye.top + containerEye.height/2)) * scalePupilY;
    setPupilPosition({ x: scaledPupilX, y: scaledPupilY });
  }

  //returns the component
  return (
    <div className="eye" style={props.headClicked ? {} : {left: `${eyePosition.x}px`, top: `${eyePosition.y}px`}}>
      <div style= {props.headClicked ? {} : {left: `${pupilPosition.x}px`, top: `${pupilPosition.y}px`}} className="pupil"></div>
    </div>
  );
}

export default EyeComponent;