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

        String a = "patt.a";
        StringMessage m = new StringMessage(a, 
                Arrays.toString(this.activity.getRenderer().get(a)));
        byte[] mess = m.PackBody();
        System.out.println(mess.length);

        try {
            Socket clntSock = servSock.accept();
            System.out.println("Recieved connection from: "
                    + clntSock.getInetAddress()
                    + ":"
                    + clntSock.getLocalPort());
            InputStream is = clntSock.getInputStream();

            is.read();
            DataOutputStream outToClient = new DataOutputStream(
                    clntSock.getOutputStream());
            outToClient.write(mess);
            outToClient.flush();
            outToClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

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
