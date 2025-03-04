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
            return entry.value();
        } else {
            cache.remove(key);
            return null;
        }
    }

    public void remove(K key) {
        cache.remove(key);
    }

    private record CacheEntry<Z>(Z value, long expiryTime) {

        public boolean isExpired() {
                return System.currentTimeMillis() >= expiryTime;
            }
        }
}

