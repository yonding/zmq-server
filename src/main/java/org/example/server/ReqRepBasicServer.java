package org.example.server;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import static java.lang.Thread.sleep;


public class ReqRepBasicServer {
    public static void start() throws Exception {
        try (ZContext context = new ZContext()) {
            // Socket to talk to clients
            ZMQ.Socket socket = context.createSocket(SocketType.REP);
            socket.bind("tcp://*:5555");

            while (true) {
                // Wait for next request from client
                byte[] reply = socket.recv(0);
                System.out.println("Received request: " + new String(reply, ZMQ.CHARSET));

                //  Do some 'work'
                sleep(1000);

                // Send reply back to client
                String response = "World";
                socket.send(response.getBytes(ZMQ.CHARSET), 0);

            }
        }
    }
}