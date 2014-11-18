package protocol;

import java.util.*;
import java.util.concurrent.BlockingQueue;

import physics.Vect;
import server.PingballWorker;

public class BallRequest implements Request {
    
    //AF: a message from the client that tells the server
    //    to send a ball to another board, located at 
    //    xLoc, yLoc, with a given velocity
    //RI: the ball must be located within the board and its velocity must be specified
    
    private final String boardName;
    private Vect velocity;
    private int xLoc;
    private int yLoc;
    
    /**
     * A request that involves ball reallocation
     * @param boardName the board that the ball is going to
     * @param velocity the velocity of the ball
     * @param x the x coordinate the ball should be placed in the new board
     * @param y the y coordinate the ball should be placed in the new board
     */
    public BallRequest(String boardName, Vect velocity, int x, int y) {
        this.boardName=boardName;
        this.velocity=velocity;
        this.xLoc=x;
        this.yLoc=y;
    }
    
    /**
     * Checks the representation invariant of the Ball, namely that the velocity and position of the ball is are in a 
     * reasonable position (within the bounds of the board)
     * 
     */
    public void checkRep() {
        boolean correctRep = (this.xLoc > -1 && this.yLoc > -1 && this.xLoc < 21 && this.yLoc < 21 && this.velocity != null);
        if (!correctRep) {
            throw new RuntimeException("Invalid ball request");
        }
    }
    
    @Override
    public String destinationBoard() {
        return boardName;
    }

    @Override
    public PingballWorker destinationPingballWorker() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Vect velocity() {
        return velocity;
    }

    @Override
    public int getX() {
        return xLoc;
    }

    @Override
    public int getY() {
        return yLoc;
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
        return true;
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
