package edu.brown.cs.student.main.server.audioRecognition.audioRecords.audioHandlerRecord;

/**
 * Record used by moshi in order to parse the passed in post object to be able to use in the
 * server
 *
 * @param data the base64 encoded String
 */
public record AudioHandlerPostObj(String data) {

}
