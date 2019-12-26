import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Log
@Setter
@AllArgsConstructor
public class ClientEntity implements Observer, Runnable {
    private Client client;
    private Socket socket;
    private MyServer myServer;

    @Override
    public boolean isConnected() {
        return socket.isConnected();
    }

    @Override
    public void stopClient() {
//        return socket.close();
    }

    @Override
    public void notifyObserver(String message) {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println(message);
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        log.info("New Client");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String input = reader.readLine();
                if (input != null) {
                    //pattern, matcher
                    if (input.contains("Registration login:")) {
                        String login = input.substring(19).split("password:")[0];
                        String password = input.substring(19).split("password:")[1];
                        this.setClient(new Client(login, password));
                        log.info("Client login:" + login + "password: " + password + " register!");
                    } else {
                        log.info(input);
                        myServer.notifyObservers(input);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

