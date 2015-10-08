package com.ivanfoong.cache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by ivanfoong on 8/10/15.
 */
public class CacheTest {

    private TemporaryFolder mTemporaryFolder;
    private File cacheFolder;
    private Cache<String, String> cache;

    @Before
    public void setUp() throws Exception {
        mTemporaryFolder = new TemporaryFolder();
        mTemporaryFolder.create();
        cacheFolder = mTemporaryFolder.newFolder("cache");
        cache = new Cache<String, String>(cacheFolder, 100, true);
    }

    @After
    public void tearDown() throws Exception {
        cache = null;
        cacheFolder = null;
        mTemporaryFolder.delete();
        mTemporaryFolder = null;
    }

//    @Test
//    public void testGetLastCacheMissedCount() throws Exception {
//
//    }
//
//    @Test
//    public void testGetMemoryCacheCapacity() throws Exception {
//
//    }
//
//    @Test
//    public void testSetMemoryCacheCapacity() throws Exception {
//
//    }

    @Test
    public void testAll() throws Exception {
        cache.setMemoryCacheCapacity(1);

        final String key = "key";
        final String value = "value";
        cache.put(key, value);

        final String key2 = "key2";
        final String value2 = "value2";
        cache.put(key2, value2);

        final HashMap results = cache.all();
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(value, results.get(key));
        assertEquals(value2, results.get(key2));

        assertEquals(0, cache.getLastCacheMissedCount()); // should return 0 as calling all() will not use memory cache
    }

    @Test
    public void testGet() throws Exception {
        cache.setMemoryCacheCapacity(1);

        final String key = "key";
        final String value = "value";
        cache.put(key, value);

        final String key2 = "key2";
        final String value2 = "value2";
        cache.put(key2, value2);

        final Set<String> keys = new HashSet<String>();
        keys.add(key);
        keys.add(key2);
        final HashMap results = cache.get(keys);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(value, results.get(key));
        assertEquals(value2, results.get(key2));

        assertEquals(1, cache.getLastCacheMissedCount());
    }

    @Test
    public void testRemove() throws Exception {
        final String key = "key";
        final String value = "value";
        cache.put(key, value);

        final String key2 = "key2";
        final String value2 = "value2";
        cache.put(key2, value2);

        final Set<String> keys = new HashSet<String>();
        keys.add(key2);
        cache.remove(keys);
        assertEquals(1, cache.size());

        final Set<String> keysToAssert = new HashSet<String>();
        keysToAssert.add(key);
        assertEquals(value, cache.get(keysToAssert).get(key));
    }

    @Test
    public void testClear() throws Exception {
        final String key = "key";
        final String value = "value";
        cache.put(key, value);

        final String key2 = "key2";
        final String value2 = "value2";
        cache.put(key2, value2);

        cache.clear();
        assertEquals(0, cache.size());
    }

    @Test
    public void testPut() throws Exception {
        final String key = "key";
        final String value = "value";
        cache.put(key, value);

        assertEquals(1, cache.mMemoryCache.size());
        assertEquals(0, cache.getLastCacheMissedCount());
    }

    @Test
    public void testSize() throws Exception {
        final int capacity = 2;
        cache.setMemoryCacheCapacity(capacity);

        final String key = "key";
        final String value = "value";
        cache.put(key, value);

        final String key2 = "key2";
        final String value2 = "value2";
        cache.put(key2, value2);

        final String key3 = "key3";
        final String value3 = "value3";
        cache.put(key3, value3);

        assertEquals(capacity, cache.mMemoryCache.size());
        assertEquals(cache.mDiskCache.size(), cache.size());
        assertEquals(3, cache.size());
    }
}