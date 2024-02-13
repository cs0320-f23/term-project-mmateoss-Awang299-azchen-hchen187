import { easySongData, songsData } from "./MockedData";

//interface to outline the fields needed
interface MockedAPIData {
  result: string;
  [key: string]: string | {title: string, artist: string, trackID: string, albumUrl: string}[]
}

interface MockedSongData {
    Result: string;
    data : string[][]
}

//method to return song data based on search input
export const mockGetSongs = (input: string) : MockedSongData => {
    if (easySongData[input]) {
        return {
            Result: "Success", 
            data: easySongData[input]
        }
    } else {
        return {
            Result: "error", 
            data: [["error in fetch"]]
        }
    }
}