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

            // Publish 타입의 소켓을 생성한다.
            ZMQ.Socket publisher = context.createSocket(SocketType.PUB);
            // 해당 소켓을 포트번호 5556에 bind한다.
            publisher.bind("tcp://*:5556");

            // 시드를 현재 시간으로 설정하여 Random 객체를 생성한다.
            Random srandom = new Random(System.currentTimeMillis());
            while (true) {
                // zipcode, temperature, relhumidity를 랜덤수로 초기화한다.
                int zipcode = 10000 + srandom.nextInt(10000);
                int temperature = srandom.nextInt(215) - 80 + 1;
                int relhumidity = srandom.nextInt(50) + 10 + 1;

                // 모든 Subscribers에게 데이터를 업데이트한다. (default flag: 0)
                String update = String.format(
                        "%05d %d %d", zipcode, temperature, relhumidity
                );
                publisher.send(update);
            }
        }
    }
}