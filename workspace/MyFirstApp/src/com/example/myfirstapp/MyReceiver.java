package com.example.myfirstapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class MyReceiver extends ResultReceiver {
    
    
    
    public MyReceiver(Handler handler) {
        super(handler);
    }
    

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        ProgressDialog mProgressDialog = MainActivity.mProgressDialog;
        super.onReceiveResult(resultCode, resultData);
        if (resultCode == DownloadService.UPDATE_PROGRESS) {
            int progress = resultData.getInt("progress");
            mProgressDialog.setProgress(progress);
            if (progress == 100) {
                mProgressDialog.dismiss();
            }
        }
    }
}

