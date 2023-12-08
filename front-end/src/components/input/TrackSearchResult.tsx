import React from 'react'
import { SongData } from '../interfaces/Interface';

interface TrackSearchResultProps {
    track: string[]
    chooseTrack: (selectedTrack: string[]) => void;
}

export default function TrackSearchResult(props: TrackSearchResultProps) {

    const handleTrackSelection = () => {
        props.chooseTrack(props.track)
    }
    
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
