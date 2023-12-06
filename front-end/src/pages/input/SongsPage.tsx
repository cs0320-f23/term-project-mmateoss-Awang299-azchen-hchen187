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

interface SongData {
  title: string,
  artist: string,
  trackID: string,
  albumUrl: string,
}

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
  const [search, setSearch] = useState("")
  const [searchResults, setSearchResults] = useState<SongData[]>([])
  const [isActive, setIsActive] = useState(false);

  const handleSearchClick = () => {
    setIsActive(true);
  }

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

          <Container className="search-container">
            <div className={`search-bar ${isActive ? 'active' : ''}`} onClick={handleSearchClick}>
              <Form.Control
                type="search"
                placeholder="Search Songs/Artists"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="search-input"
              />
            </div>
            <div className="flex-grow-1 my-2" style={{overflowY: "auto", color: "white"}}>
              {searchResults.map(track => (
                <TrackSearchResult track={track} key={track.trackID} />
              ))}
            </div>
            <div style={{color: "white"}}>Bottom</div>
          </Container>

          <NavButton nextPage="/input/metadata" />
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
