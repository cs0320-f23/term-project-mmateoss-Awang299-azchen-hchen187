import { IntegerType } from 'mongodb';
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
}

const AppContext = createContext<AppContextProps | undefined>(undefined);

export const ContextProvider: React.FC<{ children: ReactNode }> = ({children}) => {
    const [selectedTrack, setSelectedTrack] = useState<SongData>();
    const [nativeLanguage, setNativeLanguage] = useState("");
    const [totalPoints, setTotalPoints] = useState(0);
    const [difficulty, setDifficulty] = useState("easy");

    

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
