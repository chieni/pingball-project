package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.LinkedBlockingQueue;

import protocol.JoinRequest;
import protocol.Request;

/**
 * Reads for inputs from the console and converts them into
 * work items to be placed on the server's queue
 *
 *Thread safety argument:
 *Console worker implements a thread and only 
 *places Requests on the main blockingQueue, 
 *which is a thread safe data
 *structure.  This guarantees safe execution of
 *methods on the queue by
 *multiple threads at the same time.
 */
public class ConsoleWorker implements Runnable{
    //AF: responsible for creating Requests that join boards
    //RI: there exists a serverQueue in the ConsoleWorker object
    
    private LinkedBlockingQueue<Request> serverQueue;

    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));;
    
    public ConsoleWorker(LinkedBlockingQueue<Request> serverQueue) {
        this.serverQueue = serverQueue;    
        
        checkRep();
    }

    /**
     * Checks the representation of the the ConsoleWorker, as described above
     * 
     */
    private void checkRep(){
        boolean goodRep = this.serverQueue != null;
        if (!goodRep){
            throw new RuntimeException("ConsoleWorker not initialized properly.");
        }
    }
    @Override
    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                    serverQueue.add(createRequest(line));
            }
        }
        catch (IOException e) {
            System.out.println("could not process console request");
            e.printStackTrace();
        }
    }

    /**
     * Takes an input string from the console and produces
     * two Join Request items
     * @param line - input from client
     * @return
     */
    private Request createRequest(String line) throws IOException{
        String regex = "((h)|(v)) ([A-Za-z_][A-Za-z_0-9]+) ([A-Za-z_][A-Za-z_0-9]+)";
        if ( !line.matches(regex)) {
            throw new IOException("recieved invalid reply from server");
        }
        
        String[] tokens = line.split(" ");
        try {
            String orientation = tokens[0];
            String board1 = tokens[1];
            String board2 = tokens[2];
            Request requests = new JoinRequest(orientation, board1, board2); 
            return requests;
            
        } catch (NullPointerException e) {
            throw new IOException("recieved invalid reply from server");
        }
    }

}
