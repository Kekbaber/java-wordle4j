package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.GameExceptions;
import ru.yandex.practicum.exceptions.InvalidWord;
import ru.yandex.practicum.exceptions.WordAlreadyUsed;
import ru.yandex.practicum.exceptions.WordNotFoundInDictionary;

import static org.junit.jupiter.api.Assertions.*;

public class WordleGameTest {

    private WordleDictionary dictionary;
    private WordleGame game;
    private GameLogger logger;

    @BeforeEach
    void beforeEach() throws GameExceptions {
        dictionary = new WordleDictionary();
        dictionary.add("слово");
        dictionary.add("молот");
        dictionary.add("крона");
        dictionary.add("слива");
        logger = new GameLogger(System.out);
        game = new WordleGame(dictionary, logger, 3);
        game.startGame("слово");
    }

    @Test
    void testStartGameInit() {
        assertEquals(1, game.getSteps());
        assertFalse(game.isGameOver());
        assertFalse(game.isWin());
    }

    @Test
    void testGameIncrementStepsAndReturnFeedback() throws GameExceptions {
        String feedback = game.guess("молот");
        assertNotNull(feedback);
        assertEquals(2, game.getSteps());
    }

    @Test
    void testGuessThrowsInvalidWordException() {
        assertThrows(InvalidWord.class, () -> game.guess("дом"));
    }

    @Test
    void testGuessThrowsWordNotFoundInDictionary() {
        assertThrows(WordNotFoundInDictionary.class, () -> game.guess("ааааа"));
    }

    @Test
    void testGuessThrowWordAlreadyUsed() throws GameExceptions {
        game.guess("молот");
        assertThrows(WordAlreadyUsed.class, () -> game.guess("молот"));
    }

    @Test
    void testSetsWinAndGameOverAfterCorrectAnswer() throws GameExceptions {;
        game.guess("слово");
        assertTrue(game.isWin());
        assertTrue(game.isGameOver());
    }

    @Test
    void testGuessMaxStepsReachedSetsGameOver() throws GameExceptions {
        game.guess("слива");
        game.guess("крона");
        game.guess("молот");
        assertTrue(game.isGameOver());
        assertFalse(game.isWin());
    }

    @Test
    void testGetHintReturnsWord() {
        String hint = game.getHint();
        assertNotNull(hint);
        assertEquals(5, hint.length());
    }

}
