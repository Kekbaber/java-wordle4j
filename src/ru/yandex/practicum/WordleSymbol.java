package ru.yandex.practicum;

public enum WordleSymbol {
    ABSENT('-'),
    PRESENT('^'),
    EXACT('+');

    private final char symbol;

    WordleSymbol(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public static WordleSymbol fromChar(char c) {
        for (WordleSymbol fs : values()) {
            if (fs.symbol == c) {
                return fs;
            }
        }
        throw new IllegalArgumentException("Неизвестный символ: " + c);
    }
}
