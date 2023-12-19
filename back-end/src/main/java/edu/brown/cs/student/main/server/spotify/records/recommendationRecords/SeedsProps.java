package edu.brown.cs.student.main.server.spotify.records.recommendationRecords;

/**
 * Record that tells moshi how to make up the internal list of seed objects.
 *
 * <p>Descriptions gotten directly from spotify:
 * https://developer.spotify.com/documentation/web-api/reference/get-recommendations
 *
 * @param afterFilteringSize The number of tracks available after min_* and max_* filters have been
 *     applied.
 * @param afterRelinkingSize The number of tracks available after relinking for regional
 *     availability.
 * @param href A link to the full track or artist data for this seed. For tracks this will be a link
 *     to a Track Object.
 * @param id The id used to select this seed. This will be the same as the string used in the
 *     seed_artists, seed_tracks or seed_genres parameter.
 * @param initialPoolSize The number of recommended tracks available for this seed.
 * @param type The entity type of this seed. One of artist, track or genre.
 */
public record SeedsProps(
    int afterFilteringSize,
    int afterRelinkingSize,
    String href,
    String id,
    int initialPoolSize,
    String type) {}
