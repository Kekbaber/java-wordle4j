package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.ProgramException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    private static final String DEFAULT_PATH = "words_ru.txt";

    private final WordleDictionary dictionary;
    private GameLogger logger;

    private WordleDictionaryLoader(WordleDictionary dictionary, GameLogger logger) {
        this.dictionary = dictionary;
        this.logger = logger;
    }

    public static WordleDictionaryLoader create(WordleDictionary dictionary, GameLogger logger, String path) throws IOException, ProgramException {
        WordleDictionaryLoader loader = new WordleDictionaryLoader(dictionary, logger);
        loader.loadDictionary(path);
        return loader;
    }

    public static WordleDictionaryLoader create(WordleDictionary dictionary, GameLogger logger)
            throws IOException, ProgramException {
        return create(dictionary, logger, DEFAULT_PATH);
    }

    private void loadDictionary(String path) throws IOException, ProgramException {
        if (!Files.exists(Paths.get(path))) {
            String errorMsg = "Словарь не найден по указанному пути: " + path;
            logger.error(errorMsg);
            throw new FileNotFoundException(errorMsg);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                dictionary.add(line);
            }
            logger.info("Слов загружено: " + dictionary.size());
            dictionary.checkDictionaryEmpty();
        } catch (IOException e) {
            logger.error("Ошибка при загрузке словаря: " + e.getMessage());
            throw e;
        } catch (ProgramException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
