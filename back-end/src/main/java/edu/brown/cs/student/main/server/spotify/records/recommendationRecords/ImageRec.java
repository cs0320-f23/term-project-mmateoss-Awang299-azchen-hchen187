package edu.brown.cs.student.main.server.spotify.records.recommendationRecords;

/**
 * Record used for moshi to be able to parse the image to get its link and size
 *
 * @param url url to the image of the album
 * @param height height of the image
 * @param width width of the image
 */
public record ImageRec(String url, int height, int width) {}
