package server;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import physics.Vect;
import protocol.BallRequest;
import protocol.DisconnectRequest;
import protocol.MapRequest;
import protocol.Request;
import protocol.TerminateRequest;

/**
 * Reads for inputs from the pingball client and converts them into
 * work items to be placed on the server's queue
 * 
 *Thread safety argument:
 *Pingball worker implements a thread and only 
 *places Requests on the main blockingQueue, 
 *which is a thread safe data
 *structure.  This guarantees safe execution of
 *methods on the queue by
 *multiple threads at the same time.
 */

public class PingballWorker implements Runnable {
    //AF: responsible for 
    //      1. creating Requests that are generated as messages in the board
    //      and sending them through the network to be distributed to the other
    //      clients
    //      2. converting Requests into passable messages to the client
    //
    //RI: serverQueue, clientSocket, in, out, boardName are not null.

    private LinkedBlockingQueue<Request> serverQueue;
    private Socket clientSocket;

    private BufferedReader in;

    private PrintWriter out;

    /** list of boards that are connected to this client */
    private ArrayList<String> connectedBoards;

    private String boardName;



    public PingballWorker(LinkedBlockingQueue<Request> serverQueue, 
            LinkedBlockingQueue<Request> requestedQueue, Socket clientSocket) throws IOException {
        this.serverQueue = serverQueue;
        this.clientSocket = clientSocket;
        this.connectedBoards= new ArrayList<String>();
        this.boardName = "";

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        
        checkRep();
    }

    /**
     * Checks the representation of PingballWorker, as described above
     * 
     */
    private void checkRep(){
        boolean goodRep = (this.boardName != null) && (this.serverQueue != null) && 
                (this.clientSocket != null);
        if (!goodRep){
            throw new RuntimeException("PingballWorker not initialized properly.");
        }
    }

    @Override
    public void run() {
        String line;
        try {
            while ((line = in.readLine())!=null) {
                Request request = createRequest(line);
                serverQueue.add(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Request request= new DisconnectRequest(boardName, connectedBoards);
            serverQueue.add(request);
            out.close();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * pingball-worker readable form
     * @throws IOException if network or server failure
     */
    public void sendRequest(String message) throws IOException { 
        out.print(message);
        out.flush(); // important! make sure request actually gets sent
    }

    /**
     * Takes an input string from the client and produces
     * a Request item
     * @param line - input from client
     * @return request item
     */
    private Request createRequest(String line) {
        String[] args = line.split(" ");
        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        String connectedBoardName="";
        Vect ballVect=new Vect(0,0);
        int xLoc=0;
        int yLoc=0;

        while ( ! arguments.isEmpty()) {
            String flag = arguments.remove();
            if (flag.equals("--board")) {
                connectedBoardName=arguments.remove();
            } else if (flag.equals("--velocity")){
                String[] sizes = arguments.remove().split(",");
                int xVect = Integer.parseInt(sizes[0]);
                int yVect = Integer.parseInt(sizes[1]);
                ballVect= new Vect(xVect, yVect);
            } else if (flag.equals("--position")){
                String[] sizes = arguments.remove().split(",");
                xLoc = Integer.parseInt(sizes[0]);
                yLoc = Integer.parseInt(sizes[1]);
            } else if (flag.equals("--name")) {
                String boardName = arguments.remove();
                this.boardName=boardName;
                Request request = new MapRequest(boardName, this);
                return request;
            }
        }
        Request request= new BallRequest(connectedBoardName, ballVect, xLoc, yLoc);

        return request;
    }

    /**
     * Takes an input message from the dispatcher and produces a String line
     * @param message - input from dispatcher
     * @return string line
     */
    private String createStringFromRequest(Request message){
        if (message.isJoin()){
            connectedBoards.add(message.firstBoard());
            connectedBoards.add(message.secondBoard());
            return "--join "+ message.boardJoiningOrientation()+ " "+ message.firstBoard()+ " "+ message.secondBoard();
        }else if (message.isBallReallocation()){
            return "--ball " + (int) message.velocity().x()+","+(int)message.velocity().y()+" "+ message.getX()+ ","+ message.getY(); 
        }else if (message.isTermination()) {
            return "--end";
        }
        else{
            connectedBoards.remove(message.clientSender());
            return "--disconnect "+message.clientSender();
        }
    }

    /**
     * Sends message to the client through the socket
     */
    public void passMessage(Request newRequest){
        String strRequest= createStringFromRequest(newRequest);
        out.println(strRequest);
        out.flush();
    }


}
