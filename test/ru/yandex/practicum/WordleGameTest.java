package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.GameExceptions;
import ru.yandex.practicum.exceptions.InvalidWord;
import ru.yandex.practicum.exceptions.WordAlreadyUsed;
import ru.yandex.practicum.exceptions.WordNotFoundInDictionary;

import static org.junit.jupiter.api.Assertions.*;

public class WordleGameTest {

    private WordleGame game;
    private final GameLogger logger = new GameLogger(System.out);

    @BeforeEach
    void beforeEach() throws GameExceptions {
        WordleDictionary dictionary = new WordleDictionary();
        dictionary.add("слово");
        dictionary.add("молот");
        dictionary.add("крона");
        dictionary.add("слива");
        game = new WordleGame(dictionary, logger);
        game.startGame("слово", 3);
    }

    @Test
    void shouldInitGame() {
        assertEquals(0, game.getSteps());
        assertFalse(game.isGameOver());
        assertFalse(game.isWin());
    }

    @Test
    void ShouldReturnFeedbackAndIncrementSteps() throws GameExceptions {
        String feedback = game.guess("молот");
        assertNotNull(feedback);
        assertEquals(1, game.getSteps());
    }

    @Test
    void ShouldThrowInvalidWordWhenGuess() {
        assertThrows(InvalidWord.class, () -> game.guess("дом"));
    }

    @Test
    void shouldThrowsWordNotFoundInDictionaryWhenGuess() {
        assertThrows(WordNotFoundInDictionary.class, () -> game.guess("ааааа"));
    }

    @Test
    void shouldThrowsWordAlreadyUsedWhenGuess() throws GameExceptions {
        game.guess("молот");
        assertThrows(WordAlreadyUsed.class, () -> game.guess("молот"));
    }

    @Test
    void shouldSetsWinAndGameOverAfterCorrectAnswer() throws GameExceptions {
        game.guess("слово");
        assertTrue(game.isWin());
        assertTrue(game.isGameOver());
    }

    @Test
    void shouldSetsGameOverWhenMaxStepsReached() throws GameExceptions {
        game.guess("слива");
        game.guess("крона");
        game.guess("молот");
        assertTrue(game.isGameOver());
        assertFalse(game.isWin());
    }

    @Test
    void shouldReturnHint() {
        String hint = game.getHint();
        assertNotNull(hint);
        assertEquals(5, hint.length());
    }

}
