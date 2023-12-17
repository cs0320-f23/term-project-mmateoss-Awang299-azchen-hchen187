package edu.brown.cs.student.main.server.lyrics.lyricsRecords;

import java.util.List;

/**
 * Record for moshi to be able to parse the responses from the Lyrics Api.
 * @param error tell us if an error occured
 * @param syncType the syncType for the api return
 * @param lines a list of line objects which contain the lyrics and time stamps
 */
public record LyricsObject(boolean error, String syncType, List<LineObj> lines) {

}
