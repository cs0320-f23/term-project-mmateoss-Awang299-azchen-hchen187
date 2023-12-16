package edu.brown.cs.student.Tests.server.spotify.audioTests;

import com.beust.ah.A;
import edu.brown.cs.student.main.server.audioRecognition.audioData.AudioData;
import edu.brown.cs.student.main.server.audioRecognition.audioData.AudioDataSource;
import edu.brown.cs.student.main.server.audioRecognition.audioData.mockedAudioSource.MockedAudioDataSource;
import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

/**
 * Class that tests we can correctly use real data to make api call.
 */
public class AudioDataTests {

  /**
   * Method that tests that we can correctly turn an encoded base64 audio string into bytes in order
   * to make the api call.
   */
  @Test
  public void testByteCreation() throws Exception{
    MockedAudioDataSource source = new MockedAudioDataSource("data/mockedAudio/mockedAudio1.mp3");
    byte[] encoded = source.getBase64Encoded();
    String encodedString = Base64.getEncoder().encodeToString(encoded);
    AudioData data = new AudioData();
    byte[] bytes = data.getBytes(encodedString);
    Assert.assertEquals(bytes, encoded);

  }

  /**
   * Method that tests that we can correctly get the translation of audio to text using the encoded
   * audio string.
   */
  @Test
  public void testGetTranslation() throws Exception{
    MockedAudioDataSource source = new MockedAudioDataSource("data/mockedAudio/mockedAudio1.mp3");
    byte[] encoded = source.getBase64Encoded();
    String encodedString = Base64.getEncoder().encodeToString(encoded);
    AudioData data = new AudioData();
    //TODO: Add token here to test that its correct.
    String token = "";
    String transcript = data.getTranslation(token, encodedString);
    Assert.assertEquals(transcript, "What can I do for you?");
  }

  @Test
  public void testOtherLanguage() throws Exception{
    MockedAudioDataSource source = new MockedAudioDataSource("data/mockedAudio/mockAudio3.mp3");
    byte[] encoded = source.getBase64Encoded();
    String encodedString = Base64.getEncoder().encodeToString(encoded);
    AudioData data = new AudioData();
    //TODO: Add token here to test that its correct.
    String token = "";
    String transcript = data.getTranslation(token, encodedString);
    Assert.assertEquals(transcript, "O la");
  }


}



