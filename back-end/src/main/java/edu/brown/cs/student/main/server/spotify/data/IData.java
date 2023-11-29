package edu.brown.cs.student.main.server.spotify.data;

import edu.brown.cs.student.main.server.spotify.records.audioFeaturesRecords.FeaturesProp;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.records.searchRecords.Song;

public interface IData {
    public Song getSong(String token, String songName) throws Exception;

    public Recommendation getRecommendation(String token, String limit, String seed_tracks,
        String min_acousticness, String max_acousticness, String min_danceability,
        String max_danceability, String min_energy, String max_energy,
        String min_speechiness, String max_speechiness, String min_valence,
        String  max_valence) throws Exception;

    public FeaturesProp getFeatures(String token, String[] allNames) throws Exception;
}
