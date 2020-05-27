# MultiThreadingInAndroid

Brief:

1. Used HandleThread to manage different tasks

2. Used two class CustomHandler and UiHandler within serviceWorker to manage callbacks on different Threads.

3. onExecute works async on background thread with repective instance of serviceWorker thread process.

4. Recieve Callback after executing tasks on onExecute to onTaskComplete in UiThreads with help of UiThreadCallback.
