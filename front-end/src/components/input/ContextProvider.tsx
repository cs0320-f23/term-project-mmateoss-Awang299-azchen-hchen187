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
  const [token, setToken] =
    useState(`BQB5Obvr9woLrutyJ8ogQiSGgOSxiWrTab0fib0X3fvskhgiaHcHD1hE_Zz8YcyHEaGcWl0S3R5ZHoWfI3Q7rAPGQHvXQyy_0EGfqWE7rM7F-qjzflxlq2qUo4FNq_nehaLnBIpYSFoQn7azZpXUbgsc2ZhZhZ7yuenX-rT8m7SXtavgskgL7_r_gYefnsceMs4n-0zCSg
`);

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
