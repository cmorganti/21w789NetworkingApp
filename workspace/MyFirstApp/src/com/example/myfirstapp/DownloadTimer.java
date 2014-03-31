package com.example.myfirstapp;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.DownloadManager;
import android.database.Cursor;
import android.util.Log;




public class DownloadTimer {
    long managerID;
//    DownloadManager manager;
    long startTime;
    Timer timer;
//    int bytes_downloaded;
    long latency = 0;
    public static ArrayList<Long> bytesArray;
    
//    public DownloadTimer(int milliseconds, long managerID, DownloadManager manager) {
//        timer = new Timer();
//        DownloadTask task = new DownloadTask(timer, managerID, manager);
//        timer.schedule(task, 0, 5);
//        startTime = System.currentTimeMillis();
//        task.run();
//    }
    
    public DownloadTimer(int milliseconds) {
        bytesArray = new ArrayList<Long>();
        timer = new Timer();
        DownloadTask task = new DownloadTask(timer);
        timer.schedule(task, 0, 50);
        startTime = System.currentTimeMillis();
        task.run();
    }
    
    public class DownloadTask extends TimerTask {
        
        long managerID;
    //    DownloadManager manager;
        Timer timer;
        boolean latent = true;

        
//        public DownloadTask(Timer timer, long managerID, DownloadManager manager) {
//            this.timer = timer;
//            this.managerID = managerID;
//            this.manager = manager;
//            
//        }
        
        public DownloadTask(Timer timer) {
            this.timer = timer;
        }
        
        @Override
        public void run() {
            long bytes = DownloadService.total;
           // Log.d("bytes", String.valueOf(bytes));
            bytesArray.add(bytes);
            if (DownloadService.total >= 650924) {
                Log.d("dt",  "end");
                ArrayList<Long> thruputs = this.calculateThroughput();
                Log.d("thruputs", thruputs.toString());
                int latency = this.calculateLatency();
                Log.d("latency", String.valueOf(latency) + " milliseconds");
                Log.d("avg_thruput", String.valueOf(this.calculateAvgThroughput(thruputs, latency)) + " bytes/sec");
                
                timer.cancel();
            }

            
        }
        
        public int calculateLatency() {
            int size = bytesArray.size();
            int latency = 0;
            for (int i = 0; i < size; i++) {
                if (bytesArray.get(i) > 0) {
                    latency = 5*i;
                    break;
                }
            }
            return latency;
        }
        
        public ArrayList<Long> calculateThroughput() {
            ArrayList<Long> throughputs = new ArrayList<Long>();
            int size = bytesArray.size();
            for (int i = 1; i < size; i++) {
                throughputs.add(200*(bytesArray.get(i) - bytesArray.get(i - 1)));
            }
            
            return throughputs;
        }
        
        public long calculateAvgThroughput(ArrayList<Long> throughputs, int latency) {
            int startIndex = latency / 5 - 1;
            int size = throughputs.size();
            long sum = 0;
            int numElements = 0;
            for (int i = startIndex; i < size; i++) {
                sum += throughputs.get(i);
                numElements++;
            }
            long avg = sum/numElements;
            return avg;
        }
    }
}