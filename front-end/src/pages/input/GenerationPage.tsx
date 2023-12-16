import React, { useState, useEffect, useInsertionEffect, useRef } from 'react';

import './GenerationPage.css';
import '../../components/home/Person.css';
import LyricsGame from '../../components/input/LyricsGame';
import { useAppContext } from '../../components/input/ContextProvider';
import { LyricLine } from '../../components/input/Types';

const mockLyricsResponse : LyricLine[] = [
  {startTime: 16, learningLyric: "She was all, that I could see", nativeLyric: "Ella era todo, lo que podía ver"},
  {startTime: 23, learningLyric: "She was all that was in front of me", nativeLyric: "Ella era todo lo que estaba frente a mí"},
  {startTime: 27, learningLyric: "", nativeLyric: ""},
  {startTime: 32, learningLyric: "Try to climb the mountain peaks", nativeLyric: "Intenta escalar las cimas de las montañas"},
  {startTime: 39, learningLyric: "What if I only ever reach the sea?", nativeLyric: "¿Y si solo llego al mar?"},
  {startTime: 43, learningLyric: "Would you stay awake and wait for me?", nativeLyric: "¿Te quedarías despierto y esperarías por mí?"},
  {startTime: 47, learningLyric: "", nativeLyric: ""},
  {startTime: 50, learningLyric: "Hold me under until I see her light", nativeLyric: "Manténme bajo el agua hasta que vea su luz"},
  {startTime: 54, learningLyric: "Take it easy on my eyes", nativeLyric: "Sé suave con mis ojos"},
  {startTime: 58, learningLyric: "Take it easy on my eyes", nativeLyric: "Sé suave con mis ojos"},
  {startTime: 63, learningLyric: "", nativeLyric: ""},
];



function GenerationPage() {

  const { token, difficulty } = useAppContext();
  const [trackUri, setTrackUri] = useState(`spotify:track:2E0Lr1ecydv5MjTYYM0WhN`);

  return (
    <div className="generation-page">
      <LyricsGame token={token} trackUri={trackUri} lyrics={mockLyricsResponse} difficulty={"medium"}/>
    </div>
  );
}

export default GenerationPage;
