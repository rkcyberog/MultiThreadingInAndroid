package com.rahul.multithreading;

import android.os.Message;

//Callback for pushing to ui thread
public interface UiThreadCallback {
    void pushToUiThread(Message message);
}
