package phase1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import physics.Angle;
import physics.Vect;

/**
 *  TODO: FLIPPERS BOARD IS INCORRECT
 *      missing absorber, + rotation in flippers
 *      
 * Provides access to board configurations from problem set.
 * 
 */
public class BenchmarkBoards {
    
    public static Board createBoard(String boardType) {
        Map<String, Board> nameToBoard = new HashMap<String, Board>();
        nameToBoard.put("default", getDefaultBoard());
        nameToBoard.put("absorber", getAbsorberBoard());
        nameToBoard.put("flippers", getFlippersBoard());
        
        if (!nameToBoard.containsKey(boardType)) {
            throw new RuntimeException("Invalid board type");
        }
        return nameToBoard.get(boardType);
    }
    
    /**
     * @return Board with three balls
     */
    public static Board getBoardWithOnlyBalls() {
        Board board = new Board();
        
        // add gadgets
        List<Gadget> gadgets = new ArrayList<Gadget>();
        gadgets.add(new Ball(new Vect(0, 50), 10.25, 15.25));
        gadgets.add(new Ball(new Vect(0, 0), 19.25, 3.25));
        gadgets.add(new Ball(new Vect(0, 0), 1.25, 5.25));
        
        board.insertGadgets(gadgets);
        return board;
    }
    
    /**
     * @return problem set's Board with name "default"
     */
    public static Board getDefaultBoard() {
        Board board = new Board();
        // add gadgets
        List<Gadget> gadgets = new ArrayList<Gadget>();
        gadgets.add(new Ball(new Vect(0, 0), 1.25, 1.25));
        gadgets.add(new CircleBumper(1, 10));
        gadgets.add(new TriangleBumper(12, 15, Angle.DEG_180));
        gadgets.add(new SquareBumper(0, 17));
        gadgets.add(new SquareBumper(1, 17));
        gadgets.add(new SquareBumper(2, 17));
        gadgets.add(new CircleBumper(7, 18));
        gadgets.add(new CircleBumper(8, 18));
        gadgets.add(new CircleBumper(9, 18));
        
        board.insertGadgets(gadgets);
        return board;
    }
    
    /**
     * @return problem set's Board with name "absorber"
     */
    public static Board getAbsorberBoard() {
        Board board = new Board();
        
        // add gadgets
        List<Gadget> gadgets = new ArrayList<Gadget>();
        gadgets.add(new Ball(new Vect(0, 0), 10.25, 15.25));
        gadgets.add(new Ball(new Vect(0, 0), 19.25, 3.25));
        gadgets.add(new Ball(new Vect(0, 0), 1.25, 5.25));
        gadgets.add(new TriangleBumper(19, 0, Angle.DEG_90));
        
        Gadget absorber = new Absorber(0, 18, 20, 2);
        gadgets.add(absorber);
        
        // all circle bumpers trigger the absorber
        List<Gadget> triggeredGadgets = new ArrayList<Gadget>();
        triggeredGadgets.add(absorber);
        gadgets.add(new CircleBumper(1, 10, triggeredGadgets));
        gadgets.add(new CircleBumper(2, 10, triggeredGadgets));
        gadgets.add(new CircleBumper(3, 10, triggeredGadgets));
        gadgets.add(new CircleBumper(4, 10, triggeredGadgets));
        gadgets.add(new CircleBumper(5, 10, triggeredGadgets));
        
        board.insertGadgets(gadgets);
        return board;
    }
    
    /**
     * @return problem set's Board with name "flippers"
     */
    public static Board getFlippersBoard() {
        Board board = new Board();
 
        // add gadgets
        List<Gadget> gadgets = new ArrayList<Gadget>();
        gadgets.add(new Ball(new Vect(0, 0), 0.25, 3.25));
        gadgets.add(new Ball(new Vect(0, 0), 5.25, 3.25));
        gadgets.add(new Ball(new Vect(0, 0), 10.25, 3.25));
        gadgets.add(new Ball(new Vect(0, 0), 15.25, 3.25));
        gadgets.add(new Ball(new Vect(0, 0), 19.25, 3.25));
        
        Gadget leftFlipper1 = new LeftFlipper(0, 8, Angle.DEG_90);
        Gadget leftFlipper2 = new LeftFlipper(4, 10, Angle.DEG_90);
        Gadget leftFlipper3 = new LeftFlipper(9, 8, Angle.DEG_90);
        Gadget leftFlipper4 = new LeftFlipper(15, 8, Angle.DEG_90);
        
        Gadget rightFlipper1 = new RightFlipper(2, 15, Angle.ZERO);
        Gadget rightFlipper2 = new RightFlipper(17, 15, Angle.ZERO);
        
        gadgets.add(leftFlipper1);
        gadgets.add(leftFlipper2);
        gadgets.add(leftFlipper3);
        gadgets.add(leftFlipper4);
        gadgets.add(rightFlipper1);
        gadgets.add(rightFlipper2);
        
        gadgets.add(new CircleBumper(5, 18));
        gadgets.add(new CircleBumper(7, 13));
        gadgets.add(new CircleBumper(0, 5, Arrays.asList(leftFlipper1)));
        gadgets.add(new CircleBumper(5, 5));
        gadgets.add(new CircleBumper(10, 5, Arrays.asList(leftFlipper3)));
        gadgets.add(new CircleBumper(15, 5, Arrays.asList(leftFlipper4)));
        
        gadgets.add(new TriangleBumper(19, 0, Angle.DEG_90));
        gadgets.add(new TriangleBumper(10, 18, Angle.DEG_180));
        
        Gadget absorber = new Absorber(0, 19, 20, 1, Arrays.asList(rightFlipper1, rightFlipper2));
        absorber.makeSelfTriggering();
        gadgets.add(absorber);
        
        board.insertGadgets(gadgets);
        return board;
        
    }
    
    
    

}
