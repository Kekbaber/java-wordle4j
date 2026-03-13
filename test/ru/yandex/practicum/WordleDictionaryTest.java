package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.EmptyDictionary;
import ru.yandex.practicum.exceptions.InvalidWord;
import ru.yandex.practicum.exceptions.WordNotFoundInDictionary;

import static org.junit.jupiter.api.Assertions.*;

public class WordleDictionaryTest {

    private WordleDictionary dictionary;

    @BeforeEach
    void beforeEach() {
        dictionary = new WordleDictionary();
    }

    @Test
    void shouldAddNormalizedWord() {
        dictionary.add("кранЫ");
        assertEquals(1, dictionary.size());
        assertTrue(dictionary.getList().contains("краны"));
    }

    @Test
    void shouldAddReplaceToE() {
        dictionary.add("вёрст");
        assertEquals(1, dictionary.size());
        assertTrue(dictionary.getList().contains("верст"));
    }

    @Test
    void shouldNotAddInvalidWord() {
        dictionary.add("кот");
        dictionary.add("mouse");
        dictionary.add("не+слово");
        dictionary.add("ля-л!");
        assertEquals(0, dictionary.size());

        dictionary.add("кросс");
        assertEquals(1, dictionary.size());
    }


    @Test
    void shouldValidateWord() {
        assertTrue(dictionary.validateWord("молок"));
        assertTrue(dictionary.validateWord("слово"));
    }

    @Test
    void shouldNotValidateInvalidWord() {
        assertFalse(dictionary.validateWord("дом"));
        assertFalse(dictionary.validateWord("молоко"));
        assertFalse(dictionary.validateWord("ёлка"));
    }

    @Test
    void shouldNotValidateEnglishWord() {
        assertFalse(dictionary.validateWord("house"));
    }

    @Test
    void shouldNotValidateWordWithSpecChar() {
        assertFalse(dictionary.validateWord("с-лово"));
    }

    @Test
    void shouldNormalizeWord() {
        assertEquals("ежик", dictionary.normalizeWord("ёжик"));
        assertEquals("елка", dictionary.normalizeWord("ёлка"));
        assertEquals("медведь", dictionary.normalizeWord("медведь"));
    }

    @Test
    void shouldReturnExactFeedback() {
        dictionary.add("столб");
        String feedback = dictionary.getFeedback("столб", "столб");
        assertEquals("+++++", feedback);
    }

    @Test
    void shouldReturnMixedFeedback() {
        dictionary.add("книга");
        String feedback = dictionary.getFeedback("аббат", "табак");
        assertEquals("^-++^", feedback);
    }

    @Test
    void shouldDoseNotThrowWhenValidate() {
        assertDoesNotThrow(() -> dictionary.checkWordValidate("слово"));
    }

    @Test
    void shouldThrowWhenValidate() {
        assertThrows(InvalidWord.class, () -> dictionary.checkWordValidate("дом"));
    }

    @Test
    void shouldDoseNotThrowWhenCheckWordExists() {
        dictionary.add("слово");
        assertDoesNotThrow(() -> dictionary.checkWordExists("слово"));
    }

    @Test
    void shouldThrowWhenCheckWordNotExist() {
        assertThrows(WordNotFoundInDictionary.class, () -> dictionary.checkWordExists("слово"));
    }

    @Test
    void shouldThrowWhenCheckEmptyDictionary() {
        assertThrows(EmptyDictionary.class, () -> dictionary.checkDictionaryEmpty());
    }

    @Test
    void shouldDoesNotThrowWhenCheckNotEmpty() throws EmptyDictionary {
        dictionary.add("слово");
        dictionary.checkDictionaryEmpty();
    }
}
