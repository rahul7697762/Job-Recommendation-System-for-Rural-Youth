package com.ruralyouth.dsa;

import java.util.*;

/**
 * Trie data structure for efficient string searching and autocomplete
 * Used for job titles and skills search
 */
public class Trie {
    private TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    /**
     * Insert a word into the trie
     * Time Complexity: O(m) where m is the length of the word
     */
    public void insert(String word) {
        if (word == null || word.isEmpty()) return;
        
        TrieNode current = root;
        String lowerWord = word.toLowerCase();
        
        for (char c : lowerWord.toCharArray()) {
            current.children.putIfAbsent(c, new TrieNode());
            current = current.children.get(c);
        }
        current.isEndOfWord = true;
        current.words.add(word); // Store original word for case preservation
    }

    /**
     * Search for a word in the trie
     * Time Complexity: O(m) where m is the length of the word
     */
    public boolean search(String word) {
        if (word == null || word.isEmpty()) return false;
        
        TrieNode current = root;
        String lowerWord = word.toLowerCase();
        
        for (char c : lowerWord.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return false;
            }
            current = current.children.get(c);
        }
        return current.isEndOfWord;
    }

    /**
     * Check if any word starts with the given prefix
     * Time Complexity: O(m) where m is the length of the prefix
     */
    public boolean startsWith(String prefix) {
        if (prefix == null || prefix.isEmpty()) return false;
        
        TrieNode current = root;
        String lowerPrefix = prefix.toLowerCase();
        
        for (char c : lowerPrefix.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return false;
            }
            current = current.children.get(c);
        }
        return true;
    }

    /**
     * Get all words that start with the given prefix (autocomplete)
     * Time Complexity: O(m + k) where m is prefix length, k is number of matching words
     */
    public List<String> getWordsWithPrefix(String prefix) {
        List<String> result = new ArrayList<>();
        if (prefix == null || prefix.isEmpty()) return result;
        
        TrieNode current = root;
        String lowerPrefix = prefix.toLowerCase();
        
        // Navigate to the node representing the prefix
        for (char c : lowerPrefix.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return result; // Prefix doesn't exist
            }
            current = current.children.get(c);
        }
        
        // Collect all words from this node and its descendants
        collectWords(current, result);
        return result;
    }

    /**
     * Collect all words from a given node and its descendants
     */
    private void collectWords(TrieNode node, List<String> result) {
        if (node.isEndOfWord) {
            result.addAll(node.words);
        }
        
        for (TrieNode child : node.children.values()) {
            collectWords(child, result);
        }
    }

    /**
     * Get all words in the trie
     */
    public List<String> getAllWords() {
        List<String> result = new ArrayList<>();
        collectWords(root, result);
        return result;
    }

    /**
     * Remove a word from the trie
     * Time Complexity: O(m) where m is the length of the word
     */
    public boolean remove(String word) {
        if (word == null || word.isEmpty()) return false;
        
        return removeHelper(root, word.toLowerCase(), 0);
    }

    private boolean removeHelper(TrieNode node, String word, int index) {
        if (index == word.length()) {
            if (!node.isEndOfWord) {
                return false; // Word doesn't exist
            }
            node.isEndOfWord = false;
            node.words.clear();
            return node.children.isEmpty();
        }
        
        char c = word.charAt(index);
        TrieNode child = node.children.get(c);
        
        if (child == null) {
            return false; // Word doesn't exist
        }
        
        boolean shouldDeleteChild = removeHelper(child, word, index + 1);
        
        if (shouldDeleteChild) {
            node.children.remove(c);
            return node.children.isEmpty() && !node.isEndOfWord;
        }
        
        return false;
    }

    /**
     * Get the size of the trie (number of words)
     */
    public int size() {
        return getAllWords().size();
    }

    /**
     * Check if the trie is empty
     */
    public boolean isEmpty() {
        return root.children.isEmpty();
    }

    /**
     * Clear all words from the trie
     */
    public void clear() {
        root = new TrieNode();
    }

    /**
     * TrieNode inner class
     */
    private static class TrieNode {
        Map<Character, TrieNode> children;
        boolean isEndOfWord;
        Set<String> words; // Store original words for case preservation

        TrieNode() {
            this.children = new HashMap<>();
            this.isEndOfWord = false;
            this.words = new HashSet<>();
        }
    }
} 