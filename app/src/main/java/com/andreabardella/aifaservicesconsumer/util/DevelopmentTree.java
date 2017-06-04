package com.andreabardella.aifaservicesconsumer.util;

import android.util.Log;

import timber.log.Timber;

public class DevelopmentTree extends Timber.DebugTree {
    private static final String TAG = DevelopmentTree.class.getSimpleName();

    private static final String LINE_NUMBER = "ln";
    private static final String THREAD_ID = "tId";

    public DevelopmentTree() {
        super();
        Log.i(TAG, "N.B.:"
                + " " + LINE_NUMBER + " stands for \"line number\""
                + " " + THREAD_ID + " stands for \"thread ID\"");
    }

    @Override
    protected String createStackElementTag(StackTraceElement element) {
        return super.createStackElementTag(element)
                + "[" + LINE_NUMBER + ":" + element.getLineNumber() + "]"
                + "[" + THREAD_ID + ":" + Thread.currentThread().getId() + "]";
    }
}
