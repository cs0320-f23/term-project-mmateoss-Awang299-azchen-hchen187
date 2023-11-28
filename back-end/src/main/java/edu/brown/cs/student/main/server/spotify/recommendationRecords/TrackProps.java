package edu.brown.cs.student.main.server.spotify.recommendationRecords;


/**
 * Record that tells Moshi how to create the list of track objects.
 *
 * Descriptions gotten directly from spotify:
 * https://developer.spotify.com/documentation/web-api/reference/get-recommendations
 *
 * @param duration_ms The track length in milliseconds.
 * @param explicit Whether the track has explicit lyrics
 *                 ( true = yes it does; false = no it does not OR unknown).
 * @param href A link to the Web API endpoint providing full details of the track.
 * @param id The Spotify ID for the track.
 * @param is_playable  If true, the track is playable in the given market. Otherwise, false.
 * @param uri The Spotify URI for the track.
 * @param track_number The number of the track. If an album has several discs,
 *                     the track number is the number on the specified disc.
 * @param preview_url A link to a 30-second preview (MP3 format) of the track. Can be null
 * @param name The name of the track.
 */
public record TrackProps(int duration_ms, boolean explicit, String href, String id, boolean
    is_playable, String uri, int track_number, String preview_url, String name) {

}
