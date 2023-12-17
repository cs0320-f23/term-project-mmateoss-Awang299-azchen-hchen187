package edu.brown.cs.student.main.server.spotify.records.searchRecords;


/**
 * Record object used to allow moshi to parse in order to get the name of the artist.
 *
 * @param name the name of the artist
 */
public record ArtistObj(String name) {

}
