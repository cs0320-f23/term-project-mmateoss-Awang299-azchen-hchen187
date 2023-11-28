package edu.brown.cs.student.main.server.spotify.recommendationrecords;

public record seedsProps(int afterFilteringSize, int afterRelinkingSize,
                         String href, String id, int initialPoolSize, String type) {

}
