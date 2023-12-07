import React, { useState } from 'react';

import SpotifyPlayer from 'react-spotify-web-playback';

function GenerationPage() {

  const [token, setToken] = useState(`BQC1f8oeLxe3IaMk-FvLXDVyX3mQUl_RRQdsL7J4_N9oiULNewi1utjgafi1tc-wYyaVLG94-oFS79jDHFXAxaHUqZxHyrG6wQwIGJ1nFdcgWhhGQp18_mKBWOjTcKDCySrSwkWCOzOEMSvjMuuc-yuIjwQnrxa9dHAqJZ2gXXyUul-RuIb0XzA8drM_XTYAe4kwkyiUP20

  `)
  const [trackUri, setTrackUri] = useState("spotify:track:5f4Hy5mw5SRaUgXX9c6P5S")

  return (
    <SpotifyPlayer
      token={token}
      play={true}
      uris={[trackUri]}
    />
  );
}

export default GenerationPage;
