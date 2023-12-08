package edu.brown.cs.student.main.server.spotify.records.searchRecords;

import java.util.List;

/**
 * Record created to allow moshi to know how to parse the json into an object, this pertains to the
 * album field of the json
 * @param images - list of image objects that contain the links to the image for the album
 */
public record AlbumObj(List<ImageObj> images) {

}
