package com.ivanfoong.cache.disk;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by ivanfoong on 8/10/15.
 */
public class DiskCacheTest {

    private TemporaryFolder mTemporaryFolder;
    private File diskCacheFolder;
    private DiskCache<String, String> diskCache;

    @Before
    public void setUp() throws Exception {
        mTemporaryFolder = new TemporaryFolder();
        mTemporaryFolder.create();
        diskCacheFolder = mTemporaryFolder.newFolder("disk_cache");
        diskCache = new DiskCache<String, String>(diskCacheFolder);
    }

    @After
    public void tearDown() throws Exception {
        diskCache = null;
        diskCacheFolder = null;
        mTemporaryFolder.delete();
        mTemporaryFolder = null;
    }

    @Test
    public void testAll() throws Exception {
        final String key = "key";
        final String value = "value";
        diskCache.put(key, value);

        final HashMap results = diskCache.all();
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(value, results.get(key));
    }

    @Test
    public void testGet() throws Exception {
        final String key = "key";
        final String value = "value";

        diskCache.put(key, value);

        final Set<String> keys = new HashSet<String>();
        keys.add(key);
        final HashMap results = diskCache.get(keys);
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(value, results.get(key));
    }

    @Test
    public void testClear() throws Exception {
        final String key = "key";
        final String value = "value";
        diskCache.put(key, value);

        final String key2 = "key2";
        final String value2 = "value2";
        diskCache.put(key2, value2);

        diskCache.clear();
        assertEquals(0, diskCache.all().size());
    }

    @Test
    public void testRemove() throws Exception {
        final String key = "key";
        final String value = "value";
        diskCache.put(key, value);

        final String key2 = "key2";
        final String value2 = "value2";
        diskCache.put(key2, value2);

        final Set<String> keys = new HashSet<String>();
        keys.add(key);
        diskCache.remove(keys);
        assertEquals(1, diskCache.all().size());
    }

    @Test
    public void testPut() throws Exception {
        final String key = "key";
        final String value = "value";
        diskCache.put(key, value);
        final File[] files = diskCacheFolder.listFiles();
        assertEquals(2, files.length);

        final String filename = diskCache.generateFilename(key);
        assertEquals(filename, files[0].getName());

        try {
            InputStream file = new FileInputStream(files[0]);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            final HashMap result = (HashMap)input.readObject();

            HashMap<String, DiskCacheItem> expectedResult = new HashMap<String, DiskCacheItem>();
            expectedResult.put(key, new DiskCacheItem(value));
            assertEquals(expectedResult, result);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testSize() throws Exception {
        final String key = "key";
        final String value = "value";
        diskCache.put(key, value);

        final String key2 = "key2";
        final String value2 = "value2";
        diskCache.put(key2, value2);

        final String key3 = "key3";
        final String value3 = "value3";
        diskCache.put(key3, value3);

        assertEquals(3, diskCache.size());
    }

    @Test
    public void testPutPerformance() throws Exception {
        final long startTime = System.currentTimeMillis();

        final int count = 10000;

        for (int i = 0; i < count; i++) {
            final long iStartTime = System.currentTimeMillis();
            final String key = String.format("key.%d", i);
            final String value = String.format("value.%d", i);
            diskCache.put(key, value);
            final long iStopTime = System.currentTimeMillis();
            final long iRunTime = iStopTime - iStartTime;
            System.out.println(String.format("%d Run time: %d", i, iRunTime));
        }

        final long stopTime = System.currentTimeMillis();

        assertEquals(count, diskCache.mFileIndexes.size());

        final long runTime = stopTime - startTime;
        System.out.println("Run time: " + runTime);

        final File[] files = diskCacheFolder.listFiles();
        System.out.printf("Number of cache files, %d", files.length);
    }

    @Test
    public void putPerformance2() throws IOException {
        final DiskCache<ComplexObject, ComplexObject> complexObjectDiskCache = new DiskCache<ComplexObject, ComplexObject>(diskCacheFolder);

        final long startTime = System.currentTimeMillis();

        final int count = 10000;

        for (int i = 0; i < count; i++) {
            final long iStartTime = System.currentTimeMillis();
            final ComplexObject key = new ComplexObject(i);
            final ComplexObject value = new ComplexObject(i);
            complexObjectDiskCache.put(key, value);
            final long iStopTime = System.currentTimeMillis();
            final long iRunTime = iStopTime - iStartTime;
            System.out.println(String.format("%d Run time: %d", i, iRunTime));
        }

        final long stopTime = System.currentTimeMillis();

        assertEquals(count, complexObjectDiskCache.mFileIndexes.size());

        final long runTime = stopTime - startTime;
        System.out.println("Run time: " + runTime);

        final File[] files = diskCacheFolder.listFiles();
        System.out.printf("Number of cache files, %d", files.length);
    }

//    @Test
//    public void testGenerateFilename() throws Exception {
//        assertTrue(false);
//    }
//
//    @Test
//    public void testDeserializeFromFile() throws Exception {
//        assertTrue(false);
//    }
//
//    @Test
//    public void testSerializeToFile() throws Exception {
//        assertTrue(false);
//    }
//
//    @Test
//    public void testDeserializeFileIndex() throws Exception {
//        assertTrue(false);
//    }
//
//    @Test
//    public void testSerializeFileIndex() throws Exception {
//        assertTrue(false);
//    }
}