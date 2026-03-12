package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.GameExceptions;
import ru.yandex.practicum.exceptions.ProgramException;

import java.io.IOException;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    private static final String EXIT_COMMAND = "/q";
    private static final String RESET_COMMAND = "/r";
    private static final String LOGGER_NAME = "game.log";

    public static void main(String[] args) {
        gameLoop();
    }

    public static void gameLoop() {
        try (GameLogger logger = new GameLogger(LOGGER_NAME, false)) {
            WordleGame game = initGame(logger);

            Scanner scanner = new Scanner(System.in);

            String input;
            String feedback;
            int currentAttempt;

            printHello();
            printRules();
            game.startGame();

            mainLoop:
            while (!game.isGameOver()) {
                try {
                    currentAttempt = game.getSteps() + 1;
                    System.out.println("Попытка " + currentAttempt);
                    input = scanner.nextLine();

                    if (input.isBlank()) {
                        input = game.getHint();
                        System.out.println(input);
                    }

                    switch (input) {
                        case EXIT_COMMAND -> {
                            logger.info("Выход из игры");
                            break mainLoop;
                        }
                        case RESET_COMMAND -> {
                            restartGame(game, logger);
                            continue mainLoop;
                        }
                        default -> {
                            feedback = game.guess(input);
                            System.out.println(feedback);
                        }
                    }

                    if (game.isGameOver()) {
                        System.out.println(game.isWin() ? "Победа!" : "Правильный ответ: " + game.getAnswer());
                        System.out.println("Если хотите сыграть еще раз, введите: " + RESET_COMMAND);
                        input = scanner.nextLine();
                        if (input.equals(RESET_COMMAND)) {
                            restartGame(game, logger);
                            continue;
                        }
                        break;
                    }
                } catch (GameExceptions e) {
                    System.out.println(e.getMessage());
                }
            }
            printExit();
        } catch (Exception e) {
            System.out.println("Критическая ошибка: " + e.getMessage());
        }
    }

    public static WordleGame initGame(GameLogger logger) throws IOException, ProgramException {
        WordleDictionary dictionary = new WordleDictionary();
        WordleDictionaryLoader loader = WordleDictionaryLoader.create(dictionary, logger);
        return new WordleGame(dictionary, logger);
    }

    public static void printHello() {
        System.out.println("WORDLE");
    }

    public static void printRules() {
        System.out.println("правила игры:");
        System.out.println("отгадайте слово из 5 букв. У вас есть 6 попыток!");
        System.out.println("+ буква на правильном месте");
        System.out.println("^ буква есть в слове, но в другом месте");
        System.out.println("- буквы нет в слове");
        System.out.println("Нажмите Enter без ввода, чтобы получить подсказку");
        System.out.println("Введите " + EXIT_COMMAND + " для выхода из игры");
        System.out.println("Введите " + RESET_COMMAND + " для перезапуска игры");
    }

    public static void restartGame(WordleGame game, GameLogger logger) {
        logger.info("Перезапуск игры");
        printRules();
        game.startGame();
    }

    public static void printExit() {
        System.out.println("Завершение игры!");
    }
}
