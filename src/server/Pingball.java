package server;

import grammar.BoardFileProcessor;
import grammar.GrammarTest;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import phase1.Ball;
import phase1.Board;
import physics.Vect;

/**
 * Pingball is a client that sends requests to 
 * the PingballServer and interprets its replies.
 */
public class Pingball {
    
    //AF: The Pingball client, responsible for
    //      1. playing the board as output
    //      2. Converting messages from the server to be implemented as actions on the board.
    //     
    //RI: board is not null, socket is not null, in and out are not null

    private final Board board;

    private Socket socket;

    private BufferedReader in;

    private PrintWriter out;


    /** Default server port. */
    private static final int DEFAULT_PORT = 10987;
    /** Maximum port number as defined by ServerSocket. */
    private static final int MAXIMUM_PORT = 65535;
    /** Default file name */
    private static final String DEFUALT_FILE_NAME ="default.pb";
    /** Queue of incoming messages for client */
    private final BlockingQueue<String> queue;

    private static final double timeResolutionSec = 0.0005;
    private static final double timeResolutionMilli = timeResolutionSec * 1000; 
    private static String boardName;

    /**
     * Singleplayer play:
     * Make a Pingball client and connect it to a 
     * server running on hostname at the specified
     * port.
     * 
     * @param Board - pingball board loaded from file
     */
    public Pingball(Board pingballBoard) {
        socket= new Socket();
        in=null;
        out=null;
        this.board = pingballBoard;
        queue = new LinkedBlockingQueue<String>();
        this.boardName="";
        
        checkRep();
    }

    /**
     * Multiplayer play:
     * Make a Pingball client and connect it to a 
     * server running on hostname at the specified
     * port.
     * 
     * @param hostname - the hostname of the server to connect to
     * @param port - the port of the server connection
     * @param Board - pingball board loaded from file
     * @throws IOException if can't connect
     */
    public Pingball(String hostname, int port, Board board) throws IOException {
        //Game pingBallGame = new file.parseIntoBoard <-- create a board
        socket = new Socket(hostname, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.board=board;
        queue = new LinkedBlockingQueue<String>();
        this.boardName="";
        
        checkRep();
    }
    
    
    /**
     * Checks the representation of Pingball, as described above
     * 
     */
    private void checkRep(){
        boolean goodRep = (this.board != null) && (Pingball.boardName != null) &&
                (this.socket != null);
        if (!goodRep){
            throw new RuntimeException("Pingball not initialized properly.");
        }
    }

    
    /**
     * Send a request to the pingball worker. Requires this is "open".
     * @param request - formatted string to be converted to 
     * pingball-worker readable form
     * @throws IOException if network or server failure
     */
    public void sendRequest(String request) throws IOException { 
        out.println(request);
        out.flush(); // important! make sure request actually gets sent

    }

    /**
     * Takes a reply from the server and mutates the board accordingly
     * @param reply - a string reply to be converted into an action on the board
     */
    private void convertToBoardAction(String reply) throws IOException{
        String[] args = reply.split(" ");
        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        while ( ! arguments.isEmpty()) {
            String flag = arguments.remove();
            System.out.println("flag"+flag);
            if (flag.equals("--join")){
                String orientation= arguments.remove();
                String firstBoard= arguments.remove();
                String secondBoard= arguments.remove();
                if (orientation.equals("v")){
                    if (firstBoard.equals(this.boardName)){
                        board.findAndUpdateInvisibleWall("top", secondBoard);
                    }else{
                        board.findAndUpdateInvisibleWall("bottom", firstBoard);
                    }
                }
                else{
                    if (firstBoard.equals(this.boardName)){
                        board.findAndUpdateInvisibleWall("left", secondBoard);
                    }else{
                        board.findAndUpdateInvisibleWall("right", firstBoard);
                    }
                }
            }
            else if (flag.equals("--ball")){
                System.out.println("Pingball message to board");
                System.out.println(arguments);
                String[] sizes = arguments.remove().split(",");
                int xVect = Integer.parseInt(sizes[0]);
                int yVect = Integer.parseInt(sizes[1]);
                String[] location = arguments.remove().split(",");
                int xLoc = Integer.parseInt(location[0]);
                int yLoc = Integer.parseInt(location[1]);
                board.addBalltoBoard(new Ball(new Vect(xVect,yVect), xLoc, yLoc));
            }
            else if(flag.equals("--end")){
                in.close();
                out.close();
                socket.close();
            }
            else if(flag.equals("--disconnect")){
                String disconnectedBoard=arguments.remove();
                board.findAndUpdateSolidWall(disconnectedBoard);
            }
        }
    }


    /**
     * Use a PingballServer to host the pingball board client
     * <br> Usage:
     *      Pingball [--host HOST] [--port PORT] FILE
     * 
     * <br> HOST is an optional hostname or IP address 
     *      of the server to connect to. If no HOST is
     *      provided, then the client starts in single
     *      -machine play mode
     *      
     * <br> PORT is an optional integer in the range 0
     *      to 65535 inclusive, specifying the port 
     *      where the server is listening for incoming
     *      connections. The default port is 10987.
     *      
     * <br> FILE is a required argument specifying a 
     *  file pathname of the Pingball board that this 
     *      client should run
     * 
     * @param args arguments as described
     * @throws IOException 
     * @throws InterruptedException 
     */

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = DEFAULT_PORT;
        String host = null;
        Optional<File> file = Optional.empty();       

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
                    } else if (flag.equals("--host")) {
                        host = arguments.remove();
                    } else if (flag.endsWith(".pb")) {//file does not have a flag  
                        file = Optional.of(new File("resources/" + flag));
                        if ( ! file.get().isFile()) {
                            throw new IllegalArgumentException("file not found: \"" + file + "\"");
                        }

                    }
                    else {
                        throw new IllegalArgumentException("unknown option or file type: \"" + flag + "\"");
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
            System.err.println("usage: Pinball [--host HOSTNAME] [--port PORT] FILE");
            throw new IllegalArgumentException("usage: Pinball [--host HOSTNAME] [--port PORT] FILE");
        }

