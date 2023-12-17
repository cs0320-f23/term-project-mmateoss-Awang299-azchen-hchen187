package edu.brown.cs.student.main.server.translate.mockedData;

import java.util.ArrayList;

import edu.brown.cs.student.main.server.translate.data.ITranslateData;
import edu.brown.cs.student.main.server.translate.records.TranslateResult;

public class MockTranslateData implements ITranslateData {

    @Override
    public TranslateResult getTranslation(String text, String fromLanguage, String toLanguage) throws Exception {
        return new TranslateResult("HELLO!", fromLanguage);
    }
}
