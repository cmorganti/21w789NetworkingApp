package com.example.myfirstapp;

import java.util.Timer;
import java.util.TimerTask;

import android.app.DownloadManager;
import android.database.Cursor;
import android.util.Log;




public class DownloadTimer {
    long managerID;
    DownloadManager manager;
    long startTime;
    Timer timer;
    int bytes_downloaded;
    long latency = 0;
    
    public DownloadTimer(int milliseconds, long managerID, DownloadManager manager) {
        timer = new Timer();
        DownloadTask task = new DownloadTask(timer, managerID, manager);
        timer.schedule(task, 0, 5);
        startTime = System.currentTimeMillis();
        task.run();
        
        
    }
    
    public class DownloadTask extends TimerTask {
        
        long managerID;
        DownloadManager manager;
        Timer timer;
        boolean latent = true;

        
        public DownloadTask(Timer timer, long managerID, DownloadManager manager) {
            this.timer = timer;
            this.managerID = managerID;
            this.manager = manager;
            
        }
        
        @Override
        public void run() {
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(managerID);
            Cursor cursor = manager.query(q);
            cursor.moveToFirst();
            bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            cursor.close();
            if (latent && bytes_downloaded > 0) {
                latent = false;
                latency = System.currentTimeMillis() - startTime;
                Log.d("Latency", String.valueOf(latency));
            }
            if (bytes_downloaded >= 650924) {
                this.timer.cancel();
            }
            Log.d("DownloadTimer", String.valueOf(bytes_downloaded));
        }
    }
}