package edu.brown.cs.student.main.server.audioRecognition.audioRecords.audioObjRecords;

import java.util.List;

/**
 * Record used by moshi to parse the response from the audio processing API.
 *
 * @param alternatives objects that hold the possibilities of what the audio said
 */
public record ChannelObj(List<AlternativeObj> alternatives) {}
