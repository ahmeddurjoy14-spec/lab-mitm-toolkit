package com.lab.mitm;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;

public class ProxySetupActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);
        layout.setBackgroundColor(0xFF0D1117);
        
        TextView title = new TextView(this);
        title.setText("Proxy Setup Guide");
        title.setTextSize(22);
        title.setTextColor(0xFF58A6FF);
        title.setPadding(0, 0, 0, 30);
        layout.addView(title);
        
        String[] guides = {"ProxyDroid Setup", "Postern Setup", "ADB + Mitmproxy", "HTTP Canary Setup"};
        int[] colors = {0xFF238636, 0xFF6E40C9, 0xFF316DCA, 0xFF8957E5};
        
        for (int i = 0; i < guides.length; i++) {
            final int idx = i;
            Button btn = new Button(this);
            btn.setText(guides[i]);
            btn.setTextColor(0xFFFFFFFF);
            btn.setTextSize(14);
            btn.setPadding(20, 30, 20, 30);
            btn.setBackgroundColor(colors[i]);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 20;
            btn.setLayoutParams(params);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { showGuide(idx); }
            });
            layout.addView(btn);
        }
        
        Button back = new Button(this);
        back.setText("BACK");
        back.setBackgroundColor(0xFF484F58);
        back.setTextColor(0xFFFFFFFF);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { finish(); }
        });
        layout.addView(back);
        
        setContentView(layout);
    }
    
    private void showGuide(int idx) {
        String title, content;
        
        if (idx == 0) {
            title = "ProxyDroid Setup";
            content = "1. Install ProxyDroid\n2. Enable Global Proxy\n3. Host: 127.0.0.1\n4. Port: 8080\n5. Tap Connect!";
        } else if (idx == 1) {
            title = "Postern Setup";
            content = "1. Install Postern\n2. Configure > Add Proxy\n3. Type: HTTP\n4. Server: 127.0.0.1\n5. Port: 8080\n6. Enable VPN Mode";
        } else if (idx == 2) {
            title = "ADB + Mitmproxy";
            content = "PC Required!\n\nOn PC:\n1. adb reverse tcp:8080 tcp:8080\n2. mitmproxy --listen-port 8080\n\nOn Phone:\n1. WiFi > Proxy: 127.0.0.1:8080";
        } else {
            title = "HTTP Canary";
            content = "1. Install HTTP Canary\n2. Grant VPN permission\n3. Select apps to monitor\n4. Enable SSL interception\n5. Start capture!";
        }
        
        new AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(content)
            .setPositiveButton("OK", null)
            .show();
    }
}
