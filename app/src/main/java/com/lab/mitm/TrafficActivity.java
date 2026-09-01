package com.lab.mitm;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import java.util.*;
import java.io.*;

public class TrafficActivity extends Activity {
    
    private TextView tvStats;
    private boolean running = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);
        layout.setBackgroundColor(0xFF0D1117);
        
        TextView title = new TextView(this);
        title.setText("Traffic Monitor");
        title.setTextSize(24);
        title.setTextColor(0xFF58A6FF);
        title.setPadding(0, 0, 0, 30);
        layout.addView(title);
        
        tvStats = new TextView(this);
        tvStats.setText("Starting...");
        tvStats.setTextColor(0xFFE6EDF3);
        tvStats.setTextSize(16);
        layout.addView(tvStats);
        
        Button btn = new Button(this);
        btn.setText("START MONITORING");
        btn.setBackgroundColor(0xFF238636);
        btn.setTextColor(0xFFFFFFFF);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { running = !running; }
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 150);
        params.topMargin = 50;
        btn.setLayoutParams(params);
        layout.addView(btn);
        
        Button back = new Button(this);
        back.setText("BACK");
        back.setBackgroundColor(0xFF484F58);
        back.setTextColor(0xFFFFFFFF);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { running = false; finish(); }
        });
        params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 120);
        params.topMargin = 20;
        back.setLayoutParams(params);
        layout.addView(back);
        
        setContentView(layout);
        startMonitoring();
    }
    
    private void startMonitoring() {
        running = true;
        new Thread(new Runnable() {
            public void run() {
                long startRx = 0, startTx = 0;
                
                while (running) {
                    try {
                        long rx = getNetworkRx();
                        long tx = getNetworkTx();
                        
                        if (startRx == 0) { startRx = rx; startTx = tx; }
                        
                        final String stats = String.format(
                            "Network Stats\n\nDownloaded: %.2f MB\nUploaded: %.2f MB",
                            (rx - startRx) / 1024.0 / 1024.0,
                            (tx - startTx) / 1024.0 / 1024.0);
                        
                        runOnUiThread(new Runnable() {
                            public void run() { tvStats.setText(stats); }
                        });
                        
                        Thread.sleep(1000);
                    } catch (Exception e) {}
                }
            }
        }).start();
    }
    
    private long getNetworkRx() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("/sys/class/net/wlan0/statistics/rx_bytes"));
            return Long.parseLong(br.readLine().trim());
        } catch (Exception e) { return 0; }
    }
    
    private long getNetworkTx() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("/sys/class/net/wlan0/statistics/tx_bytes"));
            return Long.parseLong(br.readLine().trim());
        } catch (Exception e) { return 0; }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        running = false;
    }
}
