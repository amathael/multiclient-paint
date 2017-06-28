package com.paint.global.utils;

import java.io.*;
import java.util.StringTokenizer;

/**
 * Created by jetbrains on 02/28/2017.
 */

public abstract class TargetedListener {

    private boolean completed;

    protected TargetedListener() {
        completed = false;
    }

    public abstract void answer(String value);

    protected void complete() {
        completed = true;
    }

    public boolean isCompleted() {
        return completed;
    }
}
