package nl.rug.aoop.networking.Server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.messageHandler.Communicator;
import nl.rug.aoop.networking.messageHandler.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Class to handle communication with a client.
 */
@Slf4j
@Getter
public class ClientHandler implements Runnable, Communicator {
    private final BufferedReader in;
    private final PrintWriter out;
    private final Socket socket;
    private final int id;
    private boolean running = false;
    private final MessageHandler messageHandler;

    /**
     * Constructor of the client handler.
     *
     * @param socket connected to the client.
     * @param id identifier of the client handler.
     * @param messageHandler to handle the messages from the client.
     * @throws IOException if there is an error while setting up bufferedReader and PrintWriter.
     */
    public ClientHandler(Socket socket, int id, MessageHandler messageHandler)  throws IOException {
        this.socket = socket;
        this.id = id;
        this.messageHandler = messageHandler;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * while there is a connection with the client, reads the messages from the client
     * and processes them using the message handler.
     */
    @Override
    public void run() {
        running = true;
        log.info("your id: " + id);
        while (running && socket.isBound() && !socket.isClosed()) {
            try {
                String fromClient = in.readLine();
                if (fromClient == null) {
                    terminate();
                    break;
                }
                messageHandler.handleMessage(fromClient, this);
            } catch (IOException e) {
                log.error("socket is closed cannot read from: " + id );
            }
        }
    }

    /**
     * Sends messages to the client.
     *
     * @param message The message to be sent.
     */
    @Override
    public void send(String message) {
        if (message != null) {
            out.println( message);
            out.flush();
        } else {
            throw new IllegalArgumentException("message cannot be null");
        }
    }

    /**
     * terminates the client handler.
     */

    public void terminate() {
        running = false;
        try {
            this.socket.close();
            log.info("client: " + id + " has terminated");
        } catch (IOException e) {
            log.error("could not close the socket", e);
        }
    }

}
