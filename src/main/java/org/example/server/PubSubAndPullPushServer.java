package org.example.server;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.Random;

public class PubSubAndPullPushServer {
    public static void start(){
        try (ZContext context = new ZContext()) {
            ZMQ.Socket publisher = context.createSocket(SocketType.PUB);
            publisher.bind("tcp://*:5558");
            ZMQ.Socket collector = context.createSocket(SocketType.PULL);
            collector.bind("tcp://*:5559");

            while (true) {
                String message = new String(collector.recv(), ZMQ.CHARSET);
                System.out.println("I: publishing update " + message);
                publisher.send(message);
            }
        }
    }
}
