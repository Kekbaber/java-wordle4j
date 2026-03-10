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
    void testAddWordValidNormalized() {
        dictionary.add("кранЫ");
        assertEquals(1, dictionary.size());
        assertTrue(dictionary.getList().contains("краны"));
    }

    @Test
    void testAddWordValidReplaceToE() {
        dictionary.add("вёрст");
        assertEquals(1, dictionary.size());
        assertTrue(dictionary.getList().contains("верст"));
    }

    @Test
    void testAddWordInvalidLength() {
        dictionary.add("кот");
        assertEquals(0, dictionary.size());
    }

    @Test
    void testAddWordEnglishWord() {
        dictionary.add("mouse");
        assertEquals(0, dictionary.size());
    }

    @Test
    void testAddWordSpecChar() {
        dictionary.add("кросс");

        dictionary.add("не+слово");
        dictionary.add("ля-л!");
        assertEquals(1, dictionary.size());
    }

    @Test
    void testValidateWord() {
        assertTrue(dictionary.validateWord("молок"));
        assertTrue(dictionary.validateWord("слово"));
    }

    @Test
    void testValidateWordInvalid() {
        assertFalse(dictionary.validateWord("дом"));
        assertFalse(dictionary.validateWord("молоко"));
        assertFalse(dictionary.validateWord("ёлка"));
    }

    @Test
    void testValidateWordEnglish() {
        assertFalse(dictionary.validateWord("house"));
    }

    @Test
    void testValidateWordSpecChar() {
        assertFalse(dictionary.validateWord("с-лово"));
    }

    @Test
    void testNormalizeWord() {
        assertEquals("ежик", dictionary.normalizeWord("ёжик"));
        assertEquals("елка", dictionary.normalizeWord("ёлка"));
        assertEquals("медведь", dictionary.normalizeWord("медведь"));
    }

    @Test
    void testGetFeedbackExactMatched() {
        dictionary.add("столб");
        String feedback = dictionary.getFeedback("столб", "столб");
        assertEquals("+++++", feedback);
    }

    @Test
    void testGetFeedbackMixed() {
        dictionary.add("книга");
        String feedback = dictionary.getFeedback("аббат", "табак");
        assertEquals("^-++^", feedback);
    }

    @Test
    void testCheckWordValidateValid() throws InvalidWord {
        dictionary.checkWordValidate("слово");
    }

    @Test
    void testCheckWordValidateInvalidThrows() {
        assertThrows(InvalidWord.class, () -> dictionary.checkWordValidate("дом"));
    }

    @Test
    void testCheckWordExistsExists() throws WordNotFoundInDictionary {
        dictionary.add("слово");
        dictionary.checkWordExists("слово");
    }

    @Test
    void testCheckWordExistsNotExistsThrows() {
        assertThrows(WordNotFoundInDictionary.class, () -> dictionary.checkWordExists("слово"));
    }

    @Test
    void testCheckDictionaryEmptyThrows() {
        assertThrows(EmptyDictionary.class, () -> dictionary.checkDictionaryEmpty());
    }

    @Test
    void testCheckDictionaryEmptyNotEmptyValid() throws EmptyDictionary {
        dictionary.add("слово");
        dictionary.checkDictionaryEmpty();
    }
}
