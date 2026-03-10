package ru.yandex.practicum;

import java.util.*;

import static ru.yandex.practicum.WordleSymbol.EXACT;
import static ru.yandex.practicum.WordleSymbol.PRESENT;

public class WordleSolver {

    private final List<String> dictionary;
    private final Set<Character> presentLetters;
    private final Set<Character> absentLetters;
    private final char[] exactPositions;
    private final Map<Character, Set<Integer>> notInPositions;

    public WordleSolver(List<String> dictionary) {
        this.dictionary = dictionary;
        this.presentLetters = new HashSet<>();
        this.absentLetters = new HashSet<>();
        this.exactPositions = new char[5];
        this.notInPositions = new HashMap<>();
    }

    public String getHint(Map<String, String> userWords) {
        if (userWords.isEmpty()) {
            return dictionary.get(new Random().nextInt(dictionary.size()));
        }

        presentLetters.clear();
        absentLetters.clear();
        Arrays.fill(exactPositions, (char) 0);
        notInPositions.clear();

        for (Map.Entry<String, String> entry : userWords.entrySet()) {
            String word = entry.getKey();
            String feedback = entry.getValue();
            processFeedback(word, feedback);
        }

        List<String> candidates = new ArrayList<>();

        for (String word : dictionary) {
            if (userWords.containsKey(word)) {
                continue;
            }

            if (isCandidate(word)) {
                candidates.add(word);
            }
        }

        if (!candidates.isEmpty()) {
            return selectBestCandidate(candidates);
        } else {
            List<String> remaining = new ArrayList<>(dictionary);
            remaining.removeAll(userWords.keySet());
            if (remaining.isEmpty()) {
                return dictionary.get(new Random().nextInt(dictionary.size()));
            } else {
                return remaining.get(new Random().nextInt(remaining.size()));
            }
        }
    }

    private void processFeedback(String word, String feedback) {
        for (int i = 0; i < 5; i++) {
            char c = word.charAt(i);
            char f = feedback.charAt(i);

            if (f == EXACT.getSymbol()) {
                exactPositions[i] = c;
                presentLetters.add(c);

                Set<Integer> forbidden = notInPositions.get(c);
                if (forbidden != null) {
                    forbidden.remove(i);
                }
            } else if (f == PRESENT.getSymbol()) {
                presentLetters.add(c);
                notInPositions.computeIfAbsent(c, k -> new HashSet<>()).add(i);
            } else {
                if (!presentLetters.contains(c)) {
                    absentLetters.add(c);
                }
            }
        }
    }

    private boolean isCandidate(String word) {
        for (char c : absentLetters) {
            if (word.indexOf(c) >= 0) {
                return false;
            }
        }

        for (int i = 0; i < 5; i++) {
            if (exactPositions[i] != 0 && word.charAt(i) != exactPositions[i]) {
                return false;
            }
        }

        for (char c : presentLetters) {
            if (word.indexOf(c) < 0) {
                return false;
            }
        }

        for (Map.Entry<Character, Set<Integer>> entry : notInPositions.entrySet()) {
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

    private String selectBestCandidate(List<String> candidates) {
        String best = null;
        int maxNewLetters = -1;

        for (String word : candidates) {
            int newLetters = countNewLetters(word);
            if (newLetters > maxNewLetters) {
                maxNewLetters = newLetters;
                best = word;
            }
        }

        return best;
    }

    private int countNewLetters(String word) {
        Set<Character> newOnes = new HashSet<>();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!presentLetters.contains(c) && !absentLetters.contains(c)) {
                newOnes.add(c);
            }
        }
        return newOnes.size();
    }
}
