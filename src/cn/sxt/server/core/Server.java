package cn.sxt.server.core;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket client;
    private boolean isRunning;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        isRunning = true;
        try {
            serverSocket = new ServerSocket( 8888 );
            receive();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println( "服务器启动失败..." );
        }
    }

    public void receive() {
        try {
            while (isRunning) {
                client = serverSocket.accept();
                System.out.println( "一个客户端建立了连接..." );
                new Thread( new Dispatcher( client ) ).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println( "客户端连接失败..." );
        }
    }

    public void stop() {
        isRunning = false;
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
