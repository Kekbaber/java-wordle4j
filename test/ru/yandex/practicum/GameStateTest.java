package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.GameExceptions;
import ru.yandex.practicum.exceptions.WordAlreadyUsed;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    private GameState state;
    private final GameLogger logger = new GameLogger(System.out);

    @BeforeEach
    void beforeEach() {
        state = new GameState("слово", 3, logger);
    }

    @Test
    void updateShouldAddExactPosition() throws GameExceptions {
        state.update("слово", "+++++");
        assertEquals(Set.of('с', 'л', 'о', 'в'), state.getPresentLetters());
        assertTrue(state.getAbsentLetters().isEmpty());
        assertArrayEquals(new char[]{'с', 'л', 'о', 'в', 'о'}, state.getExactPositions());
        assertTrue(state.getNotInPositions().isEmpty());
    }

    @Test
    void updateShouldHandlePresentLetters() throws GameExceptions {
        state.update("волос", "^^^^^");
        assertEquals(Set.of('в', 'о', 'л', 'с'), state.getPresentLetters());
        assertEquals(Set.of(0), state.getNotInPositions().get('в'));
        assertEquals(Set.of(1, 3), state.getNotInPositions().get('о'));
        assertEquals(Set.of(2), state.getNotInPositions().get('л'));
        assertEquals(Set.of(4), state.getNotInPositions().get('с'));
    }

    @Test
    void updateShouldHandleAbsentLetters() throws GameExceptions {
        state.update("тумба", "----^");
        assertTrue(state.getAbsentLetters().contains('т'));
        assertTrue(state.getAbsentLetters().contains('у'));
        assertTrue(state.getAbsentLetters().contains('м'));
        assertEquals(Set.of(4), state.getNotInPositions().get('а'));
    }

    @Test
    void updateShouldSetsExact() throws GameExceptions {
        state.update("абвгд", "^----");
        state.update("а____", "+----");
        assertFalse(state.getNotInPositions().getOrDefault('а', Set.of()).contains(0));
    }

    @Test
    void updateShouldThrowWordAlreadyUsed() throws GameExceptions {
        state.update("молот", "-+---");
        assertThrows(WordAlreadyUsed.class, () -> state.update("молот", "-+---"));
    }

    @Test
    void updateShouldSetsGameOverWhenMaxStepsReached() throws GameExceptions {
        state.update("еееее", "-----");
        state.update("ууууу", "-----");
        state.update("ррррр", "-----");
        assertTrue(state.isGameOver());
        assertFalse(state.isWin());
        assertEquals(3, state.getSteps());
    }

    @Test
    void updateShouldThrowGameExceptionWhenOverMaxSteps() throws GameExceptions {
        state.update("еееее", "-----");
        state.update("ууууу", "-----");
        state.update("ррррр", "-----");
        assertThrows(GameExceptions.class, () -> state.update("ауауа", "-----"));
        assertTrue(state.isGameOver());
        assertFalse(state.isWin());
    }

    @Test
    void updateShouldThrowGameExceptionWhenCalledAgain() throws GameExceptions {
        state.update("слово", "+++++");
        assertThrows(GameExceptions.class, () -> state.update("слово", "+++++"));
        assertTrue(state.isGameOver());
        assertTrue(state.isWin());
    }

    @Test
    void updateShouldShouldSetsWinWhenRightAnswer() throws GameExceptions {
        state.update("слово", "+++++");
        assertTrue(state.isGameOver());
        assertTrue(state.isWin());
    }
}
