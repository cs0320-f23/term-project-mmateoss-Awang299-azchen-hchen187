import React, { useEffect, useState } from 'react'
import { useAppContext } from '../input/ContextProvider';
import { TrackInfo } from '../interfaces/Interface';

//More narrow interface definitions for the returned recommendation json
interface GeneratedTrackComponentProps {
    track: TrackInfo
}

//main comopnent to display the recommended tracks
export default function GeneratedTrackComponent(props:GeneratedTrackComponentProps) {
    const {chooseTrack, selectedTrack} = useAppContext();
    const [chosen, setChosen] = useState(false);
  
    //method to update the selectedTrack in the usecontext 
    const handleRecClick = () => {
        const newTrack = [props.track.name, "", props.track.id, props.track.albumUrl]
        console.log("new track selected: " + props.track.name)
        chooseTrack(newTrack)
    }

    //useeffect to check if the track has been selected based on selectedTrack
    useEffect(() => {
        if (selectedTrack[0] === props.track.name) {
            setChosen(true);
        } else setChosen(false)
    }, [selectedTrack])
  
    //returns the component
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
