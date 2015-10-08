package com.ivanfoong.cache.disk;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ivanfoong on 8/10/15.
 */
public class DiskCacheItem<T> implements Serializable {
    T mItem;

    public DiskCacheItem(final T aItem) {
        mItem = aItem;
    }

    public T getItem() {
        return mItem;
    }

    @Override public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof DiskCacheItem)) {
            return false;
        }

        DiskCacheItem<T> other = (DiskCacheItem<T>) o;
        return getItem().equals(other.getItem());
    }

    @Override public int hashCode() {
        return getItem().hashCode();
    }
}
