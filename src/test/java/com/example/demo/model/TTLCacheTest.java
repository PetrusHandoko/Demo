package com.example.demo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TTLCacheTest {

    @Test
    void put() {
        TTLCache<String, String> ttlCache = new TTLCache<>(2000); // TTL of 2 seconds

        ttlCache.put("key1", "value1");
        assertEquals("value1", ttlCache.get("key1"));
    }

    @Test
    void get() {
        TTLCache<String, String> ttlCache = new TTLCache<>(2000); // TTL of 2 seconds
        assertNull(ttlCache.get("key1"));

        ttlCache.put("key1", "value1");
        assertEquals("value1", ttlCache.get("key1"));
    }

    @Test
    void remove() {
        TTLCache<String, String> ttlCache = new TTLCache<>(2000); // TTL of 2 seconds
        assertNull(ttlCache.get("key1"));

        ttlCache.put("key1", "value1");
        assertEquals("value1", ttlCache.get("key1"));
        ttlCache.remove("key1");
        assertNull(ttlCache.get("key1"));
    }

    @Test
   void TestTTLRemoval() throws InterruptedException {
        TTLCache<String, String> ttlCache = new TTLCache<>(2000); // TTL of 2 seconds

        ttlCache.put("key1", "value1");
        assertEquals("value1", ttlCache.get("key1"));

        Thread.sleep(3000);
        assertNull(ttlCache.get("key1"));
    }
}