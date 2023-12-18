package edu.brown.cs.student.main.server.audioRecognition.audioData;

/** Class that mocks the return of the audio data to ensure that the handler works as intended */
public class MockedAudioData implements IAudioData {

  /** Constructor for the mocked audio data class. */
  public MockedAudioData() {}

  /**
   * Method that mocks the getTranslation method in order to be able to ensure that the handler
   * works without making API calls.
   *
   * @param token API token for Deepgram, allowing us to use their model to turn audio to text and
   *     detect different languages.
   * @param encodedAudio the bytes of the audio file allowing for us to send it to the API
   * @return mocked String to be able to test the handler without API calls
   */
  @Override
  public String getTranslation(String token, String encodedAudio) {

    return "This is audio as a string";
  }
}
