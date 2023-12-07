import React from 'react'
import { SongData } from '../interfaces/Interface';

interface TrackSearchResultProps {
    track: SongData
    chooseTrack: (selectedTrack: SongData) => void;
}

export default function TrackSearchResult(props: TrackSearchResultProps) {

    const handleTrackSelection = () => {
        props.chooseTrack(props.track)
    }
    
  return (
        <div className="track-container" onClick={handleTrackSelection}>
        <img
          className="track-image"
          src={props.track.albumUrl}
          style={{ width: "64px", height: "64px" }}
          alt="album cover"
        />
        <div className="track-info">
          <div className="track-title">{props.track.title}</div>
          <div className="track-artist">{props.track.artist}</div>
        </div>
      </div>
      
  );
}
