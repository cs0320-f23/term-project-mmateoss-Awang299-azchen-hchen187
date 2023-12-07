import React, { useState, useEffect, useInsertionEffect, useRef } from 'react';
import { motion } from 'framer-motion';
import ProgressBar from '../../components/progress/ProgressBar'
import NavButton from '../../components/button/NavButton';
import PersonComponent from '../../components/home/PersonComponent';

import './SongsPage.css';
import '../../components/home/Person.css';
import { Container, Form } from 'react-bootstrap';
import { mockGetSongs } from '../../mock/MockAPICalls';
import TrackSearchResult from '../../components/input/TrackSearchResult';
import { SongData } from '../../components/interfaces/Interface';
import { useAppContext } from '../../components/input/ContextProvider';

async function fetchSongs(input: string) : Promise<string> {
  //todo: update serverinput string
  const serverInput = "getSongs?" + "whateverparemeter=" + input;
  const fetched = await fetch("http://localhost:3232/" + serverInput);
  const dataObject = await fetched.json();
  return dataObject;
}

async function fetchMockedSongs(input: string) {
  const dataObject = mockGetSongs(input);
  return dataObject;
}


function SongsPage() {
  const { selectedTrack, chooseTrack } = useAppContext();
  const [search, setSearch] = useState("")
  const [searchResults, setSearchResults] = useState<SongData[]>([])
  const [isActive, setIsActive] = useState(false);

  const handleSearchClick = () => {
    setIsActive(true);
  }

  //displays selected song
  useEffect(() => {
    console.log("new track chosen")
    console.log(selectedTrack)
  }, [selectedTrack])

  //handles searching of songs
  useEffect(() => {
    if (!search) return setSearchResults([]);

    let cancel = false;
    fetchMockedSongs(search).then(response => {
      if (cancel) {
        console.log("cancel is true")
        return 
      }
      if (response.result === "success") {
        if (Array.isArray(response.data)) {
          setSearchResults(response.data)
          console.log(searchResults)
        }
      } else {
        //error
      }
    })
    return () => {
      cancel = true;
    }
  }, [search])

  return (
    //initial={{opacity:0}} animate={{opacity: 1}}
    <div className="songs-page">
      <motion.div className="body">
        <div className="main-container">
          <svg
            width="471"
            height="563"
            viewBox="0 0 471 563"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
            className="arrow-svg"
          >
            <path
              d="M2.00001 560.5C217.5 408 277.88 94.9409 197.44 120.47C117 146 287 287.501 462 2.5"
              stroke="#F7E32C"
              stroke-width="6"
              className="animate-arrow"
            />
            <path
              d="M462 2.5C462 2.5 410 57 362.5 29.5M462 2.5C462 2.5 435 66 468 112.5"
              stroke="#F7E32C"
              stroke-width="6"
              className="animate-arrow-head"
            />
          </svg>

          <div className="instructions-container">
            Step 1:
            <br></br>
            select the song you want to learn from using the search above
          </div>
          <Container className="search-container">
            <div
              className={`search-bar ${isActive ? "active" : ""}`}
              onClick={handleSearchClick}
            >
              <Form.Control
                type="search"
                placeholder="Search Songs/Artists"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="search-input"
              />
            </div>
            <div className="track">
              <div className="track-container-wrapper">
                {searchResults.map((track) => (
                  <TrackSearchResult
                    track={track}
                    key={track.trackID}
                    chooseTrack={chooseTrack}
                  />
                ))}
              </div>
            </div>

            {/* <div style={{ color: "white" }}>Bottom</div> */}
          </Container>

          <div className="selected-track-container">
            {selectedTrack ? (
              // Render something when a track is selected
              <div className="selected-track-overlay">
                <div className="displayed-title">{selectedTrack.title}</div>
                <img
                  className="track-image"
                  src={selectedTrack.albumUrl}
                  style={{ width: "250px", height: "250px" }}
                  alt="album cover"
                />
              </div>
            ) : null}
            <div className="album-default-container">
              <div className="song-text-container">Your Selected Track</div>
              <div className="album-lineart-container">
                <div className="arrow-container">
                  <div className="album-cover-arrow" />
                </div>
                <div className="album-cover-box" />
              </div>
            </div>
          </div>

          <NavButton nextPage="/input/metadata" displayedText="Next"/>
          <div className="person-container-small">
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

export default SongsPage;
