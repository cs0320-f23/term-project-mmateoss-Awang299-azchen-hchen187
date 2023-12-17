import React, { useState, useEffect, useInsertionEffect, useRef } from "react";

import "./GenerationPage.css";
import "../../components/home/Person.css";
import LyricsGame from "../../components/input/LyricsGame";
import { useAppContext } from "../../components/input/ContextProvider";
import { LyricLine } from "../../components/input/Types";
import PersonComponent from "../../components/home/PersonComponent";
import NavButton from "../../components/button/NavButton";

const mockLyricsResponse: LyricLine[] = [
  {
    startTime: 16,
    learningLyric: "She was all, that I could see",
    nativeLyric: "Ella era todo, lo que podía ver",
  },
  {
    startTime: 23,
    learningLyric: "She was all that was in front of me",
    nativeLyric: "Ella era todo lo que estaba frente a mí",
  },
  { startTime: 27, learningLyric: "", nativeLyric: "" },
  {
    startTime: 32,
    learningLyric: "Try to climb the mountain peaks",
    nativeLyric: "Intenta escalar las cimas de las montañas",
  },
  {
    startTime: 39,
    learningLyric: "What if I only ever reach the sea?",
    nativeLyric: "¿Y si solo llego al mar?",
  },
  {
    startTime: 43,
    learningLyric: "Would you stay awake and wait for me?",
    nativeLyric: "¿Te quedarías despierto y esperarías por mí?",
  },
  { startTime: 47, learningLyric: "", nativeLyric: "" },
  {
    startTime: 50,
    learningLyric: "Hold me under until I see her light",
    nativeLyric: "Manténme bajo el agua hasta que vea su luz",
  },
  {
    startTime: 54,
    learningLyric: "Take it easy on my eyes",
    nativeLyric: "Sé suave con mis ojos",
  },
  {
    startTime: 58,
    learningLyric: "Take it easy on my eyes",
    nativeLyric: "Sé suave con mis ojos",
  },
  { startTime: 63, learningLyric: "", nativeLyric: "" },
];

function GenerationPage() {
  const { token, difficulty, selectedTrack, nativeLanguage, songLanguage} = useAppContext();
  const [score, setScore] = useState(0);
  const [trackUri, setTrackUri] = useState("");
  const [lyrics, setLyrics] = useState<LyricLine[] | null>(null);

  const mockTrackUri = "spotify:track:2E0Lr1ecydv5MjTYYM0WhN";

  const fetchLyrics = async (trackId: string, nativeLanguage: string) => {
    const lyricsObject = await fetch(
      `http://localhost:3232/getLyrics?SpotifyTrackID=${trackId}&toLanguage=${nativeLanguage}&fromLanguage=${songLanguage}`
    );
    const lyricsJson = await lyricsObject.json();

    console.log("result", lyricsJson.Result);
    if (lyricsJson.Result) {
      const lyricsArr = lyricsJson.Message;
      let songLyrics: LyricLine[] = [];
      lyricsArr.map((lyric: string[]) =>
        songLyrics.push({
          startTime: parseInt(lyric[0]),
          learningLyric: lyric[1],
          nativeLyric: lyric[2],
        })
      );

      setLyrics(songLyrics);
    } else {
      console.error("error getting lyrics");
    }
  };

  useEffect(() => {
    fetchLyrics(selectedTrack[2], nativeLanguage);
    setTrackUri(`spotify:track:${selectedTrack[2]}`);
    console.log("testing");
    console.log(selectedTrack[2]);
    console.log(lyrics);
    console.log("end");
  }, []);

  return (
    <div className="generation-page">
      <div className="generation-page-container">
        <div className="left">
          {/* {console.log('selectedTrack')} */}
          <img
            src={selectedTrack[3]}
            alt="album cover"
            style={{ height: "15rem", width: "15rem" }}
          />
          {/* <img
            src="https://i.scdn.co/image/ab67616d00001e0238876c52ff708856ae680d7e"
            alt="album cover"
            style={{ height: "15rem", width: "15rem" }}
          /> */}
          <p>
            {selectedTrack[0]}
          </p>
          <div className="person-container-small">
            <PersonComponent
              handleHeadClick={() => {}}
              headClicked={false}
              disabledHover={true}
            />
          </div>
        </div>
        <div className="middle">
          {/* <LyricsGame
            token={token}
            trackUri={trackUri}
            lyrics={mockLyricsResponse}
            difficulty={"medium"}
            score={score}
            setScore={setScore}
          /> */}
          {lyrics && (
            <LyricsGame
              token={token}
              trackUri={trackUri}
              lyrics={lyrics}
              difficulty={difficulty}
              score={score}
              setScore={setScore}
            />
          )}
        </div>

        <div className="right">
          <p>Score: {score} </p>
          <NavButton
            nextPage="/result"
            displayedText="Leave"
            proceedToNextPage={true}
            onClickRejection={() => {}}
          />
        </div>
      </div>
    </div>
  );
}

export default GenerationPage;
