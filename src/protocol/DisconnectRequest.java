package protocol;

import java.util.*;
import java.util.concurrent.BlockingQueue;

import physics.Vect;
import server.PingballWorker;

public class DisconnectRequest implements Request {
    // AF: a message from the client that informs the server that the client has disconnected
    // RI: none
    private String clientName;
    private ArrayList<String> connectedBoards;
    
    /**
     * A request sent by a client when it terminates to revert joined board walls to solid
     * @param clientName the name of the board that was disconnected
     * @param connectedBoards a set of boards the disconnected board was joined to
     */
    public DisconnectRequest(String clientName, ArrayList<String> connectedBoards) {
        this.clientName=clientName;
        this.connectedBoards=connectedBoards;
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
        return clientName;
    }

    @Override
    public ArrayList<String> connectedBoardNames() {
        return connectedBoards;
    }

    @Override
    public boolean isTermination() {
        return false;
    }

}
