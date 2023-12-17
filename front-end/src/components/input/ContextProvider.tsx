import React, { createContext, ReactNode, useContext, useEffect, useState } from 'react'
import { SongData } from '../interfaces/Interface';

interface AppContextProps {
  selectedTrack: string[];
  chooseTrack: (track: string[]) => void;
  songLanguage: string;
  chooseSongLanguage: (language: string) => void;
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
    const [selectedTrack, setSelectedTrack] = useState<string[]>([]);
    const [songLanguage, setSongLanguage] = useState("");
    const [nativeLanguage, setNativeLanguage] = useState("");
    const [totalPoints, setTotalPoints] = useState(0);
    const [difficulty, setDifficulty] = useState("easy");
    const [token, setToken] = useState(`BQDTtV4oQ0-87nLXm8ITx8fKHa14Yl0LCdWtu4IS4vbqdH97Bpc36y_-_zmzw0524qPJY-G0wLnqqFSve7KZ3JCOLdQwJgTQ6Mm86aIcmnWXomq2IteTiaW2g-EBg5Q6C_ieLkpxVM_mz6rfzXmGJSHvskd5hupQA2g_lfXRzJ5hUOtMi5Xut009Njl39SOWADa8Rtg0x8K94qU2qmpPM8sfMRNC

    `)

    const chooseTrack = (track : string[]) => {
        setSelectedTrack(track);
        console.log("set track to " + track)
      };

    const chooseNativeLanguage = (language: string) => {
      setNativeLanguage(language);
      console.log("native language: " + language)
    }

    const chooseDifficulty = (difficulty: string) => {
      setDifficulty(difficulty);
      console.log(difficulty)
    }

    const chooseSongLanguage = (language: string) => {
      setSongLanguage(language);
      console.log("song language; "+language)
    }


    const contextInfo : AppContextProps = {
        selectedTrack, 
        chooseTrack,
        songLanguage, 
        chooseSongLanguage,
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
