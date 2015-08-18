/*
 *  ARSimple.java
 *
 *  @author Pranav Lakshminarayanan
 *
 */
package org.artoolkit.ar.samples.ARSimple;

import android.net.wifi.WifiManager;
import org.artoolkit.ar.base.ARActivity;
import org.artoolkit.ar.base.rendering.ARRenderer;
import org.artoolkit.ar.samples.ARSimple.R;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Runs WINSS application.
 */
public class ARSimple extends ARActivity {

    // UI ELEMENTS =============================================================
    /**
     * TextView to display status messages.
     */
    private TextView txtStat;

    /**
     * Send button.
     */
    private Button sendButton;

    // OBJECTS AND VARIABLES ===================================================
    /**
     * Thread for client.
     */
    Thread clientThread = null;

    /**
     * Thread for server.
     */
    Thread serverThread = null;

    private ServerSocket servSock;

    // MODIFY IP AND PORT HERE
    /**
     * IP Address of server.
     */
    final String SERVER_IP = "192.168.1.5";

    /**
     * Server port.
     */
    final int SERVER_PORT = 8099;

    // METHODS =================================================================
    /**
     * On create...
     *
     * @param savedInstanceState .
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Set up server socket
        try {
            this.servSock = new ServerSocket(this.SERVER_PORT);
        } catch (IOException ex) {
            sendButton.setText("Socket Error");
        }

        // Display IP Address of device to which clients can connect
        this.txtStat = (TextView) findViewById(R.id.txt_IPAddress);
        this.txtStat.setText("Device IP Address: "
                + getIPAddress());

        // Event listener to the Send button
        this.sendButton = (Button) findViewById(R.id.btn_send);
        this.sendButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start server
                        serverThread = new Thread(new TCPServerThread());
                        serverThread.start();
                    }
                }
        );

    }

    /**
     * Provide our own SimpleRenderer.
     *
     * @return ARRenderer
     */
    @Override
    protected ARRenderer supplyRenderer() {
        return new SimpleRenderer();
    }

    /**
     * Use the FrameLayout in this Activity's UI.
     *
     * @return FrameLayout
     */
    @Override
    protected FrameLayout supplyFrameLayout() {
        return (FrameLayout) this.findViewById(R.id.viewFrame);

    }

    /**
     * Provide IP Address of the device.
     * 
     * @return IP address as a String
     */
    private String getIPAddress() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

// THREADS =====================================================================
    /**
     * TCP Client class, executed in a separate thread.
     */
    class TCPClientThread implements Runnable {

        @Override
        public void run() {
            Socket clientSocket;
            DataOutputStream outToServer;
            BufferedReader inFromServer;
            String fromServ;
            // CONNECT TO SERVER
            try {
                clientSocket = new Socket(
                        InetAddress.getByName(SERVER_IP), SERVER_PORT);

                outToServer = new DataOutputStream(
                        clientSocket.getOutputStream());

                inFromServer = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                // WRITE TO SERVER AND RECEIVE MESSAGE
                outToServer.writeBytes("Test");
                fromServ = inFromServer.readLine();
                clientSocket.close();

            } catch (Exception e) {
                System.out.println("\nCONNECTION ERROR");
            }
        }
    }

    /**
     * Server thread.
     */
    class TCPServerThread implements Runnable {

        @Override
        public void run() {
            try {
                Socket clntSock = servSock.accept();
                BufferedReader inFromClient = new BufferedReader(
                        new InputStreamReader(clntSock.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(
                        clntSock.getOutputStream());
                inFromClient.readLine();
                outToClient.writeBytes("RECEIVED");
                outToClient.flush();
                outToClient.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
