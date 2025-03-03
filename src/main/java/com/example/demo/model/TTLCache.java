package com.example.demo.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class TTLCache<K,V> {

    private final Map<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();
    private final long ttlMillis;

    public TTLCache(long ttlMillis) {
        this.ttlMillis = ttlMillis;
    }

    public void put(K key, V value) {
        cache.put(key, new CacheEntry<>(value, System.currentTimeMillis() + ttlMillis));
    }

    public V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            return entry.getValue();
        } else {
            cache.remove(key);
            return null;
        }
    }

    public void remove(K key) {
        cache.remove(key);
    }

    private class CacheEntry<Z> {
        private final Z value;
        private final long expiryTime;

        public CacheEntry(Z value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }

        public Z getValue() {
            return value;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() >= expiryTime;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TTLCache<String, String> ttlCache = new TTLCache<>(2000); // TTL of 2 seconds

        ttlCache.put("key1", "value1");
        System.out.println(ttlCache.get("key1")); // Output: value1

        Thread.sleep(3000);
        System.out.println(ttlCache.get("key1")); // Output: null
    }
}

