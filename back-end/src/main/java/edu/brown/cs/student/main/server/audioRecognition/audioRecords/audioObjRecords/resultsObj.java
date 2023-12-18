package edu.brown.cs.student.main.server.audioRecognition.audioRecords.audioObjRecords;

import java.util.List;

/**
 * Record used to parse the response of the model into a list of responses.
 *
 * @param channels object used to split the audio the model heard into different channels.
 */
public record resultsObj(List<ChannelObj> channels) {}
