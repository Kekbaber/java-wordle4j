package ru.yandex.practicum.exceptions;

public class WordAlreadyUsed extends GameExceptions {

    public WordAlreadyUsed() {
        super("Слово уже было использовано!");
    }
}
