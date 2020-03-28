package ru.itmo.java;

import java.util.Optional;

public class HashTable {

    private final static int INITIAL_CAPACITY = 1024;
    private final static int RESIZE_FACTOR = 2;
    private final static float INITIAL_LOAD_FACTOR = 0.5f;

    private Entry[] hashArray;
    private boolean[] deletedHashArray;
    private int size = 0;
    private int capacity;
    private float loadFactor;
    private int threshold;

    public HashTable() {
        this(INITIAL_CAPACITY, INITIAL_LOAD_FACTOR);
    }

    public HashTable(int capacity) {
        this(capacity, INITIAL_LOAD_FACTOR);
    }

    public HashTable(int capacity, float loadFactor) {
        this.hashArray = new Entry[capacity];
        this.deletedHashArray = new boolean[capacity];
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.threshold = (int) (capacity * loadFactor);
    }

    private void resize() {
        if (size >= threshold) {
            capacity *= RESIZE_FACTOR;
            threshold = (int) (capacity * loadFactor);

            Entry[] previousHashArray = hashArray;
            hashArray = new Entry[capacity];
            deletedHashArray = new boolean[capacity];
            size = 0;
            for (Entry pair : previousHashArray) {
                if (pair != null) {
                    put(pair.key, pair.value);
                }
            }
        }
    }

    private int hash(Object key) {
        int hash = Math.abs(key.hashCode() % capacity);
        int cycleCount = 0;
        while (deletedHashArray[hash]
                || (hashArray[hash] != null
                && !hashArray[hash].key.equals(key))
        ) {
            hash++;
            hash %= capacity;
            if (cycleCount++ >= size) {
                resize();
            }
        }
        return hash;
    }

    public Object put(Object key, Object value) {

        int hash = hash(key);

        if (hashArray[hash] == null) {

            hash = Math.abs(key.hashCode() % capacity);
            while (hashArray[hash] != null) {
                hash++;
                hash %= capacity;
            }

            deletedHashArray[hash] = false;
            hashArray[hash] = new Entry(key, value);
            size++;

            if (size >= threshold) {
                resize();
            }

            return null;
        }
        Object oldValue = hashArray[hash].value;
        hashArray[hash] = new Entry(key, value);
        return oldValue;
    }

    public Object get(Object key) {
        return Optional.ofNullable(hashArray[hash(key)]).map(Entry::getValue).orElse(null);
    }

    public Object remove(Object key) {
        int hash = hash(key);

        if (hashArray[hash] == null) {
            return null;
        }

        deletedHashArray[hash] = true;
        Object oldValue = hashArray[hash].value;
        size--;
        hashArray[hash] = null;
        return oldValue;
    }

    public int size() {
        return size;
    }

    private static class Entry {
        private final Object key;
        private Object value;

        public Entry(Object key, Object value) {
            this.key = key;
            setValue(value);
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

}