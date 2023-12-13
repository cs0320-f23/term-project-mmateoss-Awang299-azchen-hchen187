package edu.brown.cs.student.main.server.lyrics.data;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public interface ILyricsData {

    public ArrayList<String[]> getLyrics(String trackID) throws Exception;

    public Boolean LyricsExist(String trackID);
}
