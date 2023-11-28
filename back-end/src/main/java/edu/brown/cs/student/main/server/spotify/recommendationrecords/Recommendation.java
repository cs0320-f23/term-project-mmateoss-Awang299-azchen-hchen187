package edu.brown.cs.student.main.server.spotify.recommendationrecords;

import java.util.List;

public record Recommendation(List<seedsProps> seeds, List<trackProps> tracks) {

}
