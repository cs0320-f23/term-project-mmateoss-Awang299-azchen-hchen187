package edu.brown.cs.student.Tests.server.spotify.audioTests;

import edu.brown.cs.student.main.server.audioRecognition.audioData.AudioDataSource;
import edu.brown.cs.student.main.server.audioRecognition.audioData.mockedAudioSource.MockedAudioDataSource;
import edu.brown.cs.student.main.server.audioRecognition.audioRecords.audioObjRecords.AudioObj;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

/**
 * Class using mocked data used to ensure that we can make and parse the api call to get text
 * from audio.
 */
public class mockedAudioDataTest {


  /**
   * Method that tests we can get the text of an audio file correctly and that we are making the
   * Api call correctly.
   *
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open
   *                     information.
   * @throws InterruptedException exception where connection to API is
   *                              interrupted.
   */
  @Test
  public void TestApi() throws URISyntaxException, IOException, InterruptedException {

    AudioDataSource data = new AudioDataSource();
    MockedAudioDataSource source = new MockedAudioDataSource("data/mockedAudio/mockedAudio1.mp3");
    //TODO: Put in DeepGram API token to run
    String token = "";
    byte[] encoded = source.getBase64Encoded();


    AudioObj parsedAudio = data.getAudioText(token, encoded);
    Assert.assertEquals(parsedAudio.results().channels().get(0).alternatives().get(0).transcript(),
        "What can I do for you?");


  }

  /**
   * Method that test that we can correctly get the bytes of an audiofile to send to the api
   *
   * @throws IOException exception where it failed to read/open
   *                     information.
   */
  @Test
  public void testEncoding() throws IOException{
    MockedAudioDataSource source = new MockedAudioDataSource("data/mockedAudio/mockedAudio1.mp3");
    byte[] encoded = source.getBase64Encoded();
    String encodedString = Base64.getEncoder().encodeToString(encoded);

    Assert.assertTrue(encoded != null);
    Assert.assertTrue(encoded.length > 0);

    System.out.println(encodedString);


  }


}
