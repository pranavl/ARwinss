/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.artoolkit.ar.tracker.ARTracker;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import org.medcare.igtl.messages.StringMessage;

/**
 *
 * @author Pranav
 */
public class OpenIGTServerThread implements Runnable {

    /**
     * Server socket.
     */
    private ServerSocket servSock;

    /**
     * ARTracker activity that created this thread.
     */
    private ARTracker activity;

    /**
     * Constructor for
     *
     * @param sock
     */
    public OpenIGTServerThread(ServerSocket sock, ARTracker a) {
        this.servSock = sock;
        this.activity = a;
    }

    @Override
    public void run() {
        try {
            Socket clntSock = servSock.accept();
            InputStream is = clntSock.getInputStream();
            is.read();
//            BufferedReader inFromClient
//                    = new BufferedReader(new InputStreamReader(is));
//            String a = inFromClient.readLine();
            byte[] mess = parseForMessage("patt.a");
            DataOutputStream outToClient = new DataOutputStream(
                    clntSock.getOutputStream());
            outToClient.write(mess);
            outToClient.flush();
            outToClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Parse input for appropriate output.
     *
     * @param a
     * @return byte array for message to be sent
     */
    private byte[] parseForMessage(String a) {
        String message = Arrays.toString(this.activity.getRenderer().get(a));
        if (message == null) {
            message = a + " not valid";
        } else if (message.equals("[0.0]")) {
            message = a + " not found";
        }
        StringMessage m = new StringMessage(a, message);
        return m.PackBody();

    }

}
