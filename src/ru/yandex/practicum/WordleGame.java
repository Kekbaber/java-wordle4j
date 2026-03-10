package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.GameExceptions;
import ru.yandex.practicum.exceptions.WordAlreadyUsed;

import java.util.*;

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
    private WordleSolver solver;
    private Map<String, String> usedWords;
    private final GameLogger logger;
    private String answer;
    private final int maxSteps;
    private int steps;
    private boolean isWin = false;
    private boolean isGameOver = false;

    public WordleGame(WordleDictionary dictionary, GameLogger logger) {
        this.dictionary = dictionary;
        this.logger = logger;
        this.maxSteps = DEFAULT_MAX_STEPS;
    }

    public WordleGame(WordleDictionary dictionary, GameLogger logger, int maxSteps) {
        this.dictionary = dictionary;
        this.logger = logger;
        this.maxSteps = maxSteps;
    }

    public void startGame() {
        init();
        generateNewAnswer();
        logger.info("Начало игры. Загаданное слово: " + answer);
    }

    public void startGame(String answer) throws GameExceptions {
        dictionary.checkWordValidate(answer);
        dictionary.checkWordExists(answer);
        init();
        this.answer = answer;
        logger.info("Начало игры. Загаданное слово: " + answer);
    }

    private void init() {
        usedWords = new LinkedHashMap<>();
        steps = 1;
        isWin = false;
        isGameOver = false;
        solver = new WordleSolver(dictionary.getList());
    }

    public void generateNewAnswer() {
        answer = dictionary.getRandomWord();
    }

    public String guess(String guess) throws GameExceptions {
        logger.info("Попытка " + steps + ": " + guess);

        guess = dictionary.normalizeWord(guess);

        dictionary.checkWordValidate(guess);
        dictionary.checkWordExists(guess);
        checkWordAlreadyUsed(guess);

        String feedback = dictionary.getFeedback(guess, answer);
        usedWords.put(guess, feedback);
        steps++;
        checkAnswer(guess);
        checkSteps();
        return feedback;
    }

    private void checkSteps() {
        if (steps > maxSteps) {
            logger.info("Поражение! Превышено количество попыток.");
            isGameOver = true;
        }
    }

    private void checkAnswer(String word) {
        if (word.equals(answer)) {
            logger.info("Слово угадано!");
            isGameOver = true;
            isWin = true;
        }
    }

    public String getHint() {
        logger.info("Запрос подсказки!");
        return solver.getHint(usedWords);
    }

    public String getAnswer() {
        return answer;
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

    private void checkWordAlreadyUsed(String guess) throws WordAlreadyUsed {
        if (usedWords.containsKey(guess)) {
            throw new WordAlreadyUsed();
        }
    }
}
