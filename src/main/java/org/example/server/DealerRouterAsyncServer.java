package org.example.server;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import java.util.ArrayList;
import java.util.List;

public class DealerRouterAsyncServer {

    public static class ServerTask implements Runnable {
        private final int numServer;

        public ServerTask(int numServer) {
            this.numServer = numServer;
        }

        @Override
        public void run() {
            try (ZContext context = new ZContext()) {
                ZMQ.Socket frontend = context.createSocket(SocketType.ROUTER);
                frontend.bind("tcp://*:5570");

                ZMQ.Socket backend = context.createSocket(SocketType.DEALER);
                backend.bind("ipc://backend");

                List<ServerWorker> workers = new ArrayList<>();
                for (int i = 0; i < numServer; i++) {
                    ServerWorker worker = new ServerWorker(context, i);
                    worker.start();
                    workers.add(worker);
                }

                ZMQ.proxy(frontend, backend, null);

                frontend.close();
                backend.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class ServerWorker extends Thread {
        private final ZContext context;
        private final int id;

        public ServerWorker(ZContext context, int id) {
            this.context = context;
            this.id = id;
        }

        @Override
        public void run() {
            try (ZMQ.Socket worker = context.createSocket(SocketType.DEALER)) {
                worker.connect("ipc://backend");
                System.out.println("Worker#" + id + " started");

                while (!Thread.currentThread().isInterrupted()) {
                    byte[] ident = worker.recv(0);
                    byte[] msg = worker.recv(0);
                    System.out.println("Worker#" + id + " received " + new String(msg) + " from " + new String(ident));
                    worker.send(ident, ZMQ.SNDMORE);
                    worker.send(msg, 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void start(String[] args) throws InterruptedException {
        int numServers = Integer.parseInt(args[0]);
        Thread server = new Thread(new ServerTask(numServers));
        server.start();
        server.join();
    }
}
