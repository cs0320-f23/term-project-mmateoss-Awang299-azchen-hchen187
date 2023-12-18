import React, {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useState,
} from "react";
import { SongData } from "../interfaces/Interface";

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
  setToken: (token: string) => void;
}

const AppContext = createContext<AppContextProps | undefined>(undefined);

export const ContextProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [selectedTrack, setSelectedTrack] = useState<string[]>([]);
  const [songLanguage, setSongLanguage] = useState("");
  const [nativeLanguage, setNativeLanguage] = useState("");
  const [totalPoints, setTotalPoints] = useState(0);
  const [difficulty, setDifficulty] = useState("easy");
  const [token, setToken] = useState(
    `BQCoD-eemvBYu1uqVGzU1C_Rr1h8YvdX4l-u84IjLyglwH1h8Ap9MEIOjOW21DTdrX9w1vBMkDhxTYEO3n0tGj2ksYjjJY8rShb4RBx1GItcVWiZB68OQC6hWyTLGtBfGbx0w2y4ZQYquyNpyYzZHeRriG-yf8zCdxVljq8E0qBi2P_IhdYAHUC_xTkLUJDRLPAOKZTep3Nw1qqoGpB00GH_hb7o6tSp`
  );

  const chooseTrack = (track: string[]) => {
    setSelectedTrack(track);
    console.log("set track to " + track);
  };

  const chooseNativeLanguage = (language: string) => {
    setNativeLanguage(language);
    console.log("native language: " + language);
  };

  const chooseDifficulty = (difficulty: string) => {
    setDifficulty(difficulty);
    console.log(difficulty);
  };

  const chooseSongLanguage = (language: string) => {
    setSongLanguage(language);
    console.log("song language; " + language);
  };

  const contextInfo: AppContextProps = {
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
  };

  return (
    <AppContext.Provider value={contextInfo}>{children}</AppContext.Provider>
  );
};

export const useAppContext = () => {
  const context = useContext(AppContext);
  if (!context) {
    throw new Error("useAppContext must be used within an AppProvider");
  }
  return context;
};
