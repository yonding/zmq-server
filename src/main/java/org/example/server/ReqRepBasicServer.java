package org.example.server;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import static java.lang.Thread.sleep;


public class ReqRepBasicServer {
    public static void start() throws Exception {
        try (ZContext context = new ZContext()) {
            // 클라이언트와 데이터를 주고받을 수 있도록 소켓을 생성한다.
            // 클라이언트로부터 데이터를 받으면 보내는 소켓 타입은 REP이다.
            ZMQ.Socket socket = context.createSocket(SocketType.REP);
            socket.bind("tcp://*:5551");

            while (true) {
                // 클라이언트로부터 데이터를 받을 때까지 기다린다. (default flag: 0)
                byte[] reply = socket.recv();
                // 클라이언트로부터 받은 메세지를 출력한다.
                System.out.println("Received request: " + new String(reply, ZMQ.CHARSET));

                // 잠시 동작 중지
                sleep(1000);

                // 클라이언트에게 메세지 전송한다. (default flag: 0)
                String response = "World";
                socket.send(response.getBytes(ZMQ.CHARSET));

            }
        }
    }
}