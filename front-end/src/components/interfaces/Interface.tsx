
export interface SongData {
    title: string,
    artist: string,
    trackID: string,
    albumUrl: string,
  }

export interface RecommendationOutputData {
  seeds: Seed[];
  tracks: Track[];
}

export interface TrackInfo {
  id: string;
  uri: string;
  name: string;
  albumUrl: string;
}

interface Seed {
  afterFilteringSize: number;
  afterRelinkingSize: number;
  href: string;
  id: string;
  initialPoolSize: number;
  type: string; // This might be a more specific type based on the actual data
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