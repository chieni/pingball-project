package protocol;

import java.util.*;
import java.util.concurrent.BlockingQueue;

import physics.Vect;
import server.PingballWorker;


/**
 * An interface for requests from the console and pingball clients
 *
 */
public interface Request {
    
    /**
     * For requests that act on one other board
     * @return the name of the board getting acted upon
     */
    public String destinationBoard();
    
    /**
     * For requests that must know the pingballWorker destination
     * @return the name of the thread getting acted upon
     */
    public PingballWorker destinationPingballWorker();
    
    /**
     * For requests that involve a ball
     * @return a vector describing the velocity of a ball
     */
    public Vect velocity();
    
    /**
     * For requests that involve a ball
     * @return the x-coordinate location of the ball for the new board
     */
    public int getX();
    
    /**
     * For requests that involve a ball
     * @return the y-coordinate location of the ball for the next board
     */
    public int getY();
    
    /**
     * For requests that involve board joining
     * @return the orientation for the board joining (verticle, horizontal)
     */
    public String boardJoiningOrientation();
    
    /**
     * For requests that involve board joining
     * @return the first board that is named in the console joining call (left or top)
     */
    public String firstBoard();
    
    /**
     * For requests that involve board joining
     * @return the second board that is named in the console joining call (right or bottom)
     */
    public String secondBoard();
    
    /**
     * @return true if request involves board joining
     */
    public boolean isJoin();
    
    /**
     * @return true if request involves ball reallocation
     */
    public boolean isBallReallocation();
    
    /**
     * @return true if request involves mapping of the board and pingballWorker
     */
    public boolean isMapping();
    
    /**
     * @return the name of the client the message is sent from
     */
    public String clientSender();
    
    /**
     * @return a set of connected boards
     */
    public ArrayList<String> connectedBoardNames();
    
    /**
     * @return true if this request should terminate a thread
     */
    public boolean isTermination();
}
