package com.example.elliotalderson.network_scanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button mButton = (Button)findViewById(R.id.scan_btn);
        final EditText mEdit   = (EditText)findViewById(R.id.ip_input);
        final TextView output = (TextView)findViewById(R.id.output_view);
        mButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        String ip = mEdit.getText().toString();
                        //List<Integer> openPorts = scan(ip,79,82);
                        System.out.println( mEdit.getText().toString());
                        String out = "";


                        InetAddress IP = null;
                        try {
                            IP = InetAddress.getByName(ip);
                            try {
                                if (IP.isReachable(1)) {
                                    output.setText("Host is up");
                                } else {
                                    output.setText("Host is down");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }


                    }
                });
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

}

                        /*

                        InetAddress addr = null;
                        try {
                            addr = InetAddress.getByName(ip);
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                        if(!openPorts.isEmpty()) {
                            for (int i = 0; i < openPorts.size(); i++) {
                                out.concat("\n Port " + i + " is open");
                            }
                        } else {
                            System.out.println("Why is this empty???");
                        }
                        */