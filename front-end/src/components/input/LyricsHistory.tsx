import React, { useRef, useEffect } from 'react'
import "./LyricsHistory.css"

import { HistoryLyric } from "./Types"

interface LyricsHistoryProps {
    history : HistoryLyric[],
    result : boolean,
}

export default function LyricsHistory({ history, result } : LyricsHistoryProps) {

    const historyEndRef = useRef<HTMLDivElement | null>(null);
    useEffect(() => {
      if (historyEndRef.current && historyEndRef.current.parentElement) {
        const container = historyEndRef.current.parentElement;
        container.scrollTop = container.scrollHeight;
      }
    }, [history]);

    return (
      <div className={result ? "lyrics-history result" : "lyrics-history"}>
        {history.map((historyLyric) => (
          <div className="lyric">
            <p>{historyLyric.lyric.beginning + " "}</p>
            <p className={historyLyric.correct ? "guess guess-correct" : "guess guess-incorrect"}>
              {historyLyric.userGuess}
            </p>
            <p>{historyLyric.lyric.end}</p>
          </div>
        ))}
        <div ref={historyEndRef} />
      </div>
    );
}
