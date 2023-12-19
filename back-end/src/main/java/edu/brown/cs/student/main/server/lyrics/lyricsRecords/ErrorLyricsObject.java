package edu.brown.cs.student.main.server.lyrics.lyricsRecords;

/**
 * Record for moshi to be able to parse the responses from the Lyrics Api.
 *
 * @param error tell us if an error occured
 */
public record ErrorLyricsObject(boolean error, String message) {}
