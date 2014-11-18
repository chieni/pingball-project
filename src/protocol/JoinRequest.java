package protocol;

import java.util.*;
import java.util.concurrent.BlockingQueue;

import physics.Vect;
import server.PingballWorker;

public class JoinRequest implements Request {
    
    // AF: a message from the console that informs the server that the two boards should be connected
    // RI: the two boards must not have the same name (cannot be the same board)
    
    private final String joiningOrientation;
    private final String boardNameLeftOrTop;
    private final String boardNameRightorBottom;

    public JoinRequest(String joiningOrientation, String boardNameLeftOrTop, String boardNameRightorBottom) {
        this.joiningOrientation=joiningOrientation;
        this.boardNameLeftOrTop=boardNameLeftOrTop;
        this.boardNameRightorBottom=boardNameRightorBottom;
    }

    @Override
    public String destinationBoard() {
        throw new UnsupportedOperationException();
    }

    /**
     * Checks that boards are joined only to other boards
     * 
     */
    public void checkRep() {
        if (!boardNameLeftOrTop.equals(boardNameRightorBottom)) {
            throw new RuntimeException("Cannot join the same board to itself");
        }
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
        return joiningOrientation;
    }

    @Override
    public String firstBoard() {
        return boardNameLeftOrTop;
    }

    @Override
    public String secondBoard() {
        return boardNameRightorBottom;
    }

    @Override
    public boolean isJoin() {
        return true;
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
    public String clientSender(){
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
