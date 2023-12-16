import React from 'react'
import PersonComponent from '../../components/home/PersonComponent'

import "./FAQPage.css"

//main component of the faq page
export default function FAQPage() {
  return (
    <div className="faq-page">
        <div className="main-container-faq">
          {/* <div className="z-wrapper">
            <div className="y-wrapper">
              <div className="x-wrapper"></div>
            </div>
          </div> */}
            <div className="person-container-faq">
              <PersonComponent handleHeadClick={() => {}} headClicked={false} disabledHover={false}/>
            </div>
            <div className="overview-container">
                <span style={{fontWeight: "bold", fontSize:"18pt"}}>Overview of how it works:</span>
                <br/>
                <b>1.</b> Input the song you want to learn
                <br/>
                <b>2.</b> Specify the language you want to learn in the chosen song
                <br/>
                <b>3.</b> Select the language you are most comfortable with {'(your native language)'}
                <br/>
                <b>4.</b> Select the level of difficulty you want your game to be
                <br/>
                <b>5.</b> Play game! The song audio will be played and you just have to fill in the
                missing word{'(s)'}/line. The more accurate you are, the more points you will get!
                <br/> 
                <b>6.</b> Once you finish a round, you can choose to play with other songs we recommend based
                on your initial song and discover new artists!
                <br/>
                <b>7.</b> Have fun!
            </div>
        </div>
    </div>

  )
}
