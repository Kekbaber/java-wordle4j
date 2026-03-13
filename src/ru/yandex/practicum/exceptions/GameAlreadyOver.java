package ru.yandex.practicum.exceptions;

public class GameAlreadyOver extends GameExceptions {
    public GameAlreadyOver() {
        super("Игра уже завершена");
    }
}
