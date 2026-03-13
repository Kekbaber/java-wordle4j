package ru.yandex.practicum.exceptions;

public class WordNotFoundInDictionary extends GameExceptions {

    private final String word;

    public WordNotFoundInDictionary(String word) {
        super("Слово \"" + word + "\" отсутствует в словаре.");
        this.word = word;

    }

    public String getWord() {
        return word;
    }
}
