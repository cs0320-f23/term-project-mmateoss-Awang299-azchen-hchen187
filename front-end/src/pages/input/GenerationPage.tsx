import React, { useState, useEffect, useInsertionEffect, useRef } from 'react';

import './GenerationPage.css';
import '../../components/home/Person.css';
import LyricsGame from '../../components/input/LyricsGame';
import { useAppContext } from '../../components/input/ContextProvider';


function GenerationPage() {

  const {token} = useAppContext();
  const [trackUri, setTrackUri] = useState(`spotify:track:2E0Lr1ecydv5MjTYYM0WhN`);

  return (
    <div className="generation-page">
      <LyricsGame token={token} trackUri={trackUri}/>
    </div>
  );
}

export default GenerationPage;
