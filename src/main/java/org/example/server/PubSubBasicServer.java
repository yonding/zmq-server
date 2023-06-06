package org.example.server;

import java.util.Random;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;
public class PubSubBasicServer
{
    public static void start()
    {
        try (ZContext context = new ZContext()) {
            System.out.println("Publishing updates at weather server...");

            ZMQ.Socket publisher = context.createSocket(SocketType.PUB);
            publisher.bind("tcp://*:5556");

            //  Initialize random number generator
            Random srandom = new Random(System.currentTimeMillis());
            while (true) {
                int zipcode = 10000 + srandom.nextInt(10000);
                int temperature = srandom.nextInt(215) - 80 + 1;
                int relhumidity = srandom.nextInt(50) + 10 + 1;

                //  Send message to all subscribers
                String update = String.format(
                        "%05d %d %d", zipcode, temperature, relhumidity
                );

                publisher.send(update, 0);
            }
        }
    }
}