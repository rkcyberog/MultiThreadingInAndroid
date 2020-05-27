package com.rahul.multithreading;

import android.graphics.Bitmap;

//Callback for respective operation
public interface Task<T> {
    T onExecuteTask();
    void onTaskComplete(Bitmap result);
}
