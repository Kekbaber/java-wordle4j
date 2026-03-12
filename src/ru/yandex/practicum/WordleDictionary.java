package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.EmptyDictionary;
import ru.yandex.practicum.exceptions.InvalidWord;
import ru.yandex.practicum.exceptions.WordNotFoundInDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static ru.yandex.practicum.WordleSymbol.*;


/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private static final int WORD_LENGTH = 5;

    private final List<String> words;

    public WordleDictionary() {
        this.words = new ArrayList<>();
    }

    public void add(String word) {
        word = normalizeWord(word);
        if (validateWord(word)) {
            words.add(word);
        }
    }

    public boolean validateWord(String word) {
        return word.length() == WORD_LENGTH
                && !word.contains("-")
                && word.matches("[а-я]+");
    }

    public String normalizeWord(String word) {
        return word.toLowerCase().replace('ё', 'е');
    }

    public String getRandomWord() {
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    public String getFeedback(String guess, String target) {
        char[] result = new char[WORD_LENGTH];
        boolean[] targetUsed = new boolean[WORD_LENGTH];

        for (int i = 0; i < WORD_LENGTH; i++) {
            if (guess.charAt(i) == target.charAt(i)) {
                result[i] = EXACT.getSymbol();
                targetUsed[i] = true;
            } else {
                result[i] = ABSENT.getSymbol();
            }
        }

        for (int i = 0; i < WORD_LENGTH; i++) {
            if (result[i] == EXACT.getSymbol()) continue; // уже обработано

            char c = guess.charAt(i);
            for (int j = 0; j < WORD_LENGTH; j++) {
                if (!targetUsed[j] && target.charAt(j) == c) {
                    result[i] = PRESENT.getSymbol();
                    targetUsed[j] = true;
                    break;
                }
            }
        }

        return new String(result);
    }

    public void checkWordValidate(String word) throws InvalidWord {
        if (!validateWord(word)) {
            throw new InvalidWord(word, WORD_LENGTH);
        }
    }

    public void checkWordExists(String word) throws WordNotFoundInDictionary {
        if (!words.contains(word)) {
            throw new WordNotFoundInDictionary(word);
        }
    }

    public void checkDictionaryEmpty() throws EmptyDictionary {
        if (words.isEmpty()) {
            throw new EmptyDictionary("Словарь пуст.");
        }
    }

    public List<String> getList() {
        return Collections.unmodifiableList(words);
    }

    public int size() {
        return words.size();
    }
}
