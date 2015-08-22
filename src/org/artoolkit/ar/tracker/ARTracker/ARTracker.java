/**
 * ARTracker.java
 *
 * @author Pranav Lakshminarayanan
 */
package org.artoolkit.ar.tracker.ARTracker;

import android.net.wifi.WifiManager;
import org.artoolkit.ar.base.ARActivity;
import org.artoolkit.ar.base.rendering.ARRenderer;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Runs WINSS application.
 */
public class ARTracker extends ARActivity {

    // UI ELEMENTS =============================================================
    /**
     * TextView to display status messages.
     */
    private TextView txtStat;

    /**
     * Start button.
     */
    private Button startButton;
    
    /**
     * Stop button.
     */
    private Button stopButton;

    // OBJECTS AND VARIABLES ===================================================
    /**
     * Thread for server.
     */
    Thread serverThread = null;

    /**
     * Socket for server.
     */
    private ServerSocket servSock;

    /**
     * Server port.
     */
    final int SERVER_PORT = 8099;
    
    /**
     * TrackerRenderer object.
     */
    private TrackerRenderer ren;

    // METHODS =================================================================
    /**
     * On create...
     *
     * @param savedInstanceState .
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        // Constructor from superclass and get layout elements
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Set up server socket and server thread
        try {
            this.servSock = new ServerSocket(this.SERVER_PORT);
        } catch (IOException ex) {
            startButton.setText("Socket Error");
        }
        
        if (serverThread == null) {
            serverThread = 
                    new Thread(new OpenIGTServerThread(this.servSock, this)); 
        }

        // Display IP Address of device to which clients can connect
        this.txtStat = (TextView) findViewById(R.id.txt_IPAddress);
        this.txtStat.setText("Server settings - "
                + getIPAddress() + ":" + this.SERVER_PORT);

        // Event listener to the Start button
        this.startButton = (Button) findViewById(R.id.btn_start);
        this.startButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start server
                        //startButton.setEnabled(false);
                        //stopButton.setEnabled(true);
                        serverThread.start();
                    }
                }
        );
        
        // Event listener to the Stop button
//        this.stopButton = (Button) findViewById(R.id.btn_stop);
//        this.stopButton.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Start server
//                        startButton.setEnabled(true);
//                        stopButton.setEnabled(false);
////                        try {
////                            servSock.close();
////                        } catch (IOException ex) {
////                            Logger.getLogger(ARTracker.class.getName()).log(Level.SEVERE, null, ex);
////                        }
//                    }
//                }
//        );

    }

    /**
     * Provide our own TrackerRenderer.
     *
     * @return ARRenderer
     */
    @Override
    protected ARRenderer supplyRenderer() {
        this.ren = new TrackerRenderer(this);
        return this.ren;
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
     * Accessor for TrackerRenderer object.
     * 
     * @return the TrackerRenderer associated with this activity
     */
    public TrackerRenderer getRenderer() {
        return this.ren;
    }
    
    /**
     * Provide IP Address of the device.
     *
     * @return IP address as a String
     */
    protected String getIPAddress() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }
    
}
