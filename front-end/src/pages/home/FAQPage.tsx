import React from 'react'
import PersonComponent from '../../components/home/PersonComponent'

import "./FAQPage.css"

//main component of the faq page
export default function FAQPage() {
  return (
    <div className="faq-page">
        <div className="main-container">
            <div className="person-container-faq">
              <PersonComponent handleHeadClick={() => {}} headClicked={false} disabledHover={false}/>
            </div>
            <div className="overview-container">
                Overview of how it works:
                <br/>
                1. Input the song you want to learn
                <br/>
                2. Specify the language you want to learn in the chosen song
                <br/>
                3. Select the language you are most comfortable with {'(your native language)'}
                <br/>
                4. Select the level of difficulty you want your game to be
                <br/>
                5. Play game! The song audio will be played and you just have to fill in the
                missing word/line{'(s)'}. The more accurate you are, the more points you will get!
                <br/> 
                6. Once you finish a round, you can choose to play with other songs we recommend based
                on your initial song and discover new artists!
                <br/>
                7. Have fun!
            </div>
        </div>
    </div>

  )
}
