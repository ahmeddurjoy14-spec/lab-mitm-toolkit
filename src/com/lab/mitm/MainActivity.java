package com.lab.mitm;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.net.wifi.*;
import android.net.*;
import android.content.*;
import android.text.method.*;
import android.graphics.*;
import android.text.*;
import java.util.*;

public class MainActivity extends Activity {
    
    private TextView tvStatus, tvWifiName, tvIP, tvGateway, tvLog;
    private StringBuilder logBuffer = new StringBuilder();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        getNetworkInfo();
    }
    
    private void initViews() {
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvWifiName = (TextView) findViewById(R.id.tvWifiName);
        tvIP = (TextView) findViewById(R.id.tvIP);
        tvGateway = (TextView) findViewById(R.id.tvGateway);
        tvLog = (TextView) findViewById(R.id.tvLog);
        
        tvLog.setMovementMethod(new ScrollingMovementMethod());
        
        ((Button) findViewById(R.id.btnScan)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { startActivity(new Intent(MainActivity.this, ScanActivity.class)); }
        });
        ((Button) findViewById(R.id.btnTraffic)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { startActivity(new Intent(MainActivity.this, TrafficActivity.class)); }
        });
        ((Button) findViewById(R.id.btnProxy)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { startActivity(new Intent(MainActivity.this, ProxySetupActivity.class)); }
        });
        ((Button) findViewById(R.id.btnVPN)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { showVPNGuide(); }
        });
        ((Button) findViewById(R.id.btnADB)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { showADBGuide(); }
        });
        ((Button) findViewById(R.id.btnSettings)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { startActivity(new Intent(MainActivity.this, SettingsActivity.class)); }
        });
        
        appendLog("[*] LAB MITM started\n");
        appendLog("[*] Non-root mode active\n");
    }
    
    private void getNetworkInfo() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            
            if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo winfo = wm.getConnectionInfo();
                
                int ip = winfo.getIpAddress();
                String ipStr = String.format(Locale.US, "%d.%d.%d.%d",
                    ip & 0xff, (ip >> 8) & 0xff, (ip >> 16) & 0xff, (ip >> 24) & 0xff);
                
                String ssid = winfo.getSSID();
                if (ssid != null) ssid = ssid.replace("\"", "");
                
                tvWifiName.setText("SSID: " + (ssid != null ? ssid : "Unknown"));
                tvIP.setText("IP: " + ipStr);
                
                DhcpInfo dhcp = wm.getDhcpInfo();
                String gateway = intToIp(dhcp.gateway);
                tvGateway.setText("Gateway: " + gateway);
                
                appendLog("[+] WiFi connected: " + ssid + "\n");
                appendLog("[+] IP: " + ipStr + "\n");
            } else {
                appendLog("[!] No WiFi connection\n");
            }
        } catch (Exception e) {
            appendLog("[!] Error: " + e.getMessage() + "\n");
        }
    }
    
    private String intToIp(int addr) {
        return String.format(Locale.US, "%d.%d.%d.%d",
            addr & 0xff, (addr >> 8) & 0xff, (addr >> 16) & 0xff, (addr >> 24) & 0xff);
    }
    
    private void showVPNGuide() {
        new AlertDialog.Builder(this)
            .setTitle("VPN-based MITM Guide")
            .setMessage("Method 1: ProxyDroid\n1. Install ProxyDroid\n2. Set Host: 127.0.0.1\n3. Set Port: 8080\n4. Enable\n\nMethod 2: Postern\n1. Install Postern\n2. Configure proxy\n3. Enable VPN Mode")
            .setPositiveButton("OK", null)
            .show();
    }
    
    private void showADBGuide() {
        new AlertDialog.Builder(this)
            .setTitle("ADB Proxy Guide")
            .setMessage("PC Required!\n\nOn PC:\n1. adb reverse tcp:8080 tcp:8080\n2. mitmproxy --listen-port 8080\n\nOn Phone:\n1. WiFi > Proxy: 127.0.0.1:8080\n2. Visit mitm.it\n3. Install cert")
            .setPositiveButton("OK", null)
            .show();
    }
    
    private void appendLog(String msg) {
        logBuffer.append(msg);
        if (logBuffer.length() > 5000) logBuffer.delete(0, logBuffer.length() - 4000);
        tvLog.setText(logBuffer.toString());
    }
}
