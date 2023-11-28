package edu.brown.cs.student.main.server.spotify.audioFeaturesRecords;


/**
 * Record that works with moshi to create the internal audio features objects for the overall list
 * of feature objects.
 *
 * Descriptions gotten directly from the Spotify API:
 * https://developer.spotify.com/documentation/web-api/reference/get-several-audio-features
 *
 * @param acousticness A confidence measure from 0.0 to 1.0 of whether the track is acoustic.
 *                    1.0 represents high confidence the track is acoustic.
 * @param danceability Danceability describes how suitable a track is for dancing based on a
 *                    combination of musical elements including tempo, rhythm stability,
 *                     beat strength, and overall regularity. A value of 0.0 is least
 *                     danceable and 1.0 is most danceable.
 * @param duration_ms The duration of the track in milliseconds.
 * @param energy Energy is a measure from 0.0 to 1.0 and represents a perceptual measure of
 *               intensity and activity. Typically, energetic tracks feel fast, loud, and noisy.
 * @param id The Spotify ID for the track.
 * @param instrumentalness Predicts whether a track contains no vocals.
 *                        "Ooh" and "aah" sounds are treated as instrumental in this context.
 *                        Rap or spoken word tracks are clearly "vocal".
 *                         The closer the instrumentalness value is to 1.0,
 *                         the greater likelihood the track contains no vocal content.
 *                         Values above 0.5 are intended to represent instrumental tracks.
 * @param key The key the track is in. Integers map to pitches using standard Pitch Class notation.
 *           E.g. 0 = C, 1 = C♯/D♭, 2 = D, and so on. If no key was detected, the value is -1.
 * @param liveness Detects the presence of an audience in the recording.
 *                 Higher liveness values represent an increased probability that the
 *                 track was performed live.
 * @param loudness The overall loudness of a track in decibels (dB). Loudness values
 *                are averaged across the entire track and are useful for comparing relative
 *                loudness of tracks. Loudness is the quality of a sound that is the primary
 *                psychological correlate of physical strength (amplitude).
 *                 Values typically range between -60 and 0 db.
 * @param mode Mode indicates the modality (major or minor) of a track, the type of scale from
 *             which its melodic content is derived. Major is represented by 1 and minor is 0.
 * @param speechiness Speechiness detects the presence of spoken words in a track.
 *                    The more exclusively speech-like the recording
 *                    (e.g. talk show, audio book, poetry), the closer to 1.0 the attribute value.
 * @param tempo The overall estimated tempo of a track in beats per minute (BPM).
 * @param time_signature An estimated time signature. The time signature (meter) is
 *                       a notational convention to specify how many beats are in each bar
 *                       (or measure). The time signature ranges from 3 to 7 indicating
 *                       time signatures of "3/4", to "7/4".
 * @param track_href A link to the Web API endpoint providing full details of the track.
 * @param valence A measure from 0.0 to 1.0 describing the musical positiveness conveyed by a track.
 *               TracksProp with high valence sound more positive
 *                (e.g. happy, cheerful, euphoric), while tracks with low valence sound more
 *                negative (e.g. sad, depressed, angry).
 */
public record AudioFeaturesObj(float acousticness,float danceability, int duration_ms, float energy,
                               String id, float instrumentalness, int key, float liveness,
                               float loudness, int mode, float speechiness, float tempo,
                               int time_signature, String track_href, float valence) {

}
