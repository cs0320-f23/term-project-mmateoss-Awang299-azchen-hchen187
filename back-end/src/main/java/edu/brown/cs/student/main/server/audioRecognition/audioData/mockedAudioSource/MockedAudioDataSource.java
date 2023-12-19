package edu.brown.cs.student.main.server.audioRecognition.audioData.mockedAudioSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class used to get the base64 encoding of mocked mp3 files in order to be able to test the logic
 * of getting the text of an audio file form the API.
 */
public class MockedAudioDataSource {

  /**
   * Class used to encode mockedAudioFiles to base64 in order to be able to see if it works with the
   * API
   */
  private String filePath;

  /**
   * Constructor for the MockedAudioDataSource class.
   *
   * @param filePath path to the file we want to send to the API.
   */
  public MockedAudioDataSource(String filePath) {

    this.filePath = filePath;
  }

  /**
   * Method used to encode a mp3 file into base64 in order to be able to pass it as an api
   * parameter.
   *
   * @return a byte array of the encoded file
   * @throws IOException exception thrown when the file can't be read or encoded.
   */
  public byte[] getBase64Encoded() throws IOException {
    // used these to learn how to encode a file
    // https://www.baeldung.com/java-base64-encode-and-decode
    // https://mkyong.com/java/how-to-convert-file-into-an-array-of-bytes/

    byte[] bytes = Files.readAllBytes(Paths.get(this.filePath));
    // String encoded = Base64.getEncoder().encodeToString(bytes);

    return bytes;
  }
}
