export type LyricLine = {
  startTime : number,
  learningLyric : string,
  nativeLyric : string,
}

export type GameLyric = {
  beginning : string,
  answer : string,
  end : string,
}

export type HistoryLyric = {
  lyric : GameLyric,
  userGuess : string,
  correct : boolean,
}

export interface LyricsHistoryProps {
    history : HistoryLyric[],
}
