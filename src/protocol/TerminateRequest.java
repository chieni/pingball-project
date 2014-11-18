package protocol;

import java.util.*;
import java.util.concurrent.BlockingQueue;

import physics.Vect;
import server.PingballWorker;

public class TerminateRequest implements Request{
    // AF: a poison pill. Sent to clients that need to be terminated
    // RI: none
    
    /**
     * A request to close a board because the name is already in use
     */
    public TerminateRequest() {

    }

    @Override
    public String destinationBoard() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PingballWorker destinationPingballWorker() {
        throw new UnsupportedOperationException();
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
        return false;
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
        return true;
    }

}
