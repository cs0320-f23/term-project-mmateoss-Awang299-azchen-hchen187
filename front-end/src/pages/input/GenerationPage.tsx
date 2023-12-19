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
    learningLyric: "remember me me me me me me",
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
  const { token, difficulty, selectedTrack, nativeLanguage, songLanguage } =
    useAppContext();
  const [score, setScore] = useState(0);
  const [totalScore, setTotalScore] = useState(0);
  const [trackUri, setTrackUri] = useState("");
  const [lyrics, setLyrics] = useState<LyricLine[] | null>(null);
  const [gameOver, setGameOver] = useState(false);

  const fetchLyrics = async (trackId: string, nativeLanguage: string) => {
    const lyricsObject = await fetch(
      `http://localhost:3232/getLyrics?SpotifyTrackID=${trackId}&toLanguage=${nativeLanguage}&fromLanguage=${songLanguage}`
    );
    const lyricsJson = await lyricsObject.json();

    if (lyricsJson.Result) {
      const lyricsArr = lyricsJson.Message;
      let songLyrics: LyricLine[] = lyricsArr.map((lyric: string[]) => ({
        startTime: parseFloat(lyric[0]),
        learningLyric: lyric[1],
        nativeLyric: lyric[2],
      }));

      setLyrics(songLyrics);
    } else {
      console.error("error getting lyrics");
    }
  };

  useEffect(() => {
    fetchLyrics(selectedTrack[2], nativeLanguage);
    setTrackUri(`spotify:track:${selectedTrack[2]}`);
  }, []);

  return (
    <div className="generation-page">
      <div className="generation-page-container">
        <div className="left">
          <p>
            Score: {score} / {totalScore}{" "}
          </p>
          <div className="person-container-small">
            <div className={`${gameOver ? "game-over-message" : "no-display"}`}>
              Congrats on finishing the song! Take a look at how you did
            </div>
            <PersonComponent
              handleHeadClick={() => {}}
              headClicked={false}
              disabledHover={true}
            />
          </div>
        </div>
        <div className={`middle${!lyrics ? " middle-loading" : ""}`}>
          {lyrics && (
            <LyricsGame
              token={token}
              trackUri={trackUri}
              lyrics={lyrics}
              difficulty={difficulty}
              setScore={setScore}
              setTotalScore={setTotalScore}
              gameOver={gameOver}
              setGameOver={setGameOver}
            />
          )}
          {!lyrics && (
            <h3 style={{}}>Translating Song to {nativeLanguage}...</h3>
          )}
        </div>

        <div className="right">
          <div className="song-info">
            <img
              src={selectedTrack[3]}
              alt="album cover"
              style={{ height: "15rem", width: "15rem" }}
            />
            <p>
              {selectedTrack[0]}{" "}
              {selectedTrack[1] ? "-" + selectedTrack[1] : ""}
            </p>
          </div>

          <NavButton
            nextPage="/result"
            displayedText={gameOver ? "Play More" : "Change Songs"}
            proceedToNextPage={true}
            onClickRejection={() => {}}
          />
        </div>
      </div>
    </div>
  );
}

export default GenerationPage;
