package com.ivanfoong.cache.disk;

import com.ivanfoong.cache.ICache;

import java.io.*;
import java.util.*;

/**
 * Created by ivanfoong on 8/10/15.
 */
public class DiskCache<K extends Serializable, V extends Serializable> implements ICache<K, V> {

    File mCacheDirectory;
    HashMap<K, DiskCacheIndex> mFileIndexes;

    public DiskCache(final File aCacheDirectory) {
        mCacheDirectory = aCacheDirectory;
        mCacheDirectory.mkdirs();
        mFileIndexes = deserializeFileIndex();
    }

    public File getCacheDirectory() {
        return mCacheDirectory;
    }

    public synchronized HashMap all() {
        return get(mFileIndexes.keySet());
    }

    public synchronized HashMap get(Set<K> aKeys) {
        final HashMap<K, V> results = new HashMap<K, V>();
        if (getCacheDirectory() != null) {
            final HashSet<K> expiredKeys = new HashSet<K>();

            for (K key : aKeys) {
                final DiskCacheIndex fileIndex = mFileIndexes.get(key);
                final File file = new File(getCacheDirectory(), fileIndex.getFilename());

                if (file.canRead() && file.isFile()) {
                    final HashMap<K, DiskCacheItem<V>> serializedObject = deserializeFromFile(file);
                    final DiskCacheItem<V> diskCacheItem = serializedObject.get(key);
                    results.put(key, diskCacheItem.getItem()); // ignoring expiry date from DiskCacheItem as expiry has already been checked
                }
            }

            // remove expired item
            remove(expiredKeys);
        }
        return results;
    }

    public synchronized ICache<K, V> clear() {
        return remove(mFileIndexes.keySet());
    }

    public synchronized ICache remove(Set<K> aKeys) {
        if (getCacheDirectory() != null) {
            HashSet<K> keysToRemove = new HashSet<K>();
            for (K key : aKeys) {
                final DiskCacheIndex diskCacheIndex = mFileIndexes.get(key);
                final File file = new File(getCacheDirectory(), diskCacheIndex.getFilename());

                if (file.canWrite() && file.isFile()) {
                    final HashMap<K, DiskCacheItem<V>> serializedObject = deserializeFromFile(file);
                    if (serializedObject.containsKey(key)) {
                        if (serializedObject.size() > 1) {
                            // remove this key and save the remaining hashmap
                            serializedObject.remove(key);
                            serializeToFile(file, serializedObject);
                        } else {
                            // remove this file
                            file.delete();
                        }
                    }
                }

                keysToRemove.add(key);
            }

            for (K key : keysToRemove) {
                mFileIndexes.remove(key);
            }
            this.serializeFileIndex(mFileIndexes);
        }
        return this;
    }

    public synchronized ICache<K, V> put(K aKey, V aValue) {
        if (getCacheDirectory() != null) {
            final String filename = generateFilename(aKey);
            final File valueFile = new File(getCacheDirectory(), filename);
            HashMap<K, DiskCacheItem<V>> value = deserializeFromFile(valueFile);
            if (value == null) {
                value = new HashMap<K, DiskCacheItem<V>>();
            }
            value.put(aKey, new DiskCacheItem(aValue));
            serializeToFile(valueFile, value);

            final DiskCacheIndex fileIndex = new DiskCacheIndex(filename);

            mFileIndexes.put(aKey, fileIndex);

            this.serializeFileIndex(mFileIndexes);
        }
        return this;
    }

    public int size() {
        return mFileIndexes.size();
    }

    String generateFilename(final Serializable aSerializable) {
        final String filename = String.format("%d.ser", aSerializable.hashCode());
        return filename;
    }

    synchronized HashMap<K, DiskCacheItem<V>> deserializeFromFile(final File aFile) {
        HashMap<K, DiskCacheItem<V>> serializedObject = null;

        if (aFile.exists() && aFile.canRead()) {
            try {
                final InputStream file = new FileInputStream(aFile);
                final InputStream buffer = new BufferedInputStream(file);
                final ObjectInput input = new ObjectInputStream(buffer);

                serializedObject = (HashMap) input.readObject();

                input.close();
                buffer.close();
                file.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return serializedObject;
    }

    synchronized void serializeToFile(final File aFile, final HashMap<K, DiskCacheItem<V>> aObject) {
        try {
            OutputStream file = new FileOutputStream(aFile);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);

            output.writeObject(aObject);

            output.flush();
            buffer.flush();
            file.flush();
            output.close();
            buffer.close();
            file.close();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            // TODO
        }
        catch (IOException e) {
            e.printStackTrace();
            // TODO
        }
    }

    synchronized HashMap<K, DiskCacheIndex> deserializeFileIndex() {
        final File indexFile = new File(getCacheDirectory(), "index.ser");
        HashMap<K, DiskCacheIndex> serializedObject = null;

        if (indexFile.exists() && indexFile.canRead()) {
            try {
                final InputStream file = new FileInputStream(indexFile);
                final InputStream buffer = new BufferedInputStream(file);
                final ObjectInput input = new ObjectInputStream(buffer);

                serializedObject = (HashMap) input.readObject();

                input.close();
                buffer.close();
                file.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            serializedObject = new HashMap<K, DiskCacheIndex>();
        }

        return serializedObject;
    }

    synchronized void serializeFileIndex(final HashMap<K, DiskCacheIndex> aObject) {
        final File indexFile = new File(getCacheDirectory(), "index.ser");
        try {
            OutputStream file = new FileOutputStream(indexFile);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);

            output.writeObject(aObject);

            output.flush();
            buffer.flush();
            file.flush();
            output.close();
            buffer.close();
            file.close();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            // TODO
        }
        catch (IOException e) {
            e.printStackTrace();
            // TODO
        }
    }
}
