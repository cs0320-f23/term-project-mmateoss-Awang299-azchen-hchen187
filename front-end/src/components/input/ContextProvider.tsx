import React, { createContext, ReactNode, useContext, useEffect, useState } from 'react'
import { SongData } from '../interfaces/Interface';

interface AppContextProps {
  selectedTrack: SongData | undefined;
  chooseTrack: (track: SongData) => void;
  nativeLanguage: string;
  chooseNativeLanguage: (language: string) => void;
  totalPoints: number;
  difficulty: string;
  chooseDifficulty: (difficulty: string) => void;
  token: string;
  setToken: (token : string) => void;
}

const AppContext = createContext<AppContextProps | undefined>(undefined);

export const ContextProvider: React.FC<{ children: ReactNode }> = ({children}) => {
    const [selectedTrack, setSelectedTrack] = useState<SongData>();
    const [nativeLanguage, setNativeLanguage] = useState("");
    const [totalPoints, setTotalPoints] = useState(0);
    const [difficulty, setDifficulty] = useState("easy");
    const [token, setToken] = useState(`BQBl1c7vtZkFDciB6PnkUuT2kjW3YvhCnqwELCunU4Eg_Myqq3pHTrMjH4X1sdfbDkVsjl0VqmUAcBEm3N8wN_hMHkbKD02anVYsWML6GwiOXRV9iqYO66KHeP4pEk_en_WPlkrXiPHj6ipyEdc0Hrvi9-vcNjSZijNQz-ULrqMcJMyEK1ulMICiOG9H_cACK3h6iVrpB_U

    `)

    const chooseTrack = (track : SongData) => {
        setSelectedTrack(track);
      };

    const chooseNativeLanguage = (language: string) => {
      setNativeLanguage(language);
      console.log(language)
    }

    const chooseDifficulty = (difficulty: string) => {
      setDifficulty(difficulty);
      console.log(difficulty)
    }


    const contextInfo : AppContextProps = {
        selectedTrack, 
        chooseTrack,
        nativeLanguage,
        chooseNativeLanguage,
        totalPoints, 
        difficulty,
        chooseDifficulty,
        token,
        setToken,
    }

  return (
    <AppContext.Provider value={contextInfo}>{children}</AppContext.Provider>
  )
}

export const useAppContext = () => {
  const context = useContext(AppContext);
  if (!context) {
    throw new Error('useAppContext must be used within an AppProvider');
  }
  return context;
}