        try {
            if (host!= null) {
                runPingballClientMultiPlayer(host, port, file);
            } else {
                runPingballClientSinglePlayer(file);
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

 
    /**
     * Run pingball game on the server 
     * 
     * @param hostname - the hostname of the server to connect to
     * @param port - the port of the server connection
     * @param file - file to creat the pingball board
     * @throws IOException - if there was an issue reading the board from the file
     * @throws InterruptedException - if there was an issue running the board
     */
    private static void runPingballClientMultiPlayer(String host, int port, Optional<File> file) throws IOException, InterruptedException {
        if (!file.isPresent()){ 
            file = Optional.of(new File("resources/" + DEFUALT_FILE_NAME));
        }

        BoardFileProcessor processor = new BoardFileProcessor(file);
        Board board = processor.createBoard();
        String boardName = processor.getBoardName();
        Pingball client = new Pingball(host, port, board);

        // first request - send board name
        client.sendRequest("--name " + boardName);

        client.setBoardName(boardName);
        client.runBoard(board);
    }
    
    /**
     * Assigns a name to the board
     * @param name the name of the boardName
     */
    private void setBoardName(String name) {
        this.boardName= name;
        
    }

    /**
     * Runs the board by printing to the console
     * updates the board by reading inputs from the server. 
     * @param board - the pingball board to update and run
     * @throws IOException - if there was an issue reading the board from the file
     * @throws InterruptedException - if there was an issue running the board
     */
    private void runBoard(Board board)
            throws InterruptedException, IOException {
        new Thread(new Runnable(){
            public void run(){
                try{
                    for (String line = in.readLine(); line != null; line = in.readLine()) {
                        queue.put(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally{
                   System.out.println("Thread is closing");
                }
            }
        }).start();

        while (true){
            Thread.sleep(300);
            while (queue.peek()!=null){
                String take = queue.take();
                System.out.println("take:" + take);
                convertToBoardAction(take);
            }
            board.updateBoard(.05);
            updateBallsInNetwork(this, board);
            System.out.println(board.toPrint());
        }
    }

    
    /**
     *look through all balls and see if they have changed to invisible trajectories
     *send correspond message to server
     *remove ball from client
     * @param client - the client thread
     * @param board - pingball board
     * @throws IOException 
     */
    private static void updateBallsInNetwork(Pingball client, Board board) throws IOException {
        List<Ball> ballsToDelete= new ArrayList<Ball>();
        for (Ball ball : board.getBalls()) {
            String message = ball.getBallTrajectoryMessage();
            if (!message.isEmpty()) {
                ballsToDelete.add(ball);
                client.sendRequest(message);
            }
        } 
        for (Ball ball: ballsToDelete){
            board.removeBallfromBoard(ball);
        }
    }

    /**
     * Run pingball game in single player mode
     * @param file
     * @throws IOException
     */
    private static void runPingballClientSinglePlayer(Optional<File> file) throws IOException {
        if (!file.isPresent()){
            file = Optional.of(new File("resources/" + DEFUALT_FILE_NAME));
        }
        BoardFileProcessor processor = new BoardFileProcessor(file);
        Board board = processor.createBoard();
        String boardName = processor.getBoardName();
        Pingball game = new Pingball(board);


        //simply run the board
        long startTime = System.currentTimeMillis();
        while (true) {
            long nextTime = System.currentTimeMillis();
            if (nextTime - startTime > timeResolutionMilli) {
                board.updateBoard(timeResolutionSec);
                startTime = nextTime;
            }
            System.out.println(board.toPrint());

        }
    }

}
