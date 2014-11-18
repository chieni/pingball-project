package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import protocol.Request;


/**
 * Multiplayer PingballServer
 *
 *Thread safety argument:
 *PingballServer uses a blockingQueue to maintain the
 *Requests from its clients. Each worker that puts and
 *removes items from the queue runs its own Thread 
 *This guarantees safe execution of
 *methods on the queue by
 *multiple threads at the same time.
 */
public class PingballServer {
    // AF: main server class, deals with network connections and maintains the data being passed through the network
    // RI: serverSocket is not null, consoleWorker and dispatcher are not null, serverRequestQueue and clientQueueMap are not null. 
    //      Port is an integer between 0 and 65535.

    /** Default server port. */
    private static final int DEFAULT_PORT = 10987;
    /** Maximum port number as defined by ServerSocket. */
    private static final int MAXIMUM_PORT = 65535;

    private ServerSocket serverSocket;

    /** queue of pending requests to be handled by the server*/
    private LinkedBlockingQueue<Request> serverRequestQueue;
    /** map of connected clients and their workers. */
    private final HashMap<String, PingballWorker> clientQueueMap;
    /** reads requests from the console and places them on the queue*/
    private Thread consoleWorker;
    /** dispatches tasks from the queue and sends them to the correct client */
    private final Thread dispatcher;
    
    private final int port;


    /**
     * Make a PingballServer that listens for connections on port.
     * 
     * @param port port number, requires 0 <= port <= 65535
     * 
     * @throws IOException if an error occurs opening the server socket
     */
    public PingballServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);

        this.serverRequestQueue = new LinkedBlockingQueue<Request>();
        this.clientQueueMap = new HashMap<String, PingballWorker>();
        this.dispatcher = new Thread( new ServerDispatcher(serverRequestQueue, clientQueueMap));
        this.consoleWorker = new Thread(new ConsoleWorker(serverRequestQueue));
        
        dispatcher.start();
        consoleWorker.start();
        
        checkRep();
    }

    /**
     * Checks the representation of the the PingballServer, as described above
     * 
     */
    private void checkRep(){
        boolean goodRep = (this.consoleWorker != null) && (this.dispatcher != null) && 
                (this.serverRequestQueue != null) && (this.clientQueueMap != null) && (this.serverSocket != null) &&
                (this.port >= 0) && (this.port <= MAXIMUM_PORT);
        if (!goodRep){
            throw new RuntimeException("PingballServer not initialized properly.");
        }
    }

    /**
     * Run the server, listening for client connections and handling them.
     * Never returns unless an exception is thrown.
     * 
     * @throws IOException if the main server socket is broken
     *                     (IOExceptions from individual clients do *not* terminate serve())
     */
    public void serve() throws IOException {        
        //uses private methods: handleConnections(Socket socket)

        while (true) {

            // block until a client connects
            final Socket socket = serverSocket.accept();
            Thread pingballWorker = new Thread(new PingballWorker(serverRequestQueue, new LinkedBlockingQueue<Request>(), socket));
            pingballWorker.start();
        }
    }



    /**
     * Start a PingballServer using the given arguments.
     * 
     * <br> Usage:
     *      PingballServer [--port PORT]
     * 
     * <br> If port isn't given, use the default server port
     * 
     * @param args arguments as described
     */
    public static void main(String[] args) {
        int port = DEFAULT_PORT;

        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        try {
            while ( ! arguments.isEmpty()) {
                String flag = arguments.remove();
                try {
                    if (flag.equals("--port")) {
                        port = Integer.parseInt(arguments.remove());
                        if (port < 0 || port > MAXIMUM_PORT) {
                            throw new IllegalArgumentException("port " + port + " out of range");
                        } 
                    }
                    else {
                        throw new IllegalArgumentException("unknown option: \"" + flag + "\"");
                    }
                }
                catch (NoSuchElementException nsee) {
                    throw new IllegalArgumentException("missing argument for " + flag);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("unable to parse number for " + flag);
                }
            }
        } catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
            System.err.println("usage: PinballServer [--port PORT]");
            return;
        }

        try {
            runPingballServer(port);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

    }


    private static void runPingballServer(int port) throws IOException {
        PingballServer server = new PingballServer(port);
        server.serve();
        
    }
}
