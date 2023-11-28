package edu.brown.cs.student.main.server.spotify.data;

import edu.brown.cs.student.main.server.spotify.records.audioFeaturesRecords.FeaturesProp;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.records.searchRecords.Song;

public interface IData {
    public Song getSong() throws Exception;

    public Recommendation getRecommendation() throws Exception;

    public FeaturesProp getFeatures() throws Exception;
}
