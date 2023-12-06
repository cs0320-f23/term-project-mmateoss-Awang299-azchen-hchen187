import React from 'react'

interface SongData {
    title: string,
    artist: string,
    trackID: string,
    albumUrl: string,
  }

export default function TrackSearchResult({track} : {track : SongData}) {
  return (
    <div className="d-flex m-2 align-items-center">
        <img src={track.albumUrl} style={{ height: "64px", width: "64px" }} />
        <div className="ml-3">
            <div>{track.title}</div>
            <div className="text-muted">{track.artist}</div>
        </div>
    </div>
  )
}
