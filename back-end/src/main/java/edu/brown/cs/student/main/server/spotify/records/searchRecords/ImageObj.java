package edu.brown.cs.student.main.server.spotify.records.searchRecords;

/**
 * Record that allows for the parsing of the object form json. Allows for the creation of an overall
 * song object.
 *
 * @param url the url for the image.
 * @param height the height of the image
 * @param width the width of the image
 */
public record ImageObj(String url, int height, int width) {}
