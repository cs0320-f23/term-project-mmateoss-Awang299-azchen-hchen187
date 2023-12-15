import { songsData } from "./MockedData";

//interface to outline the fields needed
interface MockedAPIData {
  result: string;
  [key: string]: string | {title: string, artist: string, trackID: string, albumUrl: string}[]
}

//method to return song data based on search input
export const mockGetSongs = (input: string) : MockedAPIData => {
    if (songsData[input]) {
        return {
            result: "success", 
            data: songsData[input]
        }
    } else {
        return {
            result: "error", 
            error_type: "no data found"
        }
    }
}