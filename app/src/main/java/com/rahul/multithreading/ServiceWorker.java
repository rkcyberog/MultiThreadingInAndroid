package com.rahul.multithreading;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.lang.ref.WeakReference;


public class ServiceWorker extends HandlerThread implements UiThreadCallback {
    String name;
    private UiHandler mUiHandler;
    CustomHandler mHandler;
    private WeakReference<Task> taskCallback;
    private WeakReference<UiThreadCallback> mUiThreadCallback;

    public ServiceWorker(String name) {
        super(name, android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mUiHandler = new UiHandler(Looper.getMainLooper());
        this.mUiThreadCallback = new WeakReference<UiThreadCallback>(this);
        this.name = name;
    }

    @Override
    protected void onLooperPrepared() {
            super.onLooperPrepared();
            mHandler = new CustomHandler(getLooper());
    }


    public void setTaskCallback(Task callback){
        this.taskCallback = new WeakReference<Task>(callback);
    }


    public void addTask(Task<Bitmap> task) {
        if(mHandler != null) {
            mHandler.sendEmptyMessage(1);
        }
    }


    @Override
    public void pushToUiThread(Message message) {
        if(mUiHandler != null){

            mUiHandler.sendMessage(message);
        }
    }

    //Background Process
    private class CustomHandler extends Handler {
        public CustomHandler(Looper looper) {
            super(looper);
        }


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    try {
                        Log.i("CustomHandler ", String.valueOf(Thread.currentThread().getId()));
                         Object bitmap = taskCallback.get().onExecuteTask();
                         Log.i("msg", "handler");
                        if(!Thread.interrupted() && taskCallback != null && taskCallback.get() != null){
                            Message message = Util.createMessage(1, (Bitmap) bitmap);
                            mUiThreadCallback.get().pushToUiThread(message);

                        }
                    } catch (Exception e){
                        Log.e(Util.LOG_TAG,e.toString());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    //Pushing to UI
    private  class UiHandler extends Handler {

        public UiHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Util.ID:
                    Log.i("UiHandler ", String.valueOf(Thread.currentThread().getId()));
                    Bundle bundle = msg.getData();
                    Bitmap bitmap = bundle.getParcelable(Util.DATA);
                        taskCallback.get().onTaskComplete(bitmap);
                    break;
                default:
                    break;
            }
        }
    }
}
