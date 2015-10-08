package com.ivanfoong.cache.disk;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ivanfoong on 8/10/15.
 */
public class DiskCacheIndex implements Serializable {
    String mFilename;

    public DiskCacheIndex(final String aFilename) {
        mFilename = aFilename;
    }

    public String getFilename() {
        return mFilename;
    }
}
