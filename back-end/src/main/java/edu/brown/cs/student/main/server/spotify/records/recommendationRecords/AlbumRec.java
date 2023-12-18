package edu.brown.cs.student.main.server.spotify.records.recommendationRecords;

import java.util.List;

/**
 * Record used for moshi to parse album part of the recommendation.
 *
 * @param images list of images for the album corresponding to the song
 */
public record AlbumRec(List<ImageRec> images) {}
