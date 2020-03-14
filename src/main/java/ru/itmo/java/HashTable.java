package ru.itmo.java;

public class HashTable {

    private final static int INITIAL_CAPACITY = 1024;
    private final static int RESIZE_FACTOR = 2;
    private final static float INITIAL_LOAD_FACTOR = 0.5f;


    private Entry[] HashArray;
    private boolean[] DeletedHashArray;
    private int size = 0;
    private int capacity;
    private float loadFactor;
    private int threshold;

    private static class Entry {
        private Object key;
        private Object value;

        public Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

    }

    public HashTable() {
        this(INITIAL_CAPACITY, INITIAL_LOAD_FACTOR);
    }

    public HashTable(int capacity) {
        this(capacity, INITIAL_LOAD_FACTOR);
    }

    public HashTable(int capacity, float loadFactor) {

        this.HashArray = new Entry[capacity];
        this.DeletedHashArray = new boolean[capacity];
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.threshold = (int) Math.floor(capacity * loadFactor);
    }

    private void reSize() {
        if (this.size >= this.threshold) {
            this.capacity *= RESIZE_FACTOR;
            this.threshold = (int) (this.capacity * this.loadFactor);

            Entry[] previousHashArray = this.HashArray;
            this.HashArray = new Entry[this.capacity];
            this.DeletedHashArray = new boolean[this.capacity];
            this.size = 0;
            for (Entry pair : previousHashArray) {
                if (pair != null) {
                    this.put(pair.key, pair.value);
                }
            }
        }
    }

    private int getHash(Object key) {
        return Math.abs(key.hashCode() % this.capacity);
    }

    private int hash(Object key) {
        int hash = this.getHash(key);
        int cycleCount = 0;
        while (DeletedHashArray[hash]
                || (this.HashArray[hash] != null
                && !this.HashArray[hash].key.equals(key))
        ) {
            hash++;
            hash %= this.capacity;
            if (cycleCount++ >= size){
                reSize();
            }
        }
        return hash;
    }

    public Object put(Object key, Object value) {

        int hash = this.hash(key);

        if (this.HashArray[hash] == null) {

            hash = this.getHash(key);
            while (this.HashArray[hash] != null) {
                hash++;
                hash %= this.capacity;
            }

            DeletedHashArray[hash] = false;

            this.HashArray[hash] = new Entry(key, value);
            this.size++;

            if (this.size >= this.threshold) {
                this.reSize();
            }

            return null;
        }
        Object oldValue = this.HashArray[hash].value;
        this.HashArray[hash] = new Entry(key, value);
        return oldValue;
    }

    public Object get(Object key) {
        if (this.HashArray[this.hash(key)] == null) {
            return null;
        } else {
            return this.HashArray[this.hash(key)].value;
        }
    }

    public Object remove(Object key) {
        int hash = this.hash(key);

        if (this.HashArray[hash] == null) {
            return null;
        }

        this.DeletedHashArray[hash] = true;
        Object oldValue = this.HashArray[hash].value;
        this.size--;
        this.HashArray[hash] = null;
        return oldValue;
    }

    public int size() {
        return this.size;
    }

}