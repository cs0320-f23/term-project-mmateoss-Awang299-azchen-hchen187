import React, { useEffect, useState } from "react";

import SpotifyPlayer, { CallbackState } from 'react-spotify-web-playback';

import "./LyricsGame.css"
import { collapseTextChangeRangesAcrossMultipleVersions } from "typescript";

type LyricLine = [number, string];

const mockLyricsResponse : LyricLine[] = [
  [16, "She was all that I could see"],
  [23, "She was all that was in front of me"],
  [27, ""],
  [32, "Try to climb the mountain peaks"],
  [39, "What if I only ever reach the sea?"],
  [43, "Would you stay awake and wait for me?"],
  [47, ""],
  [50, "Hold me under until I see her light"],
  [54, "Take it easy on my eyes"],
  [58, "Take it easy on my eyes"],
  [63, ""],
]

interface PlayerProps {
    trackUri : string,
    token : string,
}

export default function LyricsGame({trackUri, token}: PlayerProps) {
  const [player, setPlayer] = useState<Spotify.Player | null>(null);
  const [currentPosition, setCurrentPosition] = useState<number>(0)
  const [lyricNumber, setLyricNumber] = useState<number>(-1)
  const [currentInterval, setCurrentInterval] = useState<number[]>([0,0])
  const [play, setPlay] = useState<boolean>(true)
  const [answer, setAnswer] = useState('');

  const handlePlayerStateChange = (state : CallbackState) => {
    console.log()
    setPlay(state.isPlaying)
  }

  useEffect(() => {
    const intervalId = setInterval(() => {
      player?.getCurrentState().then(state => {
        if (state) {
          setCurrentPosition(Math.floor(state.position / 1000));
        }
      });
    }, 1000);
  
    return () => clearInterval(intervalId);
  }, [player]);

  useEffect(() => {
    if (currentPosition < currentInterval[0] || currentPosition >= currentInterval[1]) {
      setPlay(false)
      player?.seek(currentInterval[0] * 1000).then(() => {console.log("changed position")})
    }
  }, [currentPosition])

  const handleSubmitAnswer = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (answer === "correct") {
      let newLyricNumber = lyricNumber + 1;

      while (mockLyricsResponse[newLyricNumber][1] === "") {
        newLyricNumber += 1
      }
      
      const startTime = mockLyricsResponse[newLyricNumber][0]
      const endTime = mockLyricsResponse[newLyricNumber+1][0]

      console.log(startTime, endTime)
      
      player?.seek(startTime * 1000)

      console.log(newLyricNumber, currentInterval)
      
      setLyricNumber(newLyricNumber)
      setCurrentInterval([startTime, endTime-1])
    }
  }

  return (
    <div className="lyrics-game">
      {lyricNumber >= 0 && <p> {mockLyricsResponse[lyricNumber][1]} </p>}
      <button onClick={() => setPlay(true)}>
        Play
      </button>

      <form onSubmit={handleSubmitAnswer}>
        <input type="text" value={answer} onChange={(e) => setAnswer(e.target.value)} placeholder="Enter your answer" />
        <button type="submit">Submit</button>
      </form>

      <p>Time: {currentPosition}s</p>

      <div className="player">
        <SpotifyPlayer
          token={token}
          uris={trackUri ? [trackUri] : []}
          styles={{ bgColor: "white" }}
          getPlayer={(player : Spotify.Player) => setPlayer(player)}
          play={play}
          callback={handlePlayerStateChange}
          hideAttribution={false}
        />
      </div>
    </div>
  );
}
