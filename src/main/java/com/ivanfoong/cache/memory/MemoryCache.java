package com.ivanfoong.cache.memory;

import com.ivanfoong.cache.ICache;

import java.io.Serializable;
import java.util.*;

/**
 * Created by ivanfoong on 8/10/15.
 */
public class MemoryCache<K extends Serializable, V extends Serializable> implements ICache<K, V> {

    int mCapacity;
    HashMap<K, MemoryCacheNode> mHashMap;
    MemoryCacheNode mHeadNode, mTailNode;

    public MemoryCache(final int aCapacity) {
        mHashMap = new HashMap<K, MemoryCacheNode>();
        mCapacity = aCapacity;
    }

    public synchronized HashMap<K, V> all() {
        return get(mHashMap.keySet());
    }

    public synchronized HashMap<K, V> get(Set<K> aKeys) {
        final HashMap<K, V> results = new HashMap<K, V>();
        for (final K key : aKeys) {
            final MemoryCacheNode node = getNode(key);
            if (node != null) {
                results.put(key, node.getValue());
            }
        }
        return results;
    }

    public synchronized ICache<K, V> remove(Set<K> aKeys) {
        for (final K key : aKeys) {
            final MemoryCacheNode node = getNode(key);
            if (node != null) {
                removeNode(node);
            }
        }

        return this;
    }

    public synchronized ICache<K, V> clear() {
        final HashSet<K> keys = new HashSet<K>();
        for (final K key : mHashMap.keySet()) {
            keys.add(key);
        }
        return remove(keys);
    }

    public synchronized ICache<K, V> put(K aKey, V aValue) {
        setNode(aKey, aValue);
        return this;
    }

    public synchronized int size() {
        return all().size();
    }

    synchronized MemoryCacheNode getNode(final K aKey) {
        if (mHashMap.containsKey(aKey)) {
            final MemoryCacheNode node = mHashMap.get(aKey);
            setHeadNode(node);
            return node;
        }
        return null;
    }

    synchronized void removeNode(final MemoryCacheNode aNode) {
        if (mHashMap.containsKey(aNode.getKey())) {
            mHashMap.remove(aNode.getKey());
        }

        final MemoryCacheNode previousNode = aNode.getPrevious();
        final MemoryCacheNode nextNode = aNode.getNext();

        if (previousNode != null) {
            previousNode.setNext(nextNode);
        }
        else {
            mHeadNode = nextNode;
        }

        if (nextNode != null) {
            nextNode.setPrevious(previousNode);
        }
        else {
            mTailNode = previousNode;
        }
    }

    synchronized void setHeadNode(final MemoryCacheNode aNode) {
        if (!mHashMap.containsKey(aNode.getKey())) {
            mHashMap.put(aNode.getKey(), aNode);
        }

        final MemoryCacheNode previousNode = aNode.getPrevious();
        final MemoryCacheNode nextNode = aNode.getNext();

        if (nextNode != null && previousNode != null) {
            previousNode.setNext(nextNode);
            nextNode.setPrevious(previousNode);
        }
        else if (previousNode != null) {
            previousNode.setNext(null);
        }
        else if (nextNode != null) {
            nextNode.setPrevious(null);
        }

        if (mTailNode == aNode) {
            mTailNode = previousNode;
        }

        aNode.setNext(mHeadNode);
        aNode.setPrevious(null);

        if (mHeadNode != null) {
            mHeadNode.setPrevious(aNode);
        }

        mHeadNode = aNode;

        if (mTailNode == null) {
            mTailNode = mHeadNode;
        }
    }

    synchronized void setNode(final K aKey, final V aValue) {
        if (mHashMap.containsKey(aKey)) {
            final MemoryCacheNode node = getNode(aKey);
            removeNode(node);
        }

        MemoryCacheNode node = new MemoryCacheNode(aKey, aValue);
        if (mHashMap.size() >= mCapacity) {
            removeNode(mTailNode);
        }
        setHeadNode(node);
    }

    public synchronized int getCapacity() {
        return mCapacity;
    }

    public synchronized void setCapacity(final int aCapacity) {
        mCapacity = aCapacity;
    }

    class MemoryCacheNode {
        K mKey;
        V mValue;
        MemoryCacheNode mPrevious;
        MemoryCacheNode mNext;

        MemoryCacheNode(final K aKey, final V aValue) {
            mKey = aKey;
            mValue = aValue;
        }

        public K getKey() {
            return mKey;
        }

        public MemoryCacheNode getNext() {
            return mNext;
        }

        public MemoryCacheNode getPrevious() {
            return mPrevious;
        }

        public void setNext(MemoryCacheNode aNext) {
            mNext = aNext;
        }

        public void setPrevious(MemoryCacheNode aPrevious) {
            mPrevious = aPrevious;
        }

        public V getValue() {
            return mValue;
        }
    }
}
