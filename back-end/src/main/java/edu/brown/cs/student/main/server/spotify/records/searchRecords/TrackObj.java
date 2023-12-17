package edu.brown.cs.student.main.server.spotify.records.searchRecords;


import java.util.List;

/**
 * Record that takes up the internal part of a song object, here is where the song id can be found.
 *
 * Descriptions gotten directly from Spotify API:
 * https://developer.spotify.com/documentation/web-api/reference/search
 *
 * @param id The Spotify ID for the track.
 * @param uri The Spotify URI for the track.
 * @param preview_url A link to a 30-second preview (MP3 format) of the track. Can be null
 * @param name The name of the track.
 * @param popularity The popularity of the track. The value will be between 0 and 100,
 *                   with 100 being the most popular.
 */
public record TrackObj(String id, String uri, String preview_url, String name,
                       int popularity, AlbumObj album, List<ArtistObj> artists) {

}
