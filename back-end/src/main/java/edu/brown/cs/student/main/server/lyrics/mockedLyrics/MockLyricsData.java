package edu.brown.cs.student.main.server.lyrics.mockedLyrics;

import java.util.ArrayList;

import edu.brown.cs.student.main.server.lyrics.data.ILyricsData;

public class MockLyricsData implements ILyricsData {

    @Override
    public ArrayList<String[]> getLyrics(String trackID) throws Exception {
        // TODO Auto-generated method stub
        ArrayList<String[]> result = new ArrayList<String[]>();
        result.add(new String[] { "4.83", "I just wanna get high with my lover" });
        result.add(new String[] { "8.21", "Veo una mu\u00f1eca cuando miro en el espejo" });
        result.add(new String[] { "11.27", "Kiss, kiss, looking dolly, I think I may go out tonight" });
        return result;
    }

    @Override
    public Boolean LyricsExist(String trackID) {
        return true;
    }

}
