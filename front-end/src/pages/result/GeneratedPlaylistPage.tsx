import React, { MouseEventHandler, useEffect, useState } from 'react'
import { motion } from 'framer-motion';

import './GeneratedPlaylistPage.css';
import '../../components/home/Person.css';
import NavButton from '../../components/button/NavButton';
import PersonComponent from '../../components/home/PersonComponent';
import { useAppContext } from '../../components/input/ContextProvider';
import { recommendationInputData, recommendationOutputData } from '../../mock/MockedData';
import { RecommendationOutputData, TrackInfo } from '../../components/interfaces/Interface';
import GeneratedTrackComponent from '../../components/result/GeneratedTrackComponent';


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
  const { selectedTrack} = useAppContext();
  const [trackHashmap, setTrackHashmap] = useState<Record<number, TrackInfo>>({})
  const [dataFetched, setDataFetched] = useState(false);
  const [displayRecs, setDisplayRecs] = useState(false);
  const [displayWarning, setDisplayWarning] = useState(false);
  const [fieldsPopulated, setFieldsPopulated] = useState(false);
  const [initialSelectedTrack, setInitialSelectedTrack] = useState("");
  // const [chosenTrack, setChosenTrack] = useState("");
  const inputTrack = document.getElementById("input-track");
  //const selectedTrack = recommendationInputData.data;

  const handleButtonRejection = () => {
    setDisplayWarning(true)
  }

  useEffect(() => {
    if (selectedTrack !== undefined) {
      Object.values(trackHashmap).some((track : TrackInfo) => {
        if (selectedTrack[0] === track.name) {
          setFieldsPopulated(true)
        }})
      
      if (initialSelectedTrack === "") {
        setInitialSelectedTrack(selectedTrack[0])
      }
    }
  }, [selectedTrack])

  //fetches for the recommended songs based on user selected song
  useEffect(() => {
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
        setDataFetched(true)
      })
    }
  }, [])


  setTimeout(() => {
    if (inputTrack !== null) {
      inputTrack.classList.add('hidden');
      setDisplayRecs(true);
    }
  }, 1600);

  return (
    <div className="generated-page">
      <motion.div className="body">
        <div className="main-container">
          <div className="boom-box-background"></div>
          <div className="input-track-container-wrapper" id="input-track">
          <div className="input-track-container">
            <div className="input-track-overlay">
              <div className="title" style={{color: "black"}}>
                {selectedTrack ? selectedTrack[0] : "error"}
              </div>
              <img
                className="track-image"
                src={selectedTrack ? selectedTrack[3] : "error"}
                style={{ width: "120px", height: "120px" }}
                alt="album cover"
              />
            </div>
          </div>
          </div>

          <div className="boom-box-svg"></div>
          <div className="generation-text-container">
            Here are your generated songs based on 
            <br/>
            <span style={{color: "var(--green)"}}>
            {initialSelectedTrack? initialSelectedTrack: "error"}
            </span>!
            <br/>
            Select one to play with:
          </div>

          <div className="recommended-tracks-container">
            {dataFetched && displayRecs && (
              <>
                {Object.values(trackHashmap).map((track : TrackInfo) => (
                  <GeneratedTrackComponent track={track}/>
                ))}
              </>
            )}
          </div>

          <NavButton
            nextPage="/"
            displayedText="Submit"
            proceedToNextPage={fieldsPopulated}
            onClickRejection={handleButtonRejection}
          />
          <div className="person-container-small">
            <div className={`${displayWarning ? "warning-message-container" : "no-display"}`}>Please input a song!</div>
            <PersonComponent
              handleHeadClick={() => {}}
              headClicked={false}
              disabledHover={true}
            />
          </div>
        </div>
      </motion.div>
    </div>
  );
}
