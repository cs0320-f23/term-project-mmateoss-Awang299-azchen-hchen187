import React from 'react'

import "./AboutPage.css"

//main component of the about page
export default function AboutPage() {
  return (
    <div className="about-page">
      <div className="right-blob"></div>
      <div className="main-container">
        <div className="mission-container">
          <span style={{fontWeight: "bold", fontSize: "23pt", lineHeight:"50px"}}>Our mission:</span>
          <br/>
          SpotiDuo adds a fun twist to language learning by blending the joy of music with the art of language acquisition. 
          <br/>
          As you play, listen, and challenge your lyrical knowledge, SpotiDuo crafts an interactive experience that fosters 
          a deeper cultural and linguistic appreciation. Join us on this adventure of discovery, all it takes is just one song...
        </div>

        <div className="about-us-container">
          <span style={{fontWeight: "bold", fontSize: "23pt", lineHeight:"50px"}}>Who we are:</span>
          <br/>
          Made with the blood, sweat, and tears of Brown students
          <br/>
          <span style={{fontWeight: "bold"}}>
            <br/>
            Marcel Mateos Salles
            <br/>
            Andrew Chen
            <br/>
            Allen Wang
            <br/>
            Charlene Chen
          </span>
        </div>
        <div className="profile-container">
          <div className="pfp1"/>
          <div className="pfp2"/>
          <div className="pfp3"/>
          <div className="pfp4"/>
        </div>

      </div>
    </div>
  )
}
