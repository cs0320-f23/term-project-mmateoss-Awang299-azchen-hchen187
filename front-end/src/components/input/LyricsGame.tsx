import React, { useEffect, useState } from "react";

import SpotifyPlayer, { CallbackState } from 'react-spotify-web-playback';

import "./LyricsGame.css"
import LyricsHistory from "./LyricsHistory";
import { LyricLine, HistoryLyric, GameLyric } from "./Types"
import { useAppContext } from "./ContextProvider";

export interface PlayerProps {
  trackUri : string,
  token : string,
  lyrics: LyricLine[],
  difficulty: string,
  score: number,
  setScore: (updateFunction: (prevScore: number) => number) => void;
}

export default function LyricsGame({trackUri, token, lyrics, difficulty, score, setScore }: PlayerProps) {
  const  {selectedTrack } = useAppContext();

  // game controls
  const [player, setPlayer] = useState<Spotify.Player | null>(null);
  const [currentPosition, setCurrentPosition] = useState<number>(0);
  const [lyricNumber, setLyricNumber] = useState<number>(-1);
  const [currentInterval, setCurrentInterval] = useState<number[]>([-1, -1]);
  const [play, setPlay] = useState<boolean>(true);

  // current game variables
  const [answer, setAnswer] = useState("");
  const [currentGameLyric, setCurrentGameLyric] = useState<GameLyric>({ beginning: "", answer: "", end: "" });
  const [userGuess, setUserGuess] = useState("");
  const [attempts, setAttempts] = useState(0);

  // overall game variables
  const [history, setHistory] = useState<HistoryLyric[]>([]);
  const [gameOver, setGameOver] = useState(false);

  useEffect(() => {
    initializeGame();
  }, []);

  useEffect(() => {
    const intervalId = setInterval(() => {
      player?.getCurrentState().then((state) => {
        if (state) {
          setCurrentPosition(Math.floor(state.position / 1000));
        }
      });
    }, 1000);

    return () => clearInterval(intervalId);
  }, [player]);

  useEffect(() => {
    if (currentPosition >= currentInterval[1]) {
      setPlay(false);
      player?.seek(currentInterval[0] * 1000).then(() => {
        console.log("changed position");
      });
    }
  }, [currentPosition]);

  const initializeGame = () => {
    let newLyricNumber = lyricNumber + 1;

    while (lyrics[newLyricNumber].learningLyric === "") {
      newLyricNumber += 1;
    }

    const startTime = lyrics[newLyricNumber].startTime;
    const endTime = lyrics[newLyricNumber + 1].startTime;

    setLyricNumber(newLyricNumber);
    setCurrentInterval([startTime, endTime - 1]);

    generateAnswer(newLyricNumber);
    setPlay(true);
  };

  const generateAnswer = (generateLyricNumber: number) => {
    const lyric = lyrics[generateLyricNumber].learningLyric;
    const lyricArr = lyric.split(" ");
    console.log(lyricArr);
    let answerLength = 1;

    switch (difficulty) {
      case "easy":
        answerLength = 1;
        break;
      case "medium":
        answerLength = Math.floor(lyricArr.length / 3) + 1;
        break;
      case "hard":
        answerLength = lyricArr.length;
        break;
    }

    const validIdxs = lyricArr.length - answerLength + 1;
    const randomIdx = Math.floor(Math.random() * validIdxs);
    const newAnswer = lyricArr.slice(randomIdx, randomIdx + answerLength).join(" ");

    const answerIdx = lyric.indexOf(newAnswer);
    const beginning = lyric.substring(0, answerIdx);
    const end = lyric.substring(answerIdx + newAnswer.length);

    console.log({ beginning, answer: newAnswer, end });
    setCurrentGameLyric({ beginning, answer: newAnswer, end });
    setAnswer(newAnswer);
  };

  const cleanString = (str: string) => {
    return str.toLowerCase().replace(/[.,\/#!$%\^&\*;:{}=\-_`~()?]/g, "");
  };

  const handleSubmitAnswer = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const newAttempts = attempts + 1;
    const guessCorrect = cleanString(userGuess) === cleanString(answer);
    if (guessCorrect || newAttempts >= 3) {
      let newLyricNumber = lyricNumber + 1;

      if (newLyricNumber + 1 < lyrics.length) {
        const soundLyricNumber = lyricNumber + 1;

        while (lyrics[newLyricNumber].learningLyric === "") {
          newLyricNumber += 1;
        }

        const startTime = lyrics[newLyricNumber].startTime;
        const endTime = lyrics[newLyricNumber + 1].startTime;

        player?.seek(lyrics[soundLyricNumber].startTime * 1000);
        let newHistoryLyric: HistoryLyric;

        if (!guessCorrect) {
          newHistoryLyric = { lyric: currentGameLyric, userGuess: userGuess, correct: false };
        } else {

          setScore((prevScore: number) => prevScore + Math.floor(answer.length / newAttempts));
          // const points  = await calculateScore(newAttempts)
          // if (points) {
          //   setScore((prevScore) => prevScore + points)
          // }
          
          newHistoryLyric = { lyric: currentGameLyric, userGuess: userGuess, correct: true };
        }

        setHistory((prevHistory) => prevHistory.concat(newHistoryLyric));
        setAttempts(0);
        setLyricNumber(newLyricNumber);
        setCurrentInterval([startTime, endTime - 1]);
        generateAnswer(newLyricNumber);
      } else {
        setGameOver(true);
      }
    } else {
      setAttempts(newAttempts);
    }
    setUserGuess("");
  };

  const calculateScore = async (attempts : number) => {
    const scoreObject = await fetch(`http://localhost:3232/getScore?spotifyID=${selectedTrack[2]}&correctWord=${answer}&guessWord=${userGuess}`);
    const scoreJson = await scoreObject.json();
    if (scoreJson.result) {
      return parseInt(scoreJson.message)
    } else {
      console.log("incorrect score")
    }
  }

  return (
    <div className="lyrics-game" style={{ justifyContent : gameOver ? "flex-start" : "" }}>
      <h2>Fill In the Lyric</h2>
      {!gameOver && <div className="game">
        <LyricsHistory history={history} result={false}/>
        {currentGameLyric && lyricNumber >= 0 && (
          <InputLyric
            gameLyric={currentGameLyric}
            handleSubmitAnswer={handleSubmitAnswer}
            userGuess={userGuess}
            setUserGuess={setUserGuess}
            difficulty={difficulty}
            nativeLyric={lyrics[lyricNumber].nativeLyric}
          />
        )}

        <div className="player">
          <SpotifyPlayer
            token={token}
            uris={trackUri ? [trackUri] : []}
            getPlayer={(player: Spotify.Player) => setPlayer(player)}
            play={play}
            callback={(state: CallbackState) => setPlay(state.isPlaying)}
            hideAttribution
            hideCoverArt
            styles={{
              bgColor: "black",
              sliderHandleColor: "white",
              color: "white",
            }}
          />
        </div>
      </div>}
      {gameOver && 
       <LyricsHistory history={history} result={true}/>
      }
      
    </div>
  );
}

interface inputLyricProps {
  gameLyric : GameLyric,
  handleSubmitAnswer : (e : React.FormEvent<HTMLFormElement>) => void,
  userGuess : string,
  setUserGuess : (userGuess: string) => void,
  difficulty: string,
  nativeLyric: string,
}

const InputLyric = ({ gameLyric, handleSubmitAnswer, userGuess, setUserGuess, difficulty, nativeLyric }: inputLyricProps) => {
  let inputWidth = 0;
  switch(difficulty) {
    case "easy" : inputWidth = gameLyric.answer.length; break
    case "medium" : inputWidth = gameLyric.answer.length / 1.5; break
    case "hard" : inputWidth = gameLyric.answer.length / 2; break
  }

  return (
    <>
      <div className="input">
        <div className="input-lyric">
          <p>{gameLyric.beginning}</p>
          <form onSubmit={handleSubmitAnswer}>
            <input
              className="user-input"
              type="text"
              value={userGuess}
              onChange={(e) => setUserGuess(e.target.value)}
              style={{ width: inputWidth + "rem" }}
            />
          </form>
          <p>{gameLyric.end}</p>
        </div>
        <p> {nativeLyric} </p>
      </div>
    </>
  );
};
