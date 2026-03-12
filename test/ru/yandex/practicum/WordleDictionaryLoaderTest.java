package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.EmptyDictionary;
import ru.yandex.practicum.exceptions.ProgramException;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class WordleDictionaryLoaderTest {

    private static final String TEST_WORDS_PATH = "test_words.txt";
    private static final String EMPTY_WORDS_PATH = "test_words_empty.txt";
    private static final String NON_EXIST_PATH = "non_exist.txt";

    private static GameLogger logger;

    @BeforeAll
    static void beforeEach() {
        logger = new GameLogger(System.out);
    }

    @Test
    void shouldLoadWords() throws IOException, ProgramException {
        WordleDictionary dictionary = new WordleDictionary();
        WordleDictionaryLoader loader = WordleDictionaryLoader.create(dictionary, logger, TEST_WORDS_PATH);
        assertEquals(3, dictionary.size());
        assertTrue(dictionary.getList().contains("слово"));
        assertTrue(dictionary.getList().contains("книга"));
        assertTrue(dictionary.getList().contains("молот"));
    }

    @Test
    void shouldThrowEmptyDictionary() {
        WordleDictionary dictionary = new WordleDictionary();
        assertThrows(EmptyDictionary.class, () ->
                WordleDictionaryLoader.create(dictionary, logger, EMPTY_WORDS_PATH)
        );
    }

    @Test
    void shouldThrowFileNotFoundException() {
        WordleDictionary dictionary = new WordleDictionary();
        assertThrows(FileNotFoundException.class, () ->
                WordleDictionaryLoader.create(dictionary, logger, NON_EXIST_PATH)
        );
    }

}
