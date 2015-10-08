package com.ivanfoong.cache.memory;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ivanfoong on 8/10/15.
 */
public class MemoryCacheTest extends TestCase {

    static final int DEFAULT_MEMORY_SIZE = 10;
    MemoryCache<String, String> mMemoryCache;

    @Before
    public void setUp() throws Exception {
        mMemoryCache = new MemoryCache<String, String>(DEFAULT_MEMORY_SIZE);
    }

    @After
    public void tearDown() throws Exception {
        mMemoryCache = null;
    }

    @Test
    public void testAll() throws Exception {
        final String key = "key";
        final String value = "value";

        mMemoryCache.put(key, value);

        final HashMap results = mMemoryCache.all();
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(value, results.get(key));
    }

    @Test
    public void testGet() throws Exception {
        final String key = "key";
        final String value = "value";

        mMemoryCache.put(key, value);

        final Set<String> keys = new HashSet<String>();
        keys.add(key);
        final HashMap results = mMemoryCache.get(keys);
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(value, results.get(key));
    }

    @Test
    public void testRemove() throws Exception {
        final String key = "key";
        final String value = "value";

        mMemoryCache.put(key, value);

        final String key2 = "key2";
        final String value2 = "value2";
        mMemoryCache.put(key2, value2);

        final Set<String> keys = new HashSet<String>();
        keys.add(key);
        mMemoryCache.remove(keys);
        assertEquals(1, mMemoryCache.all().size());
    }

    @Test
    public void testClear() throws Exception {
        final String key = "key";
        final String value = "value";

        mMemoryCache.put(key, value);

        final String key2 = "key2";
        final String value2 = "value2";
        mMemoryCache.put(key2, value2);

        mMemoryCache.clear();
        assertEquals(0, mMemoryCache.all().size());
    }

    @Test
    public void testPut() throws Exception {
        final String key = "key";
        final String value = "value";

        mMemoryCache.put(key, value);

        assertEquals(1, mMemoryCache.mHashMap.size());
        assertEquals(value, mMemoryCache.mHashMap.get(key).mValue);
    }

    @Test
    public void testPutOverflow() throws Exception {
        final int capacity = 2;
        mMemoryCache.setCapacity(capacity);

        final String key = "key";
        final String value = "value";
        mMemoryCache.put(key, value);

        final String key2 = "key2";
        final String value2 = "value2";
        mMemoryCache.put(key2, value2);

        final Set<String> keys = new HashSet<String>();
        keys.add(key);
        mMemoryCache.get(keys);

        final String key3 = "key3";
        final String value3 = "value3";
        mMemoryCache.put(key3, value3);

        assertEquals(capacity, mMemoryCache.mHashMap.size());
        assertEquals(value, mMemoryCache.mHashMap.get(key).mValue);
        assertNull(mMemoryCache.mHashMap.get(key2));
        assertEquals(value3, mMemoryCache.mHashMap.get(key3).mValue);
    }

    @Test
     public void testSize() throws Exception {
        final String key = "key";
        final String value = "value";
        mMemoryCache.put(key, value);

        final String key2 = "key2";
        final String value2 = "value2";
        mMemoryCache.put(key2, value2);

        final String key3 = "key3";
        final String value3 = "value3";
        mMemoryCache.put(key3, value3);

        assertEquals(3, mMemoryCache.size());
    }

    @Test
    public void testSizeOverflow() throws Exception {
        final int capacity = 2;
        mMemoryCache.setCapacity(capacity);

        final String key = "key";
        final String value = "value";
        mMemoryCache.put(key, value);

        final String key2 = "key2";
        final String value2 = "value2";
        mMemoryCache.put(key2, value2);

        final String key3 = "key3";
        final String value3 = "value3";
        mMemoryCache.put(key3, value3);

        assertEquals(Math.min(capacity, 3), mMemoryCache.size());
    }

//    @Test
//    public void testGetNode() throws Exception {
//        assertTrue(false);
//    }
//
//    @Test
//    public void testRemoveNode() throws Exception {
//        assertTrue(false);
//    }
//
//    @Test
//    public void testSetHeadNode() throws Exception {
//        assertTrue(false);
//    }
//
//    @Test
//    public void testSetNode() throws Exception {
//        assertTrue(false);
//    }

    @Test
    public void testGetCapacity() throws Exception {
        assertEquals(mMemoryCache.mCapacity, mMemoryCache.getCapacity());
    }

    @Test
    public void testSetCapacity() throws Exception {
        final int capacity = this.DEFAULT_MEMORY_SIZE + 1;
        mMemoryCache.setCapacity(capacity);
        assertEquals(capacity, mMemoryCache.mCapacity);
    }
}