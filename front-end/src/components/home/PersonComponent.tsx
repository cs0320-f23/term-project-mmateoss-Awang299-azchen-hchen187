import React, { useState, useEffect} from 'react';
import HeadComponent from './HeadComponent';

//interface that specifies values passed into the component
interface PersonProps {
    handleHeadClick: () => void;
    headClicked: boolean;
    disabledHover: boolean;
}

//main component to render the person
function PersonComponent(props: PersonProps) {
  const [HPPosition, setHPPosition] = useState({ x: 0, y: 0 });
  const handleHover = props.disabledHover ? 'disable-hover' : '';
  const containerStyle: React.CSSProperties = {
    pointerEvents: props.disabledHover ? 'none' : 'auto',
  };


  //sets up the variables for event listeners
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

  //method to update component location based on mouse movement
  const updateLocation = (e: { clientX: number; clientY: number; }) => {
    const container = document.querySelector('.head-container') as HTMLElement;

    const containerRect = container.getBoundingClientRect();
    const scaleX = containerRect.width / window.innerWidth*0.12;
    const scaleY = containerRect.height / window.innerHeight*0.2;
    const scaledX = (e.clientX - (containerRect.left + containerRect.width/2)) * scaleX;
    const scaledY = (e.clientY - (containerRect.top + containerRect.height/2)) * scaleY;
    setHPPosition({x: scaledX, y: scaledY});
  }

  //returns the component
  return (
        <div className={`head-container ${handleHover}`} style={containerStyle}>
            <div className="headphone-container">
                <div className="left-headphone" style={props.headClicked ? {} : {left: `${HPPosition.x + 7}px`, top: `${HPPosition.y}px`}}></div>
            </div>
            <HeadComponent handleHeadClick={props.handleHeadClick} headClicked={props.headClicked} />
            <div className="headphone-container">
                <div className="right-headphone" style={props.headClicked ? {} : {left: `${HPPosition.x - 7}px`, top: `${HPPosition.y}px`}}></div>
            </div>
            <div className="headphone-bar" style={props.headClicked ? {} : {left: `${HPPosition.x +60}px`, top: `${HPPosition.y*1.6 -15}px`}}></div>
        </div>
    
  );
}

export default PersonComponent;