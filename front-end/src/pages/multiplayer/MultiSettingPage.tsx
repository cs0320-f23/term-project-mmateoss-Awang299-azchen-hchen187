import React from 'react'
import NavButton from '../../components/button/NavButton'
import LanguageDropdown from '../../components/input/LanguageDropdown'

import "./MultiSettingPage.css"

export default function MultiSettingPage() {


  return (
    <div className="mp-setting-page">
        <div className="main-container-mp-setting">
            <div className="mp-title">MultiPlayer</div>
            <div className="lang-dropdown-container">
                <LanguageDropdown setSongLang={true}/>
            </div>
            <div className="setup-container">setup avatar</div>
        </div>
        <NavButton
            nextPage="/multi/load"
            displayedText="Next"
            //proceedToNextPage={fieldsPopulated}
            proceedToNextPage={true}
            // onClickRejection={handleButtonRejection}
            onClickRejection={() => {}}
          />
    </div>
  )
}
