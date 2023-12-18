import React, { useEffect, useState } from 'react'
import { motion } from 'framer-motion';
import NavButton from '../../components/button/NavButton';
import PersonComponent from '../../components/home/PersonComponent';
import { useAppContext } from '../../components/input/ContextProvider';
import { recommendationOutputData } from '../../mock/MockedData';
import GeneratedTrackComponent from '../../components/result/GeneratedTrackComponent';

import './GeneratedPlaylistPage.css';
import '../../components/home/Person.css';
import { RecommendationOutputData, TrackInfo } from '../../components/interfaces/Interface';

//async method to make backend api calls to get the recommended songs
async function fetchSongs(selectedTrackTitle: string) : Promise<RecommendationOutputData> {

  let openParen = /\(/gi;
  let closeParen = /\)/gi;
  let andSymbol = /&/gi;
  let title = selectedTrackTitle.replace(openParen, "%28").replace(closeParen, "%29").replace(andSymbol, "%26");

  const limit = 5;
  const variability = 0.2;
  const tokenObject = await fetch("http://localhost:3232/token");
  const tokenJson = await tokenObject.json();
  const token = tokenJson.token;
  const serverInput = "recommendation?allNames=" + title + "&token=" + token + "&variability=" + variability + "&limit=" + limit;
  console.log(serverInput)
  const fetched = await fetch("http://localhost:3232/" + serverInput);
  const dataObject = await fetched.json();
  return dataObject;
}

//method to call the mock data
async function mockFetchSongs(selectedTrackTitle:string) : Promise<RecommendationOutputData> {
  return recommendationOutputData;
}


//main component of the generatedplaylist page
export default function GeneratedPlaylistPage() {
  const { selectedTrack} = useAppContext();
  const [trackHashmap, setTrackHashmap] = useState<Record<number, TrackInfo>>({})
  const [dataFetched, setDataFetched] = useState(false);
  const [displayRecs, setDisplayRecs] = useState(false);
  const [displayWarning, setDisplayWarning] = useState(false);
  const [fieldsPopulated, setFieldsPopulated] = useState(false);
  const [initialSelectedTrack, setInitialSelectedTrack] = useState("");
  const inputTrack = document.getElementById("input-track");
  //const selectedTrack = recommendationInputData.data;

  //method to handle when next page conditions arent met
  const handleButtonRejection = () => {
    setDisplayWarning(true)
  }

  //useeffect to check if fields are populated and initializes the usestate to display 
  //the original song
  useEffect(() => {
    Object.values(trackHashmap).some((track : TrackInfo) => {
      if (selectedTrack[0] === track.name) {
        setFieldsPopulated(true)
      }})
    
    if (initialSelectedTrack === "") {
      setInitialSelectedTrack(selectedTrack[0])
    }
  }, [selectedTrack])

  //fetches for the recommended songs based on user selected song
  useEffect(() => {
      fetchSongs(selectedTrack[0]).then(response => {
        setTrackHashmap(response.tracks.reduce((hashmap, track, i : number) => {
          if (i < 5) {
            hashmap[i] = {
              name: track.name,
              uri: track.uri,
              id: track.id,
              albumUrl: track.album.images[1].url,
            }
          }
          return hashmap;
        }, {} as Record<number, TrackInfo>))
        setDataFetched(true)
      })
  }, [])


  //timeout to add buffer for displaying the recommended songs
  setTimeout(() => {
    if (inputTrack !== null) {
      inputTrack.classList.add('hidden');
      setDisplayRecs(true);
    }
  }, 1000);

  //returns the components
  return (
    <div className="generated-page">
      <motion.div className="body">
        <div className="main-container">
          <div className="boom-box-background"></div>
          <div className="input-track-container-wrapper" id="input-track">
          <div className="input-track-container">
            <div className="input-track-overlay">
              <div className="title" style={{color: "black"}}>
                {(selectedTrack.length!==0) ? selectedTrack[0] : "error"}
              </div>
              <img
                className="track-image"
                src={(selectedTrack.length!==0) ? selectedTrack[3] : "error"}
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
