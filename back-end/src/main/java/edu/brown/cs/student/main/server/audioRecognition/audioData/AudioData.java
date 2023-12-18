package edu.brown.cs.student.main.server.audioRecognition.audioData;

import edu.brown.cs.student.main.server.audioRecognition.audioRecords.audioObjRecords.AudioObj;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;

/** Class that does all the logic for the handler to be able to turn audio to text. */
public class AudioData implements IAudioData {

  private AudioDataSource source;

  /** Constructor for the AudioData class. */
  public AudioData() {
    this.source = new AudioDataSource();
  }

  /**
   * Method called by the handler that turns the audio into Text.
   *
   * @param token API token for Deepgram, allowing us to use their model to turn audio to text and
   *     detect different languages.
   * @param encodedAudio the bytes of the audio file allowing for us to send it to the API
   * @return a String which is the translation of the audio to text
   */
  @Override
  public String getTranslation(String token, String encodedAudio)
      throws URISyntaxException, IOException, InterruptedException, IllegalStateException {

    byte[] bytes = this.getBytes(encodedAudio);
    AudioObj audioObj = this.source.getAudioText(token, bytes);
    String transcript = this.extractTranscript(audioObj);

    return transcript;
  }

  /**
   * Method the turns an encodedAudio string into bytes in order to be able to make API call.
   *
   * @param encodedAudio the String that was passed into the handler for the audio
   * @return the byte array of the encodedAudio so that it can be passed into the api call.
   */
  public byte[] getBytes(String encodedAudio) {

    byte[] bytes = Base64.getDecoder().decode(encodedAudio);

    return bytes;
  }

  /**
   * Method that extracts the Transcript from the audioObj returned by the Api call.
   *
   * @param audioObj audioObj returned by the api call and parse by moshi
   * @return the transcript of what was said in the audio.
   */
  private String extractTranscript(AudioObj audioObj) {

    if (audioObj.results().channels().size() > 0
        && audioObj.results().channels().get(0).alternatives().size() > 0) {
      String transcript = audioObj.results().channels().get(0).alternatives().get(0).transcript();
      return transcript;
    } else {
      throw new IllegalStateException();
    }
  }
}
