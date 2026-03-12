package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.GameExceptions;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class WordleSolverTest {
    private final List<String> dictionary = Arrays.asList("слова", "слово", "молот", "крона", "книга", "парта", "слива", "загиб");
    private final WordleSolver solver = new WordleSolver(dictionary);
    private final GameLogger logger = new GameLogger(System.out);

    @Test
    void getHintShouldReturnWordWhenInitState() {
        GameState state = new GameState("слово", 3, logger);
        String hint = solver.getHint(state);
        assertTrue(dictionary.contains(hint));
    }

    @Test
    void getHintShouldReturnCorrectAnswer() throws GameExceptions {
        GameState state = new GameState("молот", 3, logger);
        state.update("холод", "-+++-");
        String hint = solver.getHint(state);
        assertNotNull(hint);
        assertEquals(state.getAnswer(), hint);
    }

    @Test
    void getHintShouldReturnWordWithExactPositions() throws GameExceptions {
        GameState state = new GameState("слово", 3, logger);
        state.update("слайд", "++---");

        List<String> filtered = new ArrayList<>();
        for (String word : dictionary) {
            if (word.length() == 5 && word.startsWith("сл")) {
                filtered.add(word);
            }
        }

        String hint = solver.getHint(state);
        assertTrue(filtered.contains(hint));
    }

    @Test
    void GetHintDoesNotRepeatUsedWords() throws GameExceptions {
        GameState state = new GameState("ууууу", 6, logger);

        state.update("слова", "-----");
        state.update("слово", "-----");
        state.update("молот", "-----");
        state.update("загиб", "-----");

        String hint = solver.getHint(state);
        Set<String> list = state.getUsedWords();
        assertFalse(list.contains(hint));
    }

    @Test
    void getHintShouldReturnWordWhenAbsentLetters() throws GameExceptions {
        GameState state = new GameState("венгр", 6, logger);
        state.update("молот", "-----");

        String hint = solver.getHint(state);
        for (char c : hint.toCharArray()) {
            assertFalse("молот".indexOf(c) >= 0);
        }
    }

    @Test
    void getHintShouldReturnWordWhenPresentLetters() throws GameExceptions {
        GameState state = new GameState("венгр", 6, logger);
        state.update("агент", "^^---");

        String hint = solver.getHint(state);
        assertTrue(hint.contains("а"));
        assertTrue(hint.contains("г"));
    }
}
