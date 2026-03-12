package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.GameExceptions;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private static final int DEFAULT_MAX_STEPS = 6;

    private final WordleDictionary dictionary;
    private final WordleSolver solver;
    private final GameLogger logger;

    private GameState state;

    public WordleGame(WordleDictionary dictionary, GameLogger logger) {
        this.dictionary = dictionary;
        this.logger = logger;
        solver = new WordleSolver(dictionary.getList());
    }

    public void startGame() {
        String answer = dictionary.getRandomWord();
        state = new GameState(answer, DEFAULT_MAX_STEPS, logger);
    }

    public void startGame(String answer, int maxSteps) throws GameExceptions {
        dictionaryValidator(answer);
        state = new GameState(answer, maxSteps, logger);
    }

    public String guess(String guess) throws GameExceptions {
        int step = state.getSteps() + 1;
        logger.info("Попытка " + step + ": " + guess);
        guess = dictionary.normalizeWord(guess);

        try {
            dictionaryValidator(guess);
            state.checkWordAlreadyUsed(guess);
            String feedback = dictionary.getFeedback(guess, state.getAnswer());
            logger.info("Фидбек: " + feedback);
            state.update(guess, feedback);
            return feedback;
        } catch (GameExceptions e) {
            logger.warn("Ошибка при попытке %s: %s", step, e.getMessage());
            throw e;
        }
    }

    public void dictionaryValidator(String word) throws GameExceptions {
        dictionary.checkWordValidate(word);
        dictionary.checkWordExists(word);
    }

    public String getHint() {
        logger.info("Запрос подсказки!");
        return solver.getHint(state);
    }

    public String getAnswer() {
        return state.getAnswer();
    }

    public int getSteps() {
        return state.getSteps();
    }

    public boolean isWin() {
        return state.isWin();
    }

    public boolean isGameOver() {
        return state.isGameOver();
    }
}
