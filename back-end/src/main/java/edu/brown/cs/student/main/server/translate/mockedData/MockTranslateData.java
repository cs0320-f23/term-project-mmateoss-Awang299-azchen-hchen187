package edu.brown.cs.student.main.server.translate.mockedData;

import java.util.ArrayList;

import edu.brown.cs.student.main.server.translate.data.ITranslateData;

public class MockTranslateData implements ITranslateData {

    @Override
    public String getTranslation(String text, String fromLanguage, String toLanguage) throws Exception {
        return "HELLO!";
    }
}
