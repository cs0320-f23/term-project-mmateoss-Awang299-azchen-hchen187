package edu.brown.cs.student.main.server.lyrics.data;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import edu.brown.cs.student.main.server.lyrics.lyricsRecords.ErrorLyricsObject;
import edu.brown.cs.student.main.server.lyrics.lyricsRecords.LineObj;
import edu.brown.cs.student.main.server.lyrics.lyricsRecords.LyricsObject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class that will be used to get the needed data from the spotify API, allowing
 * our server to use it accordingly.
 */
public class LyricsData implements ILyricsData {

    /**
     * 
     * @param trackID- String representing the Spotify track ID
     * @return ArrayList<String[]> containing the lyrics, where each line is
     *         String[2] of the form [timestamp in seconds, lyrics in the line]
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    @Override
    public ArrayList<String[]> getLyrics(String trackID) throws IOException, InterruptedException, URISyntaxException {
        LyricsObject lyricsBody = this.GetLyricsBody(trackID);

        // Construct by looping over all lines
        ArrayList<String[]> result = new ArrayList<String[]>();
        for (LineObj line : lyricsBody.lines()) {
            try {
                result.add(this.ParseLine(line));
            } catch (Exception e) {
                if (result.size() > 0) {
                    result.get(result.size() - 1)[1] = result.get(result.size() - 1)[1] + " " + line.words();
                }
            }
        }
        return result;
    }

    /**
     * Public wrapper for GetLyricsBody that determines whether
     * lyrics are available for trackID's song
     * 
     * @param trackID- spotify id of a song
     * @return true if lyrics exist, false otherwise
     */
    @Override
    public Boolean LyricsExist(String trackID) {
        try {
            this.GetLyricsBody(trackID);
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    /**
     * Performs API call to obtain the lyrics of a song given trackID.
     * 
     * @param trackID- spotify song id
     * @return LyricsObject representing the lyrics body
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    private LyricsObject GetLyricsBody(String trackID) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest buildRequest = HttpRequest.newBuilder()
                .uri(new URI(
                        "https://spotify-lyric-api-984e7b4face0.herokuapp.com/?trackid=" + trackID + "&format=lrc"))
                .GET()
                .build();
        // building a response with the HttpRequest
        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(buildRequest, HttpResponse.BodyHandlers.ofString());

        // using Moshi to turn it into a Map<String, Object> object
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Map<String, Object>> dataAdapter = moshi
                .adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
        Map<String, Object> body = dataAdapter.fromJson(response.body());

        // Check if error exists
        if (((Boolean) body.get("error"))) {
            JsonAdapter<ErrorLyricsObject> errorAdapter = moshi.adapter(ErrorLyricsObject.class);
            ErrorLyricsObject errorBody = errorAdapter.fromJson(response.body());
            throw new IOException(errorBody.message());
        }

        JsonAdapter<LyricsObject> lyricsAdapter = moshi.adapter(LyricsObject.class);
        LyricsObject lyricsBody = lyricsAdapter.fromJson(response.body());

        if (!lyricsBody.syncType().equals("LINE_SYNCED")) {
            throw new IOException("Lyrics aren't line-synced");
        }
        return lyricsBody;
    }

    /**
     * Obtains and parses LineObj object into the desired String[]
     * 
     * @param LineObj representing a line of LRC
     * @return String[3] representing [timestamp in seconds, lyrics]
     * @throws Exception
     */
    private String[] ParseLine(LineObj line) throws Exception {
        String[] newLine = new String[3];
        // Parse timestamp into String[3] of the form [minutes, seconds]
        String[] parsedTime = line.timeTag().split(":");
        if (parsedTime.length != 2) {
            throw new Exception("Timestamp not formatted as \"minutes:seconds\"");
        }
        newLine[0] = ((Float) ((Float.parseFloat(parsedTime[0]) * 60) + Float.parseFloat(parsedTime[1]))).toString();
        newLine[1] = line.words().replace("\u266a", "");
        newLine[2] = "";
        return newLine;
    }
}