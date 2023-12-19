
//SongData interface to store important information about the track
export interface SongData {
    title: string,
    artist: string,
    trackID: string,
    albumUrl: string,
  }

//OutputData interface to process the returned json from backend
export interface RecommendationOutputData {
  seeds: Seed[];
  tracks: Track[];
}

//TrackInfo interface to store information about recommended tracks
export interface TrackInfo {
  id: string;
  uri: string;
  name: string;
  albumUrl: string;
}

//More narrow interface definitions for the returned recommendation json
interface Seed {
  afterFilteringSize: number;
  afterRelinkingSize: number;
  href: string;
  id: string;
  initialPoolSize: number;
  type: string;
}

interface Track {
  duration_ms: number;
  explicit: boolean;
  href: string;
  id: string;
  uri: string;
  track_number: number;
  preview_url: string;
  name: string;
  album: Album;
}

interface Album {
  images: Image[];
}

interface Image {
  url: string;
  height: number;
  width: number;
}

//data type for leaderboard data
export interface UserData {
  username: string;
  profileUrl: string;
  score: number;
}

export interface LeaderboardData {
  [key: number]: UserData;
}