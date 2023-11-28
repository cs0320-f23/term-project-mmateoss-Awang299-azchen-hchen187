package edu.brown.cs.student.main.server.spotify.data;

import edu.brown.cs.student.main.server.spotify.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.searchRecords.Song;

public interface IData {
    public Song GetSong();

    public Recommendation GetRecommendation();
}
