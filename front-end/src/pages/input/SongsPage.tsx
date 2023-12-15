import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import NavButton from '../../components/button/NavButton';
import PersonComponent from '../../components/home/PersonComponent';
import { Container, Form } from 'react-bootstrap';
import { mockGetSongs } from '../../mock/MockAPICalls';
import TrackSearchResult from '../../components/input/TrackSearchResult';
import { useAppContext } from '../../components/input/ContextProvider';

import './SongsPage.css';
import '../../components/home/Person.css';

//async function to make backend api calls to get the songs
async function fetchSongs(input: string) : Promise<{Result: string, data: string[][]}> {
  const limit = 10;
  const tokenObject = await fetch("http://localhost:3232/token");
  const tokenJson = await tokenObject.json();
  const token = tokenJson.token;
  const serverInput = "getSongs?token=" + token + "&limit=" + limit + "&query=" + input;
  console.log(serverInput)
  const fetched = await fetch("http://localhost:3232/" + serverInput);
  const dataObject = await fetched.json();
  return dataObject;
}

//mock function to get mock data
async function fetchMockedSongs(input: string) {
  const dataObject = mockGetSongs(input);
  return dataObject;
}

//main component of the songs page
function SongsPage() {
  const { selectedTrack, chooseTrack } = useAppContext();
  const [search, setSearch] = useState("")
  const [searchResults, setSearchResults] = useState<string[][]>([])
  const [isActive, setIsActive] = useState(false);
  const [fieldsPopulated, setFieldsPopulated] = useState(false);
  const [displayWarning, setDisplayWarning] = useState(false);

  //method to toggle the search bar
  const handleSearchClick = () => {
    setIsActive(true);
  }

  //method to handle when next page conditions aren't satisfied
  const handleButtonRejection = () => {
    setDisplayWarning(true)
  }

  //resetting the selectedTrack upon page load
  useEffect(() => {
    console.log("page renders")
    chooseTrack([])
  }, [])

  //updates fieldsPopulated boolean
  useEffect(() => {
    if (selectedTrack.length !== 0) {
      setFieldsPopulated(true)
    } else {
      setFieldsPopulated(false)
    }
  }, [selectedTrack])

  //handles searching of songs
  useEffect(() => {
    if (!search) return setSearchResults([]);

    let cancel = false;
    fetchSongs(search).then(response => {
      console.log(response)
      if (cancel) {
        return 
      }
      if (response.Result === "Success") {
        setSearchResults(response.data)
      } else {
        console.log(response)
      }
    })
    return () => {
      cancel = true;
    }
  }, [search])

  //returns the component
  return (
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
                    key={track[2]}
                    chooseTrack={chooseTrack}
                  />
                ))}
              </div>
            </div>
          </Container>

          <div className="selected-track-container">
            {(selectedTrack.length!==0) ? (
              <div className="selected-track-overlay">
                <div className="displayed-title">{selectedTrack[0]}</div>
                <img
                  className="track-image"
                  src={selectedTrack[3]}
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

          <NavButton nextPage="/input/settings" displayedText="Next" proceedToNextPage={fieldsPopulated} onClickRejection={handleButtonRejection}/>
          <div className="person-container-small">
            <div className={`${displayWarning ? "warning-message-container" : ""}`}>Please input a song!</div>
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
