package edu.brown.cs.student.main.server.translate.mockedData;

import java.util.ArrayList;

import edu.brown.cs.student.main.server.translate.data.ITranslateData;
import edu.brown.cs.student.main.server.translate.records.TranslateResult;
import java.util.HashMap;
import java.util.Map;

public class MockTranslateData implements ITranslateData {

    @Override
    public TranslateResult getTranslation(String text, String fromLanguage, String toLanguage) throws Exception {
        Map<String, String> wordMap = new HashMap<>();
        wordMap.put("Are you ready, kids?", "¿Están listos, chicos?");
        wordMap.put("(Aye-aye, Captain!)", "(Aye-aye, Capitán!)");
        wordMap.put("I can't hear you!", "¡No te oigo!");
        wordMap.put("(Aye-aye, Captain!)", "(Aye-aye, Capitán!)");
        wordMap.put("Oh...", "Oh...");
        wordMap.put("Who lives in a pineapple under the sea?", "¿Quién vive en una piña bajo el mar?");
        wordMap.put("(SpongeBob SquarePants!)", "(SpongeBob SquarePants!)");
        wordMap.put("Absorbant and yellow and porous is he", "Absorbante y amarillo y poroso es él");
        wordMap.put("(SpongeBob SquarePants!)", "(SpongeBob SquarePants!)");
        wordMap.put("If nautical nonsense be something you wish", "Si las tonterías náuticas son algo que deseas");
        wordMap.put("(SpongeBob SquarePants!)", "(SpongeBob SquarePants!)");
        wordMap.put("Then drop on the deck and flop like a fish", "Luego caer en la cubierta y flop como un pez");
        wordMap.put("(SpongeBob SquarePants!) Ready?", "(SpongeBob SquarePants!) ¿Listo?");
        wordMap.put("SpongeBob SquarePants", "Bob Esponja");
        wordMap.put("SpongeBob SquarePants", "Bob Esponja");
        wordMap.put("SpongeBob SquarePants", "Bob Esponja");
        wordMap.put("SpongeBob SquarePants", "Bob Esponja");
        wordMap.put("Aha-ha-ha-ha-ha", "Aha-ha-ha-ha-ha");
        wordMap.put("", "");
        return new TranslateResult(wordMap.get(text), fromLanguage);

    }
}
