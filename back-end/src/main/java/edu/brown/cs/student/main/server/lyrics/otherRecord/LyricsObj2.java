package edu.brown.cs.student.main.server.lyrics.otherRecord;

import edu.brown.cs.student.main.server.lyrics.lyricsRecords.LineObj;
import java.util.List;

public record LyricsObj2(boolean error, String syncType, List<LineObj2> lines) {

}
