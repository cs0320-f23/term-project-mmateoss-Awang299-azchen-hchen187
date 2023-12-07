import React, { createContext, ReactNode, useContext, useEffect, useState } from 'react'
import { SongData } from '../interfaces/Interface';

interface AppContextProps {
  selectedTrack: SongData | undefined;
  chooseTrack: (track: SongData) => void;
}

const AppContext = createContext<AppContextProps | undefined>(undefined);

export const ContextProvider: React.FC<{ children: ReactNode }> = ({children}) => {
    const [selectedTrack, setSelectedTrack] = useState<SongData>();
    

    const chooseTrack = (track : SongData) => {
        setSelectedTrack(track);
      };


    const contextInfo : AppContextProps = {
        selectedTrack, 
        chooseTrack,
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
