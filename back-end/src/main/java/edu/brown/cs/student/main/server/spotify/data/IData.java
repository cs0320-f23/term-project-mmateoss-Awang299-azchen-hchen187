package edu.brown.cs.student.main.server.spotify.data;

import edu.brown.cs.student.main.server.spotify.records.audioFeaturesRecords.FeaturesProp;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.records.searchRecords.Song;
import java.util.List;

/**
 * Interface used for dependency injection allowing us to run a server with
 * mocked and real data.
 */
public interface IData {

    /**
     * Method used to get a song object from its name
     *
     * @param songName the name of the Song
     * @return a song object
     * @throws Exception any exception that could occur
     */
    public Song getSong(String songName) throws Exception;

    /**
     * Method used to get a recommendation either mocked or real
     *
     * @param limit    the max number of songs recommended
     * @param allNames the names of all the acceptable songs which we want to use to
     *                 generate a
     *                 recommendation
     * @return a recommendation object
     * @throws Exception any exception that could be thrown
     */
    public Recommendation getRecommendation(String limit, String[] allNames, String variability) throws Exception;

    /**
     * Method used to get a feature object based on the names of songs
     * 
     * @param allNames the names of acceptable songs whose features we want
     * @return featureProp object
     * @throws Exception any exception that could be thrown either by mocked or real
     *                   data.
     */
    public FeaturesProp getFeatures(String[] allNames) throws Exception;

    /**
     * Method used to set the token in order to be used throughout the class.
     * 
     * @param token Spotify API token
     * @return boolean saying if it was set
     */
    public boolean setToken(String token);

    /**
     * Method used to get songs from a prompt that is inputted, could be other than
     * just song name
     * 
     * @param prompt what is searching for the song
     * @param limit  max number of songs returned
     * @return List of List of strings containing info for the song
     *
     * @throws Exception any exception that could be thrown while searching.
     */
    public List<List<String>> getSongsPrompt(String prompt, String limit) throws Exception;

    /**
     * Method that processes the recommendation returned to make sure we don't
     * recommend the same song twice
     *
     * @param rec   recommendation object to be processed
     * @param names names of the songs inputted.
     * @return a processed Recommendation obj.
     */
    public Recommendation postProcess(Recommendation rec, String[] names);
}
