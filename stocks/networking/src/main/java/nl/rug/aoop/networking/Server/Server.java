package nl.rug.aoop.networking.Server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.messageHandler.MessageHandler;

import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Class for server implementation.
 */
@Slf4j
@Getter
public class Server implements Runnable {
    private final int port;
    private final ServerSocket serverSocket;
    private boolean running = false;
    private int id = 1;
    private final ExecutorService service;
    private final MessageHandler messageHandler;
    private final ThreadPoolExecutor myPool;
    private final List<ClientHandler> clientHandlers = new ArrayList<>();

    /**
     * Constructor of the server.
     *
     * @param port of the server.
     * @param messageHandler to handle messages from clients.
     * @throws IOException if there is an error setting up the serverSocket.
     */
    public Server(int port, MessageHandler messageHandler) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.port = port;
        this.messageHandler = messageHandler;
        service = Executors.newCachedThreadPool();
        myPool = (ThreadPoolExecutor) service;
    }

    /**
     * Assigns a new client handler to each client while there is connection.
     */
    @Override
    public void run() {
        running = true;
        log.info("Server started at port " + getPort());
        while (running && serverSocket.isBound() && !serverSocket.isClosed()) {
            try {
                Socket socket = this.serverSocket.accept();
                log.info("New connection from client");
                ClientHandler clientHandler = new ClientHandler(socket, id, messageHandler);
                clientHandlers.add(clientHandler);
                this.service.submit(clientHandler);
                id++;
            } catch (IOException e) {
                log.error("could not read the socket is closed");
            }
        }
    }

    /**
     * Terminates the server by first trying to terminate all the client handlers.
     *
     * @throws InterruptedException if an interruption occurs during the termination process.
     */
    public void terminate() throws InterruptedException {
        log.info("the server is terminating");
        running = false;
        this.service.shutdown();  //shutdowns all the clientHandlers inside the thread pool.
        boolean terminated = this.service.awaitTermination(5, TimeUnit.SECONDS);
        if (!terminated) {
            this.service.shutdownNow();
        }
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            log.error("Error while closing server socket: ", e);
        }
    }

}
