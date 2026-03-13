package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.GameAlreadyOver;
import ru.yandex.practicum.exceptions.GameExceptions;
import ru.yandex.practicum.exceptions.WordAlreadyUsed;

import java.util.*;

public class GameState {

    private final GameLogger logger;
    private final Set<Character> presentLetters;
    private final Set<Character> absentLetters;
    private final Set<String> usedWords;
    private final Map<Character, Set<Integer>> notInPositions;
    private final String answer;
    private final char[] exactPositions;
    private final int maxSteps;
    private int steps;
    private boolean isWin = false;
    private boolean isGameOver = false;

    public GameState(String answer, int maxSteps, GameLogger logger) {
        this.answer = answer;
        this.maxSteps = maxSteps;
        this.logger = logger;
        presentLetters = new HashSet<>();
        absentLetters = new HashSet<>();
        exactPositions = new char[5];
        notInPositions = new HashMap<>();
        usedWords = new HashSet<>();
        logger.info("Начало игры. Загаданное слово: " + answer);
    }

    public void update(String word, String feedback) throws GameExceptions {
        checkGameAlreadyOver();
        checkWordAlreadyUsed(word);

        steps++;
        usedWords.add(word);

        for (int i = 0; i < 5; i++) {
            char c = word.charAt(i);
            char f = feedback.charAt(i);
            if (f == WordleSymbol.EXACT.getSymbol()) {
                exactPositions[i] = c;
                presentLetters.add(c);
            } else if (f == WordleSymbol.PRESENT.getSymbol()) {
                presentLetters.add(c);
            }
        }

        for (int i = 0; i < 5; i++) {
            char c = word.charAt(i);
            char f = feedback.charAt(i);
            if (f == WordleSymbol.PRESENT.getSymbol()) {
                notInPositions.computeIfAbsent(c, k -> new HashSet<>()).add(i);
            } else if (f == WordleSymbol.EXACT.getSymbol()) {
                Set<Integer> forbidden = notInPositions.get(c);
                if (forbidden != null) {
                    forbidden.remove(i);
                }
            } else if (f == WordleSymbol.ABSENT.getSymbol()) {
                if (!presentLetters.contains(c)) {
                    absentLetters.add(c);
                }
            }
        }
        checkSteps();
        checkAnswer(word);
    }

    public Set<Character> getPresentLetters() {
        return Collections.unmodifiableSet(presentLetters);
    }

    public Set<Character> getAbsentLetters() {
        return Collections.unmodifiableSet(absentLetters);
    }

    public char[] getExactPositions() {
        return exactPositions.clone();
    }

    public Map<Character, Set<Integer>> getNotInPositions() {
        Map<Character, Set<Integer>> copy = new HashMap<>();
        for (Map.Entry<Character, Set<Integer>> entry : notInPositions.entrySet()) {
            copy.put(entry.getKey(), Collections.unmodifiableSet(entry.getValue()));
        }
        return Collections.unmodifiableMap(copy);
    }

    public boolean isWordUsed(String word) {
        return usedWords.contains(word);
    }

    private void checkGameAlreadyOver() throws GameAlreadyOver {
        if (isGameOver) {
            throw new GameAlreadyOver();
        }
    }

    public void checkWordAlreadyUsed(String word) throws WordAlreadyUsed {
        if (isWordUsed(word)) {
            throw new WordAlreadyUsed();
        }
    }

    public void checkSteps() {
        if (steps == maxSteps) {
            isGameOver = true;
            logger.info("Игра закончена!");
        }
    }

    public void checkAnswer(String word) {
        if (word.equals(answer)) {
            isGameOver = true;
            isWin = true;
            logger.info("Игрок угадал слово!");
        }
    }

    public String getAnswer() {
        return answer;
    }

    public Set<String> getUsedWords() {
        return Collections.unmodifiableSet(usedWords);
    }

    public int getMaxSteps() {
        return maxSteps;
    }

    public int getSteps() {
        return steps;
    }

    public boolean isWin() {
        return isWin;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}
