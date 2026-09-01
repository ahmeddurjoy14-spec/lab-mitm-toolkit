package com.lab.mitm;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import java.util.*;
import java.io.*;

public class ScanActivity extends Activity {
    
    private ListView lvDevices;
    private ArrayList<String> devices = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        
        lvDevices = (ListView) findViewById(R.id.lvDevices);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, devices);
        lvDevices.setAdapter(adapter);
        
        ((Button) findViewById(R.id.btnScan)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { startScan(); }
        });
        ((Button) findViewById(R.id.btnBack)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { finish(); }
        });
    }
    
    private void startScan() {
        devices.clear();
        devices.add("Scanning...");
        adapter.notifyDataSetChanged();
        
        new Thread(new Runnable() {
            public void run() {
                try {
                    String gateway = getGateway();
                    
                    final String finalGateway = gateway;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            devices.clear();
                            devices.add("Gateway: " + finalGateway);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    
                    String base = gateway.substring(0, gateway.lastIndexOf('.'));
                    
                    for (int i = 1; i < 255; i++) {
                        final String ip = base + "." + i;
                        
                        try {
                            java.lang.Process p = Runtime.getRuntime().exec("/system/bin/ping -c 1 -W 1 " + ip);
                            int ret = p.waitFor();
                            
                            if (ret == 0) {
                                final String mac = getMacFromArp(ip);
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        devices.add(ip + " | " + mac);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        } catch (Exception e) {}
                    }
                    
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            devices.clear();
                            devices.add("Error: " + e.getMessage());
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }
    
    private String getGateway() {
        try {
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(
                Runtime.getRuntime().exec("ip route | grep default").getInputStream()));
            String line = br.readLine();
            if (line != null) {
                String[] parts = line.split("\\s+");
                for (int i = 0; i < parts.length - 1; i++) {
                    if (parts[i].equals("via")) return parts[i + 1];
                }
            }
        } catch (Exception e) {}
        return "192.168.1.1";
    }
    
    private String getMacFromArp(String ip) {
        try {
            java.lang.Process p = Runtime.getRuntime().exec("/system/bin/ip neigh show " + ip);
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
            String line = br.readLine();
            if (line != null && line.contains("lladdr")) {
                int idx = line.indexOf("lladdr");
                return line.substring(idx + 6).trim().split(" ")[0];
            }
        } catch (Exception e) {}
        return "Unknown";
    }
}
