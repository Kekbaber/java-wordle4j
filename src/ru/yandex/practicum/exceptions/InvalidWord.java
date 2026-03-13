package ru.yandex.practicum.exceptions;

public class InvalidWord extends GameExceptions {

    private final String word;
    private final int maxLength;

    public InvalidWord(String word, int maxLength) {
        super("Слово должно состоять из " + maxLength + " на кириллице: " + word);
        this.word = word;
        this.maxLength = maxLength;
    }

    public String getWord() {
        return word;
    }

    public int getMaxLength() {
        return maxLength;
    }
}
