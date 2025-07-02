package io.dcloud.common.adapter.util;

import android.os.AsyncTask;

/* loaded from: classes.dex */
public class AsyncTaskHandler {

    /* loaded from: classes.dex */
    public interface IAsyncTaskListener {
        void onCancel();

        void onExecuteBegin();

        void onExecuteEnd(Object obj);

        Object onExecuting();
    }

    public static void executeAsyncTask(IAsyncTaskListener iAsyncTaskListener, String[] strArr) {
        new MyAsyncTask(iAsyncTaskListener).execute(strArr);
    }

    /* loaded from: classes.dex */
    static class MyAsyncTask extends AsyncTask<String[], Integer, Object> {
        IAsyncTaskListener mListener;

        MyAsyncTask(IAsyncTaskListener iAsyncTaskListener) {
            this.mListener = null;
            this.mListener = iAsyncTaskListener;
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
            this.mListener.onExecuteBegin();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Object doInBackground(String[]... strArr) {
            return this.mListener.onExecuting();
        }

        @Override // android.os.AsyncTask
        protected void onPostExecute(Object obj) {
            super.onPostExecute(obj);
            this.mListener.onExecuteEnd(obj);
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
