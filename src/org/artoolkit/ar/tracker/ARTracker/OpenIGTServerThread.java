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
     * Constructor for Server Thread.
     *
     * @param sock socket being used
     * @param a ARTracker that created this thread
     */
    public OpenIGTServerThread(ServerSocket sock, ARTracker a) {
        this.servSock = sock;
        this.activity = a;
    }

    @Override
    public void run() {

        try {
            while (true) {
                Socket clntSock = servSock.accept();

                InputStream is = clntSock.getInputStream();
                is.read();
                
                String a = "patt.a";
                StringMessage m = new StringMessage(a,
                        Arrays.toString(this.activity.getRenderer().get(a)));
                byte[] mess = m.PackBody();
                System.out.println(mess.length);

                DataOutputStream outToClient = new DataOutputStream(
                        clntSock.getOutputStream());
                outToClient.write(mess);
                outToClient.flush();
                outToClient.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
