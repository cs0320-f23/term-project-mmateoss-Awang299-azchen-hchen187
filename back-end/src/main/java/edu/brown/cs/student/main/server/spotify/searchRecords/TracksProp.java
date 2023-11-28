package edu.brown.cs.student.main.server.spotify.searchRecords;

import java.util.List;

/**
 * Record that works to show moshi how to create a track response.
 *
 * Descriptions gotten directly from Spotify API:
 * https://developer.spotify.com/documentation/web-api/reference/search
 *
 * @param href A link to the Web API endpoint returning the full result of the request
 * @param total The total number of items available to return.
 * @param items array of TrackObj
 */
public record TracksProp(String href, int total, List<TrackObj> items) {

}
