import React, { useEffect, useState } from 'react'
import { useAppContext } from '../input/ContextProvider';
import { TrackInfo } from '../interfaces/Interface';

interface GeneratedTrackComponentProps {
    track: TrackInfo
}

export default function GeneratedTrackComponent(props:GeneratedTrackComponentProps) {
    const {chooseTrack, selectedTrack} = useAppContext();
    const [chosen, setChosen] = useState(false);
  
    const handleRecClick = () => {
        const newTrack = [props.track.name, "", props.track.id, props.track.albumUrl]
        console.log("new track selected: " + props.track.name)
        chooseTrack(newTrack)
        // setChosen(true)
    }

    useEffect(() => {
        if (selectedTrack !== undefined) {
            console.log(selectedTrack[0] + " " + props.track.name)
            if (selectedTrack[0] === props.track.name) {
                setChosen(true);
            } else setChosen(false)
        }
    }, [selectedTrack])
  
    return (
        <div key={props.track.id} className="recommended-track-container" onClick={handleRecClick}>
        {chosen && <div className="chosen-lineart"/>}
        <div className="title">{props.track.name}</div>
        <img
          className="track-image"
          src={props.track.albumUrl}
          style={{ width: "175px", height: "175px"}}
          alt="album cover"
        />
      </div>
  )
}
