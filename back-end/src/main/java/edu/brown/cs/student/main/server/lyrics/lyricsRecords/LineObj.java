package edu.brown.cs.student.main.server.lyrics.lyricsRecords;

/**
 * Record so that moshi knows how to parse the lines inside the lyric object.
 *
 * @param timeTag the time at which the lyrics begin
 * @param words the words of that line
 */
public record LineObj(String timeTag, String words) {}
