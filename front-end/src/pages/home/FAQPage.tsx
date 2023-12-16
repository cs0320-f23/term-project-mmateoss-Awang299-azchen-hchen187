import React from 'react'

import "./FAQPage.css"

//main component of the faq page
export default function FAQPage() {
  return (
    <div className="faq-page">
        <div className="main-container">
            <div className="overview-container">
                Overview of how it works:
                <br/>
                Input the song you want to learn
                <br/>
                Specify the language you want to learn in the song chosen
                <br/>
                Select the language you are most comfortable with {'(your native language)'}
                <br/>
                Select the level of difficulty you want your game to be
                <br/>
                Play game! The song audio will be played and you just have to fill in the
                <br/>
                missing words/line{'(s)'}. If you are correct, 
            </div>
        </div>
    </div>

  )
}
