import React from 'react'
import PersonComponent from 'src/components/home/PersonComponent'
import LanguageDropdown from 'src/components/input/LanguageDropdown';
import { mockLeaderBoardData } from 'src/mock/MockedData'

import "./LeaderBoardPage.css"


export default function LeaderBoardPage() {
  const leaderboardDataArray = Object.values(mockLeaderBoardData);
  
  return (
    <div className="leaderboard-page">
        <div className="main-container-leader">
          <div className="left-lineart"></div>
          <div className="right-lineart"></div>
          
          <div className="leaderboard-title">Global Leaderboard for 
            <div className="language-select-container">
              <LanguageDropdown setSongLang={false} />
            </div>
          </div>
          <div className="leaderboard-container">
          <table className="leaderboard-table">
            <thead>
              <tr>
                <th></th>
                <th style={{textIndent:"55px"}}>User</th>
                <th>Score</th>
              </tr>
            </thead>
            <tbody>
              {leaderboardDataArray.map((user, index) => (
                <tr key={index}>
                  <td style={{width: "1px", textAlign:"center"}}>{index+1}.</td>
                  <td className="user-td">
                    <img className="pfp-image" src={user.profileUrl} alt={`Profile of ${user.username}`} />
                    <div className="username-container">{user.username}</div>
                  </td>
                  <td>{user.score}</td>
                </tr>
              ))}
            </tbody>
          </table>

          </div>
            
        </div>
        <div className="person-container-small">
            <PersonComponent handleHeadClick={() => {}} headClicked={false} disabledHover={true} />
        </div>
    </div>
  )
}
