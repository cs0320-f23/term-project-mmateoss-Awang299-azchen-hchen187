package edu.brown.cs.student.main.server.spotify.records.audioFeaturesRecords;

import java.util.List;

/**
 * Record that works with moshi to create the object that contains all the information relating
 * to the features of the songs inputted.
 *
 * @param audio_features A list of audioFeatures objects.
 */
public record FeaturesProp(List<AudioFeaturesObj> audio_features) {

}
