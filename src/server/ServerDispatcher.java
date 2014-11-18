package server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import protocol.Request;
import protocol.TerminateRequest;

/**
 * ServerDispatcher "handles" the Server's queue, i.e.
 * it removes tasks from the Server's queue and passes
 * them to the corresponding threads
 * 
 * Thread safety argument:
 * Using a thread safe blockingQueue allows us to to 
 * remove Requests from the queue and send them to the 
 * PingballWorkers so that they are processed in a concurrent
 * fashion
 */
public class ServerDispatcher implements Runnable{

    // AF:  Part of the server that is responsible for sending replies to the correct clients

    // RI: serverQueue and clientQueueMap are not null
    
    private final LinkedBlockingQueue<Request> serverQueue;
    private HashMap<String, PingballWorker> clientQueueMap;

    public ServerDispatcher(LinkedBlockingQueue<Request> serverQueue, HashMap<String, PingballWorker> clientQueueMap2) {
        this.serverQueue=serverQueue;
        this.clientQueueMap=clientQueueMap2;
    }
    
    /**
     * Checks the representation of ServerDispatcher, as described above
     * 
     */
    private void checkRep(){
        boolean goodRep = (this.serverQueue != null) && (this.clientQueueMap != null);
        if (!goodRep){
            throw new RuntimeException("ServerDispatcher not initialized properly.");
        }
    }

    
    @Override
    public void run() {
        try {
            while (true) {
                Request myRequest = serverQueue.take();
                if (myRequest.isMapping()){ //tries to map
                    if (clientQueueMap.containsKey(myRequest.destinationBoard())){ //if there is already a board with this name
                        PingballWorker destinationPingballWorker= myRequest.destinationPingballWorker();
                        destinationPingballWorker.passMessage(new TerminateRequest()); //terminate this board
                    }else{
                        clientQueueMap.put(myRequest.destinationBoard(), myRequest.destinationPingballWorker());//otherwise map it
                    }
                }else if(myRequest.isJoin()){ //if trying to join two boards
                    PingballWorker destinationPingballWorker= clientQueueMap.get(myRequest.firstBoard());
                    //send request to both boards
                    PingballWorker destinationPingballWorker2= clientQueueMap.get(myRequest.secondBoard());
                    if (destinationPingballWorker!=null && destinationPingballWorker2!=null){ //makes sure that both boards exist
                        destinationPingballWorker.passMessage(myRequest);
                        destinationPingballWorker2.passMessage(myRequest);
                    }
                }
                else if (myRequest.isBallReallocation()){ //if trying to reallocate ball
                    PingballWorker destinationPingballWorker= clientQueueMap.get(myRequest.destinationBoard());
                    destinationPingballWorker.passMessage(myRequest);//send request to the board the ball is going to
                }
                else{ //if trying to disconnect a board
                    clientQueueMap.remove(myRequest.clientSender()); //the disconnected board should no longer be in the map
                    for (String connectedBoardName: myRequest.connectedBoardNames()){ //go through all the boards it is connected to
                        if (!connectedBoardName.equals(myRequest.clientSender())) {
                            PingballWorker destinationPingballWorker= clientQueueMap.get(connectedBoardName);
                            destinationPingballWorker.passMessage(myRequest); //send requests to each board to make connected invisible wall solid
                        }
                    }
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

    }
}
