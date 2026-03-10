package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.GameExceptions;

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

    public static void main(String[] args) {
        gameLoop();
    }

    public static void gameLoop() {
        try (GameLogger logger = new GameLogger("game.log", false)) {
            Scanner scanner = new Scanner(System.in);
            WordleDictionary dictionary = new WordleDictionary();
            WordleDictionaryLoader loader = WordleDictionaryLoader.create(dictionary, logger);

            String input = "";
            String feedback;
            WordleGame game = new WordleGame(loader.getDictionary(), logger);
            game.startGame();

            printHello();
            printRules();
            while (!game.isGameOver()) {
                try {
                    System.out.println("Попытка " + game.getSteps());
                    input = scanner.nextLine();

                    if (input.equals(EXIT_COMMAND)) {
                        logger.info("Выход из игры");
                        break;
                    }

                    if (input.isBlank()) {
                        input = game.getHint();
                        System.out.println(input);
                    }

                    feedback = game.guess(input);

                    System.out.println(feedback);

                    if (game.isWin()) {
                        System.out.println("Победа!");
                        break;
                    } else if (game.isGameOver()) {
                        System.out.println("Правильный ответ: " + game.getAnswer());
                    }
                } catch (GameExceptions e) {
                    logger.warn(e.getMessage());
                    System.out.println(e.getMessage());
                }
            }
            printExit();
        } catch (Exception e) {
            System.out.println("Критическая ошибка: " + e.getMessage());
        }
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
    }

    public static void printExit() {
        System.out.println("Игра окончена!");
    }
}
