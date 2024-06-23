package nl.rug.aoop.networking.client;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.messageHandler.Communicator;
import nl.rug.aoop.networking.messageHandler.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Class to handle communication with the server.
 */
@Slf4j
@Getter
@Setter
public class Client implements Communicator, Runnable {
    private final InetSocketAddress address;
    /**
     * The timeout value in milliseconds for socket connections.
     */
    public static final int TIMEOUT = 1000;
    private Socket socket;
    private boolean running = false;
    private boolean connected = false;
    private BufferedReader in;
    private PrintWriter out;
    private final MessageHandler messageHandler;

    /**
     * Constructor of the client.
     *
     * @param address        to connect.
     * @param messageHandler for processing incoming messages.
     * @throws IOException if there is an error initializing the socket.
     */
    public Client(InetSocketAddress address, MessageHandler messageHandler) throws IOException {
        this.address = address;
        this.messageHandler = messageHandler;
        initSocket();
    }

    /**
     * Connect the client with the server.
     *
     * @throws IOException if you could not connect to the server.
     */
    private void initSocket() throws IOException {
        this.socket = new Socket();
        this.socket.connect(address, TIMEOUT);
        if (!this.socket.isConnected()) {
            throw new IOException("cannot connect to the server socket");
        }
        log.info("connected to server");
        connected = true;
        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        out = new PrintWriter(this.socket.getOutputStream(), true);
    }

    /**
     * while there is a connection with the client handler, reads the messages from the client handler
     * and processes them using the message handler.
     */
    @Override
    public void run() {
        running = true;
        while (running && !socket.isClosed() && socket.isBound()) {
            try {
                String fromServer = in.readLine();
                if (fromServer == null) {
                    terminate();
                    break;
                }
                messageHandler.handleMessage(fromServer, this);
            } catch (IOException e) {
                log.error("could not read from server ");
            }
        }
    }

    /**
     * sends message to the client handler.
     *
     * @param message The message to be sent.
     */
    public void send(String message) {
        if (message != null) {
            out.println(message);
            out.flush();
        } else {
            throw new IllegalArgumentException("message cannot be null");
        }
    }

    /**
     * terminates the client.
     */
    public void terminate() {
        running = false;
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            log.error("could not close the socket");
        }
    }
}
