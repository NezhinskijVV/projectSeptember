import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Log
public class Main {
    public static final String IP = "localhost";
    public static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            Socket socket = null;
            PrintWriter printWriter = null;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));


            while (true) {
                if (socket == null) {
                    log.info("Start");
                    socket = new Socket(IP, PORT);
                    printWriter = new PrintWriter(socket.getOutputStream());

                    System.out.println("Введи логин: ");
                    String login = bufferedReader.readLine();
                    System.out.println("Введи пароль: ");
                    String password = bufferedReader.readLine();

                    printWriter.println("Registration login:" + login + "password:" + password);
                    printWriter.flush();

                    Socket finalSocket = socket;
                    new Thread(() ->
                    {
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(finalSocket.getInputStream()));
                            String inputSocket;
                            while ((inputSocket = reader.readLine()) != null) {
                                System.out.println(inputSocket);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();

                } else {
                    String message = bufferedReader.readLine();
                    printWriter.println(message);
                    printWriter.flush();
                }
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
