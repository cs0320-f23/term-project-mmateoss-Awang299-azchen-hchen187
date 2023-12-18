package edu.brown.cs.student.main.server.audioRecognition.audioRecords.audioObjRecords;

import java.util.List;

/**
 * Record used by moshi to parse the audio to text.
 *
 * @param transcript what the model thinks the audio said
 * @param confidence how confident the model is in its prediction
 * @param words list of words that the model thinks it heard
 */
public record AlternativeObj(String transcript, double confidence, List<WordObj> words) {}
