package com.ivanfoong.cache;

import com.ivanfoong.cache.disk.DiskCache;
import com.ivanfoong.cache.memory.MemoryCache;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by ivanfoong on 8/10/15.
 */
public class Cache<K extends Serializable, V extends Serializable> implements ICache<K, V> {

    final MemoryCache<K, V> mMemoryCache;
    final DiskCache<K, V> mDiskCache;
    int mLastCacheMissedCount = 0;
    boolean mDebug;

    public Cache(final File aCacheDirectory, final int aMemoryCacheCapacity, final boolean aDebug) {
        mMemoryCache = new MemoryCache<K, V>(aMemoryCacheCapacity);
        mDiskCache = new DiskCache<K, V>(aCacheDirectory);
        mDebug = aDebug;
    }

    public Cache(final File aCacheDirectory, final int aMemoryCacheCapacity) {
        this(aCacheDirectory, aMemoryCacheCapacity, false);
    }

    public Cache(final File aCacheDirectory) {
        this(aCacheDirectory, 100); // defaults to 100 items in memory
    }

    /*
    Note: calling all() will cache miss for all items as fetching all items will not use memory cache
     */
    public int getLastCacheMissedCount() {
        return mLastCacheMissedCount;
    }

    public int getMemoryCacheCapacity() {
        return mMemoryCache.getCapacity();
    }

    public void setMemoryCacheCapacity(int capacity) {
        mMemoryCache.setCapacity(capacity);
    }

    public HashMap<K, V> all() {
        mLastCacheMissedCount = 0;
        return mDiskCache.all(); // memory cache will not help in this usage, so retrieving all items from disk
    }

    public HashMap<K, V> get(Set<K> aKeys) {
        final HashMap<K, V> results = new HashMap<K, V>();
        final HashMap<K, V> memoryCacheResults = mMemoryCache.get(aKeys);

        aKeys.removeAll(memoryCacheResults.keySet());
        if (mDebug) {
            mLastCacheMissedCount = aKeys.size();
        }
        else {
            mLastCacheMissedCount = 0;
        }

        final HashMap<K, V> diskCacheResults = mDiskCache.get(aKeys);

        // add memory cache missed items into memory cache
        for (final K key : diskCacheResults.keySet()) {
            mMemoryCache.put(key, diskCacheResults.get(key));
        }

        results.putAll(memoryCacheResults);
        results.putAll(diskCacheResults);

        return results;
    }

    public ICache<K, V> remove(Set<K> aKeys) {
        mMemoryCache.remove(aKeys);
        mDiskCache.remove(aKeys);
        return this;
    }

    public ICache<K, V> clear() {
        mMemoryCache.clear();
        mDiskCache.clear();
        return this;
    }

    public ICache<K, V> put(K aKey, V aValue) {
        mMemoryCache.put(aKey, aValue);
        mDiskCache.put(aKey, aValue);
        return this;
    }

    public int size() {
        return mDiskCache.size();
    }
}
