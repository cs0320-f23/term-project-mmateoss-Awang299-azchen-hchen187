import React from 'react'

//interface that specifies values passed into the component
interface TrackSearchResultProps {
    track: string[]
    chooseTrack: (selectedTrack: string[]) => void;
}

//main component to process each track rendered
export default function TrackSearchResult(props: TrackSearchResultProps) {

    //method to update the useContext
    const handleTrackSelection = () => {
        props.chooseTrack(props.track)
    }
    
    //returning the component
  return (
        <div className="track-container" onClick={handleTrackSelection}>
        <img
          className="track-image"
          src={props.track[3]}
          style={{ width: "64px", height: "64px" }}
          alt="album cover"
        />
        <div className="track-info">
          <div className="track-title">{props.track[0]}</div>
          <div className="track-artist">{props.track[1]}</div>
        </div>
      </div>
      
  );
}
