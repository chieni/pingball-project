package protocol;

import java.util.*;
import java.util.concurrent.BlockingQueue;

import physics.Vect;
import server.PingballWorker;

public class MapRequest implements Request{
    // AF: a mapping between board names and the parts on the server 
    //     responsible for the communication to the client
    //RI: none
    
    private final String boardName;
    private PingballWorker pingballWorker;
    
    public MapRequest(String boardName, PingballWorker pingballWorker) {
        this.boardName=boardName;
        this.pingballWorker= pingballWorker;
    }
    
    @Override
    public String destinationBoard() {
        return boardName;
    }

    @Override
    public PingballWorker destinationPingballWorker() {
        return pingballWorker;
    }

    @Override
    public Vect velocity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getX() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getY() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String boardJoiningOrientation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String firstBoard() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String secondBoard() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isJoin() {
        return false;
    }

    @Override
    public boolean isBallReallocation() {
        return false;
    }

    @Override
    public boolean isMapping() {
        return true;
    }

    @Override
    public String clientSender() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ArrayList<String> connectedBoardNames() {
       throw new UnsupportedOperationException();
    }

    @Override
    public boolean isTermination() {
        return false;
    }

}
