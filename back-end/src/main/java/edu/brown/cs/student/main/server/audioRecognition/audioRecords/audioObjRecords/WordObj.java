package edu.brown.cs.student.main.server.audioRecognition.audioRecords.audioObjRecords;

/**
 * Record used by moshi for the model to give info on what each word it heard.
 *
 * @param word what word it thinks it heard
 * @param start at what time the word began
 * @param end at what time the word ended
 * @param confidence how confident the model is in its prediction
 */
public record WordObj(String word, double start, double end, double confidence) {}
