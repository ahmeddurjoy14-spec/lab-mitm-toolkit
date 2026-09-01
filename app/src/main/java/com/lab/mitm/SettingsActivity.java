package com.lab.mitm;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.content.pm.*;
import android.net.wifi.*;

public class SettingsActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);
        layout.setBackgroundColor(0xFF0D1117);
        
        TextView title = new TextView(this);
        title.setText("Settings");
        title.setTextSize(22);
        title.setTextColor(0xFF58A6FF);
        title.setPadding(0, 0, 0, 30);
        layout.addView(title);
        
        TextView tvRoot = new TextView(this);
        boolean isRoot = checkRoot();
        tvRoot.setText(isRoot ? "Root: Available" : "Root: Not Available");
        tvRoot.setTextColor(isRoot ? 0xFF3FB950 : 0xFFD29922);
        tvRoot.setTextSize(16);
        tvRoot.setPadding(0, 0, 0, 20);
        layout.addView(tvRoot);
        
        TextView tvVersion = new TextView(this);
        try {
            PackageInfo p = getPackageManager().getPackageInfo(getPackageName(), 0);
            tvVersion.setText("Version: " + p.versionName);
        } catch (Exception e) {
            tvVersion.setText("Version: 1.0");
        }
        tvVersion.setTextColor(0xFF8B949E);
        tvVersion.setTextSize(14);
        layout.addView(tvVersion);
        
        Button btnClear = new Button(this);
        btnClear.setText("Clear Logs");
        btnClear.setBackgroundColor(0xFFDA3633);
        btnClear.setTextColor(0xFFFFFFFF);
        btnClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { clearLogs(); }
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 30;
        btnClear.setLayoutParams(params);
        layout.addView(btnClear);
        
        Button btnAbout = new Button(this);
        btnAbout.setText("About");
        btnAbout.setBackgroundColor(0xFF21262D);
        btnAbout.setTextColor(0xFFFFFFFF);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { showAbout(); }
        });
        params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 10;
        btnAbout.setLayoutParams(params);
        layout.addView(btnAbout);
        
        Button back = new Button(this);
        back.setText("BACK");
        back.setBackgroundColor(0xFF484F58);
        back.setTextColor(0xFFFFFFFF);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { finish(); }
        });
        params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 30;
        back.setLayoutParams(params);
        layout.addView(back);
        
        setContentView(layout);
    }
    
    private boolean checkRoot() {
        try {
            java.lang.Process p = Runtime.getRuntime().exec("su -c id");
            java.io.BufferedReader r = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
            String result = r.readLine();
            return result != null && result.contains("uid=0");
        } catch (Exception e) {
            return false;
        }
    }
    
    private void clearLogs() {
        new AlertDialog.Builder(this)
            .setTitle("Clear Logs")
            .setMessage("Clear all logs?")
            .setPositiveButton("Yes", null)
            .setNegativeButton("No", null)
            .show();
    }
    
    private void showAbout() {
        new AlertDialog.Builder(this)
            .setTitle("About LAB MITM")
            .setMessage("LAB MITM Toolkit v1.0\n\nMITM Framework for Android\nNon-Root Edition\n\nFor educational purposes only!")
            .setPositiveButton("OK", null)
            .show();
    }
}
