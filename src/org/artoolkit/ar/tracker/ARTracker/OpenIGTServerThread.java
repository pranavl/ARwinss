/**
 * OpenIGTServerThread.java
 * 
 * @author Pranav Lakshminarayanan
 * 
 */
package org.artoolkit.ar.tracker.ARTracker;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import org.medcare.igtl.messages.StringMessage;

/**
 * Server thread using OpenIGTLink Protocol.
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
                DataInputStream input = new DataInputStream(is);
                byte[] barr = readToByteArray(input);
                String a = new String(barr);
                StringMessage m = new StringMessage(a,
                        Arrays.toString(this.activity.getRenderer().get(a)));
                byte[] mess = m.PackBody();
                
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
    
    /**
     * Read input stream until EOF character is reached.
     * 
     * @param is DataInputStream being read
     * @return byte[] of stream
     * @throws IOException if error in reading stream
     */
    private byte[] readToByteArray(DataInputStream is) 
            throws IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[1];

        while ((nRead = is.read(data)) != -1) {
            if (data[0] == (byte) 0x00) {
                break;
            }
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        return buffer.toByteArray();
    }

}
