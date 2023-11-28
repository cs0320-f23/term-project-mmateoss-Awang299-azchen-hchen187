package edu.brown.cs.student.main.server.spotify.recommendationRecords;

import java.util.List;

/**
 * Overall record that works with moshi to create a Recommendation object.
 *
 * Descriptions gotten directly from spotify:
 * https://developer.spotify.com/documentation/web-api/reference/get-recommendations
 *
 * @param seeds A list of recommendation seed objects
 * @param tracks A list of track objects ordered according to the parameters supplied.
 */
public record Recommendation(List<SeedsProps> seeds, List<TrackProps> tracks) {

}
