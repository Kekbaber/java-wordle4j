package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class WordleSolverTest {
    private WordleSolver solver;
    private List<String> dictionary;

    @BeforeEach
    void beforeEach() {
        dictionary = Arrays.asList("слова", "слово", "молот", "крона", "книга", "парта", "слива", "загиб");
        solver = new WordleSolver(dictionary);
    }

    @Test
    void testGetHintWithEmptyMapReturnsRandomWord() {
        String hint = solver.getHint(new HashMap<>());
        assertTrue(dictionary.contains(hint));
    }

    @Test
    void testGetHintWithWordReturnAnswer() {
        String answer = "молот";
        Map<String, String> used = new LinkedHashMap<>();
        used.put("холод", "-+++-");

        String hint = solver.getHint(used);
        assertNotNull(hint);

        assertEquals(answer, hint);
    }

    @Test
    void testGetHintWithExactPositions() {
        Map<String, String> used = new LinkedHashMap<>();
        used.put("слайд", "++---");
        List<String> filtered = new ArrayList<>();
        for (String word : dictionary) {
            if (word.length() == 5 && word.startsWith("сл")) {
                filtered.add(word);
            }
        }
        String hint = solver.getHint(used);
        assertTrue(filtered.contains(hint));
    }

    @Test
    void testGetHintDoesNotRepeatUsedWords() {
        Map<String, String> used = new LinkedHashMap<>();
        used.put("слова", "-----");
        used.put("слово", "-----");
        String hint = solver.getHint(used);
        assertNotEquals("слова", hint);
        assertNotEquals("слово", hint);
    }

    @Test
    void testGetHintWithAbsentLetters() {
        Map<String, String> used = new LinkedHashMap<>();
        used.put("молот", "-----");
        String hint = solver.getHint(used);
        for (char c : hint.toCharArray()) {
            assertFalse("молот".indexOf(c) >= 0);
        }
    }

    @Test
    void testGetHintWithPresentLetters() {
        Map<String, String> used = new LinkedHashMap<>();
        used.put("агент", "^^---");
        String hint = solver.getHint(used);
        assertTrue(hint.contains("а"));
        assertTrue(hint.contains("г"));
    }
}
