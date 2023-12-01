import { Paper } from '@mui/material'
import React, { useState } from 'react'
import "./GeneratedPlaylistPage.css"

export default function GeneratedPlaylistPage() {
  const [songs, setSongs] = useState(["song1", "song2", "song3"])

  return (
    <div className="generated-playlist-page">
      {songs.map((song) => (
        <Paper className="song-paper" elevation={3}>{song}</Paper>
      ))}
    </div>
  );
}
