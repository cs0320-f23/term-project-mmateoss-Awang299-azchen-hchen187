package edu.brown.cs.student.main.server.spotify.recommendationrecords;

public record trackProps( int duration_ms, boolean explicit, String href, String id, boolean
    is_playable, String uri, int track_number, String preview_url) {

}
