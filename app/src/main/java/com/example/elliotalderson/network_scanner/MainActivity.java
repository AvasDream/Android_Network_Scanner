package com.example.elliotalderson.network_scanner;

import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ownip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        Button mButton = (Button)findViewById(R.id.scan_btn);
        final EditText mEdit   = (EditText)findViewById(R.id.ip_input);
        final TextView output = (TextView)findViewById(R.id.output_view);
        final TextView ownIp = (TextView)findViewById(R.id.ownIp);
        ownIp.setText("Device Address:\n" + ownip);
        mButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        String ip = mEdit.getText().toString();
                        //List<Integer> openPorts = scan(ip,79,82);
                        System.out.println( mEdit.getText().toString());
                        String out = "";
                        pingTask p = new pingTask();
                        Object[] objectArray = {ip};
                        String ping = p.doInBackground(objectArray);
                        System.out.printf("\n***\n***\n%s\n***\n***\n", ping);
                        if (ping.contains("100% packet loss")) {
                            output.setText(ip + " is offline!");
                            output.setBackgroundColor(Color.RED);
                        } else {
                            output.setText(ip + " is online!");
                            output.setBackgroundColor(Color.GREEN);
                        }
                    }
                });
    }
    public String ping(String ip) {
        try {
        Process process = Runtime.getRuntime().exec(
                "/system/bin/ping -c 3 " + ip);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
        int i;
        char[] buffer = new char[4096];
        StringBuffer output = new StringBuffer();
        while ((i = reader.read(buffer)) > 0){
            output.append(buffer, 0, i);
        }
        reader.close();
        return output.toString();
    } catch (IOException e) {
        e.printStackTrace();
    }
        return "Error in Method ping()";
    }

    public List<Integer> scan(String ip, int port_beginn, int port_end) {
        List<Integer> openPorts = new ArrayList<>();
        openPorts.add(1);
        for (int i = port_beginn; i <= port_end; i++) {
            if (isOpen(ip,i)) {
                openPorts.add(i);
                System.out.printf("Port %s is open", i);
            }
        }
        return openPorts;
    }

    public Boolean isOpen(String ip, int port) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port));
            socket.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    private static class pingTask extends AsyncTask {
        @Override
        protected String doInBackground(Object[] objects) {
            try {
                String ip = objects[0].toString();
                Process process = Runtime.getRuntime().exec(
                        "/system/bin/ping -c 3 " + ip);
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        process.getInputStream()));
                int i;
                char[] buffer = new char[4096];
                StringBuffer output = new StringBuffer();
                while ((i = reader.read(buffer)) > 0){
                    output.append(buffer, 0, i);
                }
                reader.close();
                return output.toString();
            } catch (IOException e) {
                return "Error in Method ping()";
            }

        }
    }
}

