package ru.yandex.practicum;

import java.util.*;

public class WordleSolver {
    private final List<String> dictionary;

    public WordleSolver(List<String> dictionary) {
        this.dictionary = dictionary;
    }

    public String getHint(GameState state) {
        List<String> candidates = new ArrayList<>();

        for (String word : dictionary) {
            if (state.isWordUsed(word)) {
                continue;
            }
            if (isCandidate(word,
                    state.getPresentLetters(),
                    state.getAbsentLetters(),
                    state.getExactPositions(),
                    state.getNotInPositions())) {
                candidates.add(word);
            }
        }

        if (state.getUsedWords().isEmpty()) {
            return dictionary.get(new Random().nextInt(dictionary.size()));
        }

        if (!candidates.isEmpty()) {
            return selectBestCandidate(candidates,
                    state.getPresentLetters(),
                    state.getAbsentLetters());
        } else {
            List<String> remaining = new ArrayList<>(dictionary);
            remaining.removeAll(state.getUsedWords());
            if (remaining.isEmpty()) {
                return dictionary.get(new Random().nextInt(dictionary.size()));
            } else {
                return remaining.get(new Random().nextInt(remaining.size()));
            }
        }
    }

    private boolean isCandidate(String word,
                                Set<Character> present,
                                Set<Character> absent,
                                char[] exact,
                                Map<Character, Set<Integer>> notInPosition) {
        for (char c : absent) {
            if (word.indexOf(c) >= 0) {
                return false;
            }
        }

        for (int i = 0; i < 5; i++) {
            if (exact[i] != 0
                    && word.charAt(i) != exact[i]) {
                return false;
            }
        }

        for (char c : present) {
            if (word.indexOf(c) < 0) {
                return false;
            }
        }

        for (Map.Entry<Character, Set<Integer>> entry : notInPosition.entrySet()) {
            char c = entry.getKey();
            Set<Integer> forbidden = entry.getValue();
            for (int pos : forbidden) {
                if (word.charAt(pos) == c) {
                    return false;
                }
            }
        }

        return true;
    }

    private String selectBestCandidate(List<String> candidates,
                                       Set<Character> present,
                                       Set<Character> absent) {
        String best = null;
        int maxNewLetters = -1;

        for (String word : candidates) {
            int newLetters = countNewLetters(word, present, absent);
            if (newLetters > maxNewLetters) {
                maxNewLetters = newLetters;
                best = word;
            }
        }

        return best;
    }

    private int countNewLetters(String word,
                                Set<Character> present,
                                Set<Character> absent) {
        Set<Character> newOnes = new HashSet<>();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!present.contains(c) && !absent.contains(c)) {
                newOnes.add(c);
            }
        }
        return newOnes.size();
    }
}
