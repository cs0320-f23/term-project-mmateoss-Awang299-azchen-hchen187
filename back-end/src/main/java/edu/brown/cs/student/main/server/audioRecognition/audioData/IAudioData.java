package edu.brown.cs.student.main.server.audioRecognition.audioData;

/** Interface used to be able to dependency inject mocked or real data into the handler that gets */
public interface IAudioData {

  /**
   * Method used to get what is said in the audio as text, allowing for us to be able to see if the
   * user said it properly.
   *
   * @param token API token for Deepgram, allowing us to use their model to turn audio to text and
   *     detect different languages.
   * @param encodedAudio the bytes of the audio file allowing for us to send it to the API
   * @return a String containing the text that was said.
   */
  public String getTranslation(String token, String encodedAudio) throws Exception;
}
