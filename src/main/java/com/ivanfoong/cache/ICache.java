package com.ivanfoong.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by ivanfoong on 8/10/15.
 */
public interface ICache<K extends Serializable, V extends Serializable> {
    HashMap<K, V> all();
    HashMap<K, V> get(final Set<K> aKeys);
    ICache<K, V> remove(final Set<K> aKeys);
    ICache<K, V> clear();
    ICache<K, V> put(final K aKey, V aValue);
    int size();
}
