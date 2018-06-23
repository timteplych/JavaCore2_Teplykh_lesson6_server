package ru.ttv.javacore;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ChatServer {
    private ServerSocket serv = null;
    private Socket sock = null;
    private final int port = 8189;
    private Thread sendMessageThread;
    private Scanner scanner;
    private PrintWriter printWriter;

    public ChatServer(){

    }

    public void serverStart(final Scanner localScanner){
        try{
            serv = new ServerSocket(port);
            System.out.println("Server started, waiting for connection...");
            sock = serv.accept();
            System.out.println("Client connected");
            scanner = new Scanner(sock.getInputStream());
            printWriter = new PrintWriter(sock.getOutputStream());
            //Thread for message sending from local console
            sendMessageThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        while(true){
                            if(Thread.interrupted()){
                                System.out.println("thread break");
                                break;
                            }
                            if(localScanner.hasNext()){
                                String msg = localScanner.nextLine();
                                printWriter.append(msg+"\n");
                                printWriter.flush();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            sendMessageThread.setDaemon(true);
            sendMessageThread.start();
            //waiting data from client and sending it back
            try {
                while (true){
                    if(scanner.hasNext()) {
                        String str = scanner.nextLine();
                        if (str.equals("end")) {
                            break;
                        }
                        System.out.println(str);
                        printWriter.append("Echo: " + str+"\n");
                        printWriter.flush();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.out.println("Socket server creation error");
        } finally {
            try {
                if(sendMessageThread != null){
                    sendMessageThread.interrupt();
                }
                Thread.sleep(1000);
                serv.close();
                sock.close();
                scanner.close();
                localScanner.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
