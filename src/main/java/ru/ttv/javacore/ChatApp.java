package ru.ttv.javacore;

import java.util.Scanner;

/*
 *@author Timofey Teplykh
 */
public class ChatApp {
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.serverStart(new Scanner(System.in));
    }
}
