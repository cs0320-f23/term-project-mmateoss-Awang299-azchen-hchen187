import React, { useEffect, useState } from 'react'
import { motion } from 'framer-motion';

import './GeneratedPlaylistPage.css';
import '../../components/home/Person.css';
import NavButton from '../../components/button/NavButton';
import PersonComponent from '../../components/home/PersonComponent';
import { useAppContext } from '../../components/input/ContextProvider';
import { recommendationInputData, recommendationOutputData } from '../../mock/MockedData';
import { RecommendationOutputData, TrackInfo } from '../../components/interfaces/Interface';


async function fetchSongs(selectedTrackTitle: string) : Promise<RecommendationOutputData> {
  const limit = 5;
  const variability = 0.2;
  const tokenObject = await fetch("http://localhost:3232/token");
  const tokenJson = await tokenObject.json();
  const token = tokenJson.token;
  const serverInput = "recommendation?allNames=" + selectedTrackTitle + "&token=" + token + "&variability=" + variability + "&limit=" + limit;
  console.log(serverInput)
  const fetched = await fetch("http://localhost:3232/" + serverInput);
  const dataObject = await fetched.json();
  return dataObject;
}

async function mockFetchSongs(selectedTrackTitle:string) : Promise<RecommendationOutputData> {
  return recommendationOutputData;
}



export default function GeneratedPlaylistPage() {
  console.log("component renders")
  const { selectedTrack } = useAppContext();
  const [trackHashmap, setTrackHashmap] = useState<Record<number, TrackInfo>>({})
  const [dataFetched, setDataFetched] = useState(false);
  // const selectedTrack = recommendationInputData.data;

  //fetches for the recommended songs based on user selected song
  useEffect(() => {
    console.log("inside useeffect")
    console.log(selectedTrack)
    if (selectedTrack !== undefined) {
      fetchSongs(selectedTrack[0]).then(response => {
        setTrackHashmap(response.tracks.reduce((hashmap, track, i) => {
          hashmap[i] = {
            name: track.name,
            uri: track.uri,
            id: track.id,
            albumUrl: track.album.images[1].url,
          }
          return hashmap;
        }, {} as Record<number, TrackInfo>))
        console.log(trackHashmap)
        setDataFetched(true)
      })
    }
  }, [selectedTrack])



  return (
    <div className="generated-page">
    <motion.div className="body">
      <div className="main-container">
        <div className="input-track-container">
        <div className="input-track-overlay">
                <div className="title">{selectedTrack? selectedTrack[0] : "error"}</div>
                <img
                  className="track-image"
                  src={selectedTrack? selectedTrack[3]: "error"}
                  style={{ width: "100px", height: "100px" }}
                  alt="album cover"
                />
              </div>
        </div>

        <div className="recommended-tracks-container">
        {dataFetched && (
  <>
    {Object.values(trackHashmap).map((track) => (
      <div key={track.id} className="recommended-track-container">
        <div className="title">{track.name}</div>
        <img
          className="track-image"
          src={track.albumUrl}
          style={{ width: "100px", height: "100px" }}
          alt="album cover"
        />
      </div>
    ))}
  </>
)}
        </div>

      
        <NavButton nextPage="/" displayedText="Submit" proceedToNextPage={true} onClickRejection={() => {}}/>
        <div className="person-container-small">
          <PersonComponent handleHeadClick={() => {}} headClicked={false} disabledHover={true}/>
        </div>
      </div>
    </motion.div>
  </div>
  )
}
